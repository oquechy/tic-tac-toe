package ru.spbau.tictactoe;

import android.app.Activity;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import ru.spbau.tictactoe.Logic.Logic;
import ru.spbau.tictactoe.Network.Client;
import ru.spbau.tictactoe.Network.IPGetter;
import ru.spbau.tictactoe.Network.Server;

public class Controller {

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

    private boolean paused = false;

    private static State state;
    private static Board ui;
    private static Server server;
    private static Client client;
    private static Logic logic = new Logic();
//    private Stats stats = new Stats();
    private static NetAnotherPlayer friend;

    public Controller(Activity mainMenu) {
        state = State.MAIN_MENU;

//        ui = new UI(mainMenu);
    }

    public void fromGameToMainMenu() {
        paused = true;
        state = State.MAIN_MENU;

//        ui.toMainMenu();
    }

    public void optionGameWithBot() {
        newGameWarningIfPaused();

        state = State.CREATE_FIELD;

        final Bot bot = new Bot(logic.getBoard());
        friend = new NetAnotherPlayer() {
        ru.spbau.tictactoe.Logic.Turn.Turn turn;

            @Override
            public void setOpponentTurn(Turn t) {
                turn = bot.makeTurn();
            }

            @Override
            public Turn getOpponentTurn() {
                return new Turn(turn);
            }

            @Override
            public boolean getFirstPlayer() {
                return false;
            }

            @Override
            public String getName() {
                return bot.getName();
            }
        };

        initField();                   // if previous game wasn't finished, you can clear board here
        boolean firstPlayer = true; //ui.chooseFirstPlayer();
//        ui.switchTurn(firstPlayer);    // true if it is my turn
//        logic.setFirstPlayer(firstPlayer);

        state = firstPlayer ? State.MY_TURN : State.FRIENDS_TURN;
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

    public static void verifyTurn(Turn newTurn) {
        if (state == State.MY_TURN && logic.verifyTurn(new ru.spbau.tictactoe.Logic.Turn.Turn(newTurn.x, newTurn.y))) {
//            ui.acceptTurn(newTurn);
            friend.setOpponentTurn(newTurn);
            logic.applyMyTurn(newTurn.convertToTurn());

            if (!checkForWins(newTurn)) {             // if not end of game
                state = State.FRIENDS_TURN;

//                ui.switchTurn(false);                 // friend's turn
                System.out.println(newTurn.toString());
                state = State.FRIENDS_TURN;
                setOpponentTurn(friend.getOpponentTurn());
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }


    private static boolean checkForWins(Turn newTurn) {  // turn: who and where
        if (logic.isLittleWin()) {         // applies new turn & checks for little win
//            ui.showLittleWin(logic.getLittleWinCoords());
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
            logic.applyOpponentsTurn(new ru.spbau.tictactoe.Logic.Turn.Turn(turn.x, turn.y));
            ui.applyOpponentTurn(turn.x, turn.y);
//            System.out.println(turn.toString());

            if (!checkForWins(turn)) {
                state = State.MY_TURN;
            state = State.MY_TURN;
            verifyTurn(server == null ? new Turn(true, 1, 1) : new Turn(false, 3, 3));
//                ui.switchTurn(true);
            }
        } else {
            System.err.println("incorrect turn time");
        }
    }
//
//    public void optionStats() {
//        state = State.STATS;
//
//        ui.displayStats(stats.getRecords());
//    }

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
                verifyTurn(new Turn(true, 1, 1));
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
                verifyTurn(new Turn(false, 3, 3));
            }
        } catch (IOException e) {
//            ui.networkError();
            e.printStackTrace();
        }
    }

}
