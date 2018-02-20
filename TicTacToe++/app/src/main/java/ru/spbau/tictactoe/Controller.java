package ru.spbau.tictactoe;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import java.io.IOException;

import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Bot.Bot;
import ru.spbau.tictactoe.Bot.CleverBot.CleverBot;
import ru.spbau.tictactoe.Bot.MiniMaxBot.MiniMaxBot;
import ru.spbau.tictactoe.Bot.MonteCarloBot.MonteCarloBot;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Logic;
import ru.spbau.tictactoe.Logic.Result.Result;
import ru.spbau.tictactoe.Logic.Turn.Turn;
import ru.spbau.tictactoe.Network.Client;
import ru.spbau.tictactoe.Network.NetAnotherPlayer;
import ru.spbau.tictactoe.Network.Server;
import ru.spbau.tictactoe.Statistic.DataBase;
import ru.spbau.tictactoe.ui.UI;
import ru.spbau.tictactoe.utils.Converter;

import static android.content.Context.WIFI_SERVICE;

/**
 * main purpose of this class is maintaining the game cycle,
 * keeping current state of the app
 * and transmitting information between other classes
 */
public class Controller {

    private static final int PORT_NUMBER = 3030;

    private enum State {
        MAIN_MENU,                 // game with friend, game with bot, stats
        SHARE_IP,                  // allows friend to connect
        CONNECT_TO_FRIEND,         // get server's ip
        CREATE_FIELD,
        MY_TURN,
        FRIENDS_TURN,
        END_OF_GAME,
        STATS
    }

    private enum Player {
        CROSS,
        NOUGHT;

        public boolean isCross() {
            return this == CROSS;
        }

        public Player inverted() {
            return this == CROSS ? NOUGHT : CROSS;
        }
    }

    private final static int LOCAL_NET_MASK = (192) | (168 << 8);

    /**
     * cross is true and nought is false
     */
    private static Player myType;

    private static State state;
    private static UI ui;
    private static Server server;
    private static Client client;
    private static Logic logic = new Logic();
    private static DataBase dataBase;


    /**
     * either bot or net friend
     */
    private static NetAnotherPlayer friend;

    /**
     * provides access to ui as a field
     *
     * @param ui class for interaction with user
     */
    public static void initController(UI ui) {
        Controller.ui = ui;
        initDB(ui);
    }

    private static void initDB(Activity activity) {
        dataBase = new DataBase(activity.getApplicationContext());
    }

    /**
     * defines appearance of field at the beginning of the game
     */
    public static void initBoard() {
        if (state == State.MY_TURN) {
            ui.setHighlight(2, 2);
        }
    }

    /**
     * sets first player to real player and second player to new bot
     * and then switches state accordingly
     * @param botLevel parameter to determine difficulty of game
     */
    public static void optionGameWithBot(int botLevel) {
        state = State.CREATE_FIELD;
        myType = Player.CROSS;

        final Bot bot = getBot(botLevel);
        friend = new NetAnotherPlayer() {


            @Override
            public UITurn getOpponentTurn() {
                return new UITurn(bot.makeTurn());
            }

            @Override
            public void setOpponentTurn(UITurn t) {
                bot.getTurn(t);
            }

            @Override
            public boolean amIFirstPlayer() {
                return true;
            }

            @Override
            public String getName() {
                return bot.getName();
            }

            @Override
            public void receivePlayerType(boolean b) {
            }

            @Override
            public void newGameAsPlayer(boolean isCross) {
                bot.setBoard(logic.getBoard());
                bot.setPlayer(isCross ? Turn.Player.CROSS : Turn.Player.NOUGHT);
            }
        };

//        if (ui != null) {
//            ui.setUpField();
//        }

        state = myType.isCross() ? State.MY_TURN : State.FRIENDS_TURN;
    }

    @NonNull
    private static Bot getBot(int botLevel) {
        return botLevel == 1 ? new Bot(logic.getBoard())
                : botLevel == 2 ? new CleverBot(logic.getBoard())
                : botLevel == 3 ? new MonteCarloBot(logic.getBoard())
                : new MiniMaxBot(logic.getBoard());
    }

    /**
     * ui calls this method to verify new user's move
     * move is rejected if: <br>
     * a) it is not my turn or <br>
     * b) place to move into is incorrect
     * <p>
     * if move is correct, it is drawn and sent to friend
     * if end of game hasn't come, state switches and we become waiting to friend's turn
     *
     * @param x is horizontal projection of move [1..9]
     * @param y is vertical projection of move [1..9]
     */
    public static void verifyTurn(int x, int y) {
        UITurn newTurn = new UITurn(myType.isCross(), x, y);

        if (state == State.MY_TURN && logic.verifyTurn(newTurn.convertToTurn())) {
            ui.disableHighlight();
            ui.applyTurn(Converter.getX(newTurn), Converter.getY(newTurn), getMyType());
            logic.applyMyTurn(newTurn.convertToTurn());

            friend.setOpponentTurn(newTurn);

            if (!checkForEndOfGame(newTurn)) {
                state = State.FRIENDS_TURN;
                setOpponentTurn(friend.getOpponentTurn());
            }
        } else if (state == State.FRIENDS_TURN) {
            ui.incorrectTurnTime();
        }
    }

    private static int getMyType() {
        return myType.isCross() ? 1 : -1;
    }

    /**
     * end of game on little board causes drawing of big sign
     * end of game on whole board causes start of new game
     *
     * @param newTurn is last made turn
     * @return whether game was ended
     */
    private static boolean checkForEndOfGame(UITurn newTurn) {
        Status littleWin = logic.isLittleWin();
        if (littleWin == Status.NOUGHT || littleWin == Status.CROSS) {
            int littleWinCoords = logic.getLittleWinCoords();
            ui.smallWin(Converter.getXOfBoard(littleWinCoords),
                    Converter.getYOfBoard(littleWinCoords),
                    getPlayer(littleWin));
        }

        if (logic.isEndOfGame()) {
            state = State.END_OF_GAME;
            Result result = logic.getResult();
            ui.displayResult(result);
            dataBase.addRecord(result, friend.getName(), logic.getTurnCounter(), myType.isCross());

            return true;
        }
        return false;
    }

    public static void newGame() {
        logic = new Logic();
        myType = myType.inverted();
        state = myType.isCross() ? State.MY_TURN : State.FRIENDS_TURN;
        friend.newGameAsPlayer(myType.inverted().isCross());
        startGameCycle();
    }

    private static int getPlayer(Status status) {
        return status == Status.CROSS ? 1 : status == Status.NOUGHT ? -1 : 0;
    }

    /**
     * applies friend's new move and calls check for win
     * turn is supposed to be correct
     *
     * @param turn is new move
     */
    public static void setOpponentTurn(UITurn turn) {
        if (state == State.FRIENDS_TURN) {
            logic.applyOpponentsTurn(turn.convertToTurn());
            ui.applyTurn(Converter.getX(turn), Converter.getY(turn), getFriendsType());

            if (!checkForEndOfGame(turn)) {
                ui.clearMessage();
                state = State.MY_TURN;
                if (logic.getStatusOfInner(nextInnerBoard(turn)) == Status.GAME_CONTINUES) {
                    ui.setHighlight(Converter.getXOfNextBoard(turn), Converter.getYOfNextBoard(turn));
                } else {
                    ui.highlightAll();
                }
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }

    private static int nextInnerBoard(UITurn turn) {
        return turn.getY() % 3 * 3 + turn.getX() % 3;
    }

    private static int getFriendsType() {
        return myType.isCross() ? -1 : 1;
    }

    /**
     * finds out ip address in wifi
     * @param activity is activity to get context from
     * @return ip string or error message
     */
    public static String getIPtoShow(Activity activity) {
        int ipAddress = getIP(activity);
        return ipAddress == 0 ? "No connection" : Formatter.formatIpAddress(ipAddress);
    }

    public static int getIP(Activity activity) {
        WifiManager service = (WifiManager) activity.getApplicationContext()
                .getSystemService(WIFI_SERVICE);
        return service.getConnectionInfo().getIpAddress();
    }

    private static String formatIpAddress(int ip) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 3; ++i) {
            builder.append(ip % (1 << 8)).append('.');
            ip >>= 8;
        }
        builder.append(ip % (1 << 8));
        return builder.toString();
    }


    public static void optionJoinFriend(String text) throws IOException {
        state = State.CONNECT_TO_FRIEND;
        int ipTail = WordCoder.decode(text);

        String ip = Formatter.formatIpAddress(LOCAL_NET_MASK | ipTail << 16);
        try {
            connectToServer(ip);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void connectToServer(String ip)
            throws ExecutionException, InterruptedException, IOException {
        client = new Client();
        client.start("Client Lisa", ip, "3030");
        friend = client.getPlayer();
        String name = friend.getName();
        myType = friend.amIFirstPlayer() ? Player.CROSS : Player.NOUGHT;
        state = myType.isCross() ? State.MY_TURN : State.FRIENDS_TURN;
        System.out.println("state = " + state);
//        startGameCycle();
//            newSession(myTurn);
    }

    public static void startGameCycle() {
        if (state == State.FRIENDS_TURN) {
            setOpponentTurn(friend.getOpponentTurn());
        }
    }

    public static void optionInviteFriend()
            throws IOException {
        state = State.SHARE_IP;

        server = new Server();
        try {
            server.start("Server", 3030);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        myType = choosePlayer();
        state = myType.isCross() ? State.MY_TURN : State.FRIENDS_TURN;
        friend = server.getPlayer();
        friend.receivePlayerType(!myType.isCross());
    }

    private static Player choosePlayer() {
        boolean myTurn = true; //new Random().nextBoolean();
//        server.passTo(Boolean.toString(!myTurn));
        System.err.println("myTurn: " + myTurn);
        return myTurn ? Player.CROSS : Player.NOUGHT;
    }

    public static String getEncodedIP(Activity activity) {
        int ipAddress = getIP(activity);
        if (ipAddress == 0) {
            return "No connection";
        }

        String s = Formatter.formatIpAddress(ipAddress);
        String[] ipString = s.split("\\.");
        int ipTail = (Integer.parseInt(ipString[2])) + (Integer.parseInt(ipString[3]) << 8);

        return WordCoder.encode(ipTail);
    }

    public static String getDecodedIP(String manuda) {
        int ipTail = WordCoder.decode(manuda);

        String ip = Formatter.formatIpAddress(LOCAL_NET_MASK | ipTail << 16);
        return ip;
    }
}
