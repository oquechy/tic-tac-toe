package ru.spbau.tictactoe;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Logic.Logic;
import ru.spbau.tictactoe.Network.Client;
import ru.spbau.tictactoe.Network.IPGetter;
import ru.spbau.tictactoe.Network.NetAnotherPlayer;
import ru.spbau.tictactoe.Network.Server;
import ru.spbau.tictactoe.ui.UI;

public class Controller {

    private static State state;
    private static UI ui;
    private static Server server;
    private static Client client;
    private static Logic logic = new Logic();
    //    private Stats stats = new Stats();
    private static NetAnotherPlayer friend;
    private static boolean myType;
    private boolean paused = false;

    public static void initController(UI ui) {

        Controller.ui = ui;
    }

    public static void optionGameWithBot() {
//        newGameWarningIfPaused();

        state = State.CREATE_FIELD;
        myType = true;

        final Bot bot = new Bot(logic.getBoard());
        friend = new NetAnotherPlayer() {
            ru.spbau.tictactoe.Logic.Turn.Turn turn;

            @Override
            public Turn getOpponentTurn() {
                Turn turn = new Turn(bot.makeTurn());
                return turn;
            }

            @Override
            public void setOpponentTurn(Turn t) {
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

//        initField();                   // if previous game wasn't finished, you can clear board here
        boolean firstPlayer = myType; //ui.chooseFirstPlayer();
//        ui.switchTurn(firstPlayer);    // true if it is my turn
//        logic.setFirstPlayer(firstPlayer);

        state = firstPlayer ? State.MY_TURN : State.FRIENDS_TURN;
    }

    public static void verifyTurn(int x, int y) {
        Turn newTurn = new Turn(myType, x, y);

        if (state == State.MY_TURN && logic.verifyTurn(newTurn.convertToTurn())) {
            ui.applyTurn(newTurn.x + 1, newTurn.y + 1, myType ? 1 : -1);
            friend.setOpponentTurn(newTurn);
            logic.applyMyTurn(newTurn.convertToTurn());

            if (!checkForWins(newTurn)) {             // if not end of game
                state = State.FRIENDS_TURN;

//                ui.switchTurn(false);                 // friend's turn
                System.out.println(newTurn.toString());
                setOpponentTurn(friend.getOpponentTurn());
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }

    private static boolean checkForWins(Turn newTurn) {  // turn: who and where
        if (logic.isLittleWin()) {         // applies new turn & checks for little win
            int littleWinCoords = logic.getLittleWinCoords();
            ui.smallWin(littleWinCoords % 3 + 1, littleWinCoords / 3 + 1, myType ? 1 : -1);
        }

        if (logic.isEndOfGame()) {                // first player wins/second player wins/draw
            state = State.END_OF_GAME;

//            GameLog gameLog = logic.getGameLog(); // am I a winner? how many turns?
//            gameLog.writeFriendsName(friend.getName());
//            stats.getNewRecord(gameLog);
//            ui.displayResult(logic.getResult());  // and go to main menu then

            return true;
        }
        return false;
    }

    public static void setOpponentTurn(Turn turn) {
        if (state == State.FRIENDS_TURN) {
            logic.applyOpponentsTurn(turn.convertToTurn());
            ui.applyTurn(turn.x + 1, turn.y + 1, myType ? -1 : 1);
//            System.out.println(turn.toString());

            if (!checkForWins(turn)) {
                state = State.MY_TURN;
//                ui.switchTurn(true);
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }

    public static String getIPtoShow() {
        try {
            String s = new IPGetter().execute().get();
            System.err.println(s);
            return s;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "empty";
    }

    public static void optionConnectToFriend() {
        state = State.CONNECT_TO_FRIEND;

//        ui.getGameCode();           // could be possible to return to main menu
        setGameCode("");
    }

    public static void setGameCode(String gameCode) {
        client = new Client();
        try {
            client.start("Client", "192.168.1.49", "3030");
            boolean myTurn = Boolean.parseBoolean(client.getFrom());
//            ui.switchTurn(myTurn);
            friend = client.getPlayer("Server");
            if (!myTurn) {
                state = State.FRIENDS_TURN;
                setOpponentTurn(friend.getOpponentTurn());
            } else {
                state = State.MY_TURN;
                verifyTurn(1, 1);
            }
        } catch (IOException e) {
//            ui.networkError();
//            ui.getGameCode();
            e.printStackTrace();
        }
    }

    public static void optionInviteFriend() {
        state = State.SHARE_IP;
        server = new Server();
        try {
            server.start("Server", 3030);
            boolean myTurn = new Random().nextBoolean();
//            ui.switchTurn(myTurn);
            server.passTo(Boolean.toString(!myTurn));
            friend = server.getPlayer("Client");
            if (!myTurn) {
                state = State.FRIENDS_TURN;
                Turn opponentTurn = friend.getOpponentTurn();
                setOpponentTurn(opponentTurn);
            } else {
                state = State.MY_TURN;
                verifyTurn(3, 3);
            }
        } catch (IOException e) {
//            ui.networkError();
            e.printStackTrace();
        }
    }
//
//    public void optionStats() {
//        state = State.STATS;
//
//        ui.displayStats(stats.getRecords());
//    }

    public void fromGameToMainMenu() {
        paused = true;

//        ui.toMainMenu();
    }

    private void initField() {
//        Board board = logic.setUpField();
//        ui.setUpField(board);
    }

    private void newGameWarningIfPaused() {
        if (paused) {
//            ui.showWarning();
        }
    }

    private enum State {
        MAIN_MENU, // game with friend, game with bot, stats
        SHARE_IP,  // for connection to friend
        CONNECT_TO_FRIEND,
        CREATE_FIELD,
        MY_TURN,
        FRIENDS_TURN,
        END_OF_GAME,
        STATS
    }

}