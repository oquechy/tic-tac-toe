package ru.spbau.tictactoe;

import android.app.Activity;
import android.net.wifi.WifiManager;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import ru.spbau.tictactoe.Logic.Logic;
import ru.spbau.tictactoe.Network.Client;
import ru.spbau.tictactoe.Network.NetAnotherPlayer;
import ru.spbau.tictactoe.Network.Server;
import ru.spbau.tictactoe.ui.UI;
import ru.spbau.tictactoe.utils.Converter;

import static  android.content.Context.WIFI_SERVICE;

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
    }

    private boolean paused = false;

    /**
     * cross is true and nought is false
     */
    private Player myType;

    private State state;
    private UI ui;
    private Server server;
    private Client client;
    private Logic logic = new Logic();


    /**
     * either bot or net friend
     */
    private NetAnotherPlayer friend;

    /**
     * provides access to ui as a  field
     *
     * @param ui class for interaction with user
     */
    public void initController(UI ui) {
        this.ui = ui;
    }

    /**
     * defines appearance of field at the beginning of the game
     */
    public void initBoard() {
        if (state == State.MY_TURN) {
            ui.setHighlight(2, 2);
        }
    }

    /**
     * sets first player to real player and second player to new bot
     * and then switches state accordingly
     */
    public void optionGameWithBot() {
        state = State.CREATE_FIELD;
        myType = Player.CROSS;

        final Bot bot = new Bot(logic.getBoard());
        friend = new NetAnotherPlayer() {
            @Override
            public void setOpponentTurn(UITurn t) {
            }

            @Override
            public UITurn getOpponentTurn() {
                return new UITurn(bot.makeTurn());
            }

            @Override
            public boolean amIFirstPlayer() {
                return true;
            }

            @Override
            public String getName() {
                return bot.getName();
            }
        };

        state = myType.isCross() ? State.MY_TURN : State.FRIENDS_TURN;
    }

    /**
     * ui calls this method to verify new user's move
     * move is rejected if:
     * a) it is not my turn or
     * b) place to move into is incorrect
     * <p>
     * if move is correct, it is drawn and sent to friend
     * if end of game hasn't come, state switches and we become waiting to friend's turn
     *
     * @param x is horizontal projection of move [1..9]
     * @param y is vertical projection of move [1..9]
     */
    public void verifyTurn(int x, int y) {
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
        } else {
            UI.incorrectTurnTime();
            System.err.println("incorrect turn time");
        }
    }

    private int getMyType() {
        return myType.isCross() ? 1 : -1;
    }

    /**
     * end of game on little board causes drawing of big sign
     * end of game on whole board causes start of new game
     *
     * @param newTurn is last made turn
     * @return whether game was ended
     */
    private boolean checkForEndOfGame(UITurn newTurn) {
        if (logic.isLittleWin()) {
            int littleWinCoords = logic.getLittleWinCoords();
            ui.smallWin(Converter.getXOfBoard(littleWinCoords),
                    Converter.getYOfBoard(littleWinCoords),
                    getPlayer(newTurn));
        }

        if (logic.isEndOfGame()) {
            state = State.END_OF_GAME;
            ui.displayResult(logic.getResult());
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            newGame();
            return true;
        }

        return false;
    }

    private void newGame() {
        logic = new Logic();
        ui.setUpField();
        optionGameWithBot();
    }

    private static int getPlayer(UITurn newTurn) {
        return newTurn.isCross() ? 1 : -1;
    }

    /**
     * applies friend's new move and calls check for win
     * turn is supposed to be correct
     *
     * @param turn is new move
     */
    public void setOpponentTurn(UITurn turn) {
        if (state == State.FRIENDS_TURN) {
            logic.applyOpponentsTurn(turn.convertToTurn());
            ui.applyTurn(Converter.getX(turn), Converter.getY(turn), getFriendsType());

            if (!checkForEndOfGame(turn)) {
                UI.clearMessage();
                state = State.MY_TURN;
                ui.setHighlight(Converter.getXOfNextBoard(turn), Converter.getYOfNextBoard(turn));
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }

    private int getFriendsType() {
        return myType.isCross() ? -1 : 1;
    }

    /**
     * finds out ip address in wifi
     *
     * @param activity is activity to get context from
     * @return ip string or error message
     */
    public static String getIPtoShow(Activity activity) {
        int ipAddress = getIP(activity);
        return ipAddress == 0 ? "No connection" : formatIpAddress(ipAddress);
    }

    private static int getIP(Activity activity) {
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


    public void optionConnectToFriend() {
        state = State.CONNECT_TO_FRIEND;

//        ui.getGameCode();           // could be possible to return to main menu
        setGameCode("");
    }

    public void setGameCode(String gameCode) {
        client = new Client();
        try {
            client.start("Client", "192.168.1.49", "3030");
            boolean myTurn = Boolean.parseBoolean(client.getFrom());
//            ui.switchTurn(myTurn);
            friend = client.getPlayer("Server");
            startGame(myTurn);
        } catch (IOException e) {
//            ui.networkError();
//            ui.getGameCode();
            e.printStackTrace();
        }
    }

    private void startGame(boolean myTurn) {
        if (!myTurn) {
            state = State.FRIENDS_TURN;
            setOpponentTurn(friend.getOpponentTurn());
        } else {
            state = State.MY_TURN;
        }
    }

    public void optionInviteFriend() {
        state = State.SHARE_IP;

        String name = "Server"/*ui.getMyName()*/;
        server = new Server();
        try {
            server.start(name, PORT_NUMBER);
            boolean myTurn = new Random().nextBoolean();
            server.passTo(Boolean.toString(!myTurn));
            friend = server.getPlayer("");
            startGame(myTurn);
        } catch (IOException e) {
//            ui.networkError();
            e.printStackTrace();
        }
    }
}
