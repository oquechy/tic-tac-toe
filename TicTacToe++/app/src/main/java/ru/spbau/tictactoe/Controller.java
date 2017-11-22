package ru.spbau.tictactoe;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;

import java.util.concurrent.TimeUnit;

import ru.spbau.tictactoe.Logic.Logic;
import ru.spbau.tictactoe.Network.Client;
import ru.spbau.tictactoe.Network.NetAnotherPlayer;
import ru.spbau.tictactoe.Network.Server;
import ru.spbau.tictactoe.ui.UI;

import static android.content.Context.WIFI_SERVICE;

/**
 * main purpose of this class is maintaining the game cycle,
 * keeping current state of the app
 * and transmitting information between other classes
 */
public class Controller {

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

    private boolean paused = false;

    /**
     * cross is true and nought is false
     */
    private static boolean myType;

    private static State state;
    private static UI ui;
    private static Server server;
    private static Client client;
    private static Logic logic = new Logic();


    /**
     * either bot or net friend
     */
    private static NetAnotherPlayer friend;

    /**
     * provides access to ui as a static field
     *
     * @param ui class for interaction with user
     */
    public static void initController(UI ui) {
        Controller.ui = ui;
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
     */
    public static void optionGameWithBot() {
        state = State.CREATE_FIELD;
        myType = true;

        final Bot bot = new Bot(logic.getBoard());
        friend = new NetAnotherPlayer() {
            @Override
            public void setOpponentTurn(Turn t) {
            }

            @Override
            public Turn getOpponentTurn() {
                return new Turn(bot.makeTurn());
            }

            @Override
            public boolean getFirstPlayer() {
                return true;
            }

            @Override
            public String getName() {
                return bot.getName();
            }
        };

        boolean firstPlayer = myType;
        state = firstPlayer ? State.MY_TURN : State.FRIENDS_TURN;
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
    public static void verifyTurn(int x, int y) {
        Turn newTurn = new Turn(myType, x, y);

        if (state == State.MY_TURN && logic.verifyTurn(newTurn.convertToTurn())) {
            ui.disableHighlight();
            ui.applyTurn(getX(newTurn), getY(newTurn), getMyType());
            logic.applyMyTurn(newTurn.convertToTurn());

            friend.setOpponentTurn(newTurn);

            if (!checkForWins(newTurn)) {
                state = State.FRIENDS_TURN;
                setOpponentTurn(friend.getOpponentTurn());
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }

    private static int getX(Turn newTurn) {
        return newTurn.x + 1;
    }

    private static int getMyType() {
        return myType ? 1 : -1;
    }

    /**
     * end of game on little board causes drawing of big sign
     * end of game on whole board causes start of new game
     *
     * @param newTurn is last made turn
     * @return whether game was ended
     */
    private static boolean checkForWins(Turn newTurn) {
        if (logic.isLittleWin()) {
            int littleWinCoords = logic.getLittleWinCoords();
            ui.smallWin(getXOfBoard(littleWinCoords),
                    getYOfBoard(littleWinCoords),
                    getPlayer(newTurn));
        }

        if (logic.isEndOfGame()) {
            state = State.END_OF_GAME;
            ui.displayResult(logic.getResult() == Result.CROSS ? 1 : -1);
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

    private static void newGame() {
        logic = new Logic();
        ui.setUpField();
        optionGameWithBot();
    }

    private static int getPlayer(Turn newTurn) {
        return newTurn.player ? 1 : -1;
    }

    private static int getXOfBoard(int littleWinCoords) {
        return littleWinCoords % 3 + 1;
    }

    private static int getYOfBoard(int littleWinCoords) {
        return littleWinCoords / 3 + 1;
    }

    /**
     * applies friend's new move and calls check for win
     * turn is supposed to be correct
     *
     * @param turn is new move
     */
    public static void setOpponentTurn(Turn turn) {
        if (state == State.FRIENDS_TURN) {
            logic.applyOpponentsTurn(turn.convertToTurn());
            ui.applyTurn(getX(turn), getY(turn), getFriendsType());

            if (!checkForWins(turn)) {
                state = State.MY_TURN;
                ui.setHighlight(getXOfNextBoard(turn), getYOfNextBoard(turn));
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }

    private static int getYOfNextBoard(Turn turn) {
        return turn.y % 3 + 1;
    }

    private static int getXOfNextBoard(Turn turn) {
        return turn.x % 3 + 1;
    }

    private static int getFriendsType() {
        return myType ? -1 : 1;
    }

    private static int getY(Turn turn) {
        return turn.y + 1;
    }

    /**
     * finds out ip address in wifi
     * @param activity is activity to get context from
     * @return ip string or error message
     */
    public static String getIPtoShow(Activity activity) {
        WifiManager wm = (WifiManager) activity.getApplicationContext().getSystemService(WIFI_SERVICE);
        int ipAddress = wm.getConnectionInfo().getIpAddress();
        return ipAddress == 0 ? "No connection" : Formatter.formatIpAddress(ipAddress);
    }
}
