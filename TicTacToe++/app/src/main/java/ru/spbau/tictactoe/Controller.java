//package ru.spbau.tictactoe;
//
//import android.app.Activity;
//
//import java.io.IOException;
//import java.util.concurrent.ExecutionException;
//
//import ru.spbau.tictactoe.Network.Client;
//import ru.spbau.tictactoe.Network.IPGetter;
//import ru.spbau.tictactoe.Network.Server;
//
//public class Controller {
//
//    private enum State {
//        MAIN_MENU, // game with friend, game with bot, stats
//        SHARE_IP,  // for connection to friend
//        CONNECT_TO_FRIEND,
//        CREATE_FIELD,
//        MY_TURN,
//        FRIENDS_TURN,
//        END_OF_GAME,
//        STATS
//    }
//
//    private boolean paused = false;
//
//    private State state;
//    private UI ui;
//    private Server server;
//    private Client client;
//    private Logic logic = new Logic();
//    private Stats stats = new Stats();
//    private AnotherPlayer friend;
//
//    public Controller(Activity mainMenu) {
//        state = State.MAIN_MENU;
//
//        ui = new UI(mainMenu);
//    }
//
//    public void fromGameToMainMenu() {
//        paused = true;
//        state = State.MAIN_MENU;
//
//        ui.toMainMenu();
//    }
//
//    public void optionGameWithBot() {
//        newGameWarningIfPaused();
//
//        state = State.CREATE_FIELD;
//
//        friend = new Bot();
//        initField();                   // if previous game wasn't finished, you can clear board here
//        boolean firstPlayer = ui.chooseFirstPlayer();
//        ui.switchTurn(firstPlayer);    // true if it is my turn
//        logic.setFirstPlayer(firstPlayer);
//
//        state = firstPlayer ? State.MY_TURN : State.FRIENDS_TURN;
//    }
//
//    private void initField() {
//        Board board = logic.setUpField();
//        ui.setUpField(board);
//    }
//
//    private void newGameWarningIfPaused() {
//        if (paused) {
//            ui.showWarning();
//        }
//    }
//
//    public void verifyTurn(Turn newTurn) {
//        if (state == State.MY_TURN && logic.verifyTurn(newTurn)) {
//            ui.acceptTurn(newTurn);
//            friend.setOpponenTurn(newTurn);
//
//            if (!checkForWins(newTurn)) {             // if not end of game
//                state = State.FRIENDS_TURN;
//
//                ui.switchTurn(false);                 // friend's turn
//            }
//        } else {
//            System.err.println("incorrect turn time");
//        }
//    }
//
//
//    private boolean checkForWins(Turn newTurn) {  // turn: who and where
//        if (logic.isLittleWin(newTurn)) {         // applies new turn & checks for little win
//            ui.showLittleWin(logic.getLittleWinCoords());
//        }
//
//        if (logic.isEndOfGame()) {                // first player wins/second player wins/draw
//            state = State.END_OF_GAME;
//
//            GameLog gameLog = logic.getGameLog(); // am I a winner? how many turns?
//            gameLog.writeFriendsName(friend.getName());
//            stats.getNewRecord(gameLog);
//            ui.displayResult(logic.getResult());  // and go to main menu then
//
//            return true;
//        }
//        return false;
//    }
//
//    public void setOpponentTurn(Turn turn) {
//        if (state == State.FRIENDS_TURN) {
//            logic.applyOpponentTurn(turn);
//            ui.applyOpponentTurn(turn);
//
//            if (!checkForWins(turn)) {
//                state = State.MY_TURN;
//
//                ui.switchTurn(true);
//            }
//        } else {
//            System.err.println("incorrect turn time");
//        }
//    }
//
//    public void optionStats() {
//        state = State.STATS;
//
//        ui.displayStats(stats.getRecords());
//    }
//
//    public void optionConnectToFriend() {
//        state = State.CONNECT_TO_FRIEND;
//
//        ui.getGameCode();           // could be possible to return to main menu
//    }
//
//    public void setGameCode(String gameCode) {
//        client = new Client();
//        try {
//            client.start(Coder.parse(gameCode));
//        } catch (IOException e) {
//            ui.networkError();
//            ui.getGameCode();
//            e.printStackTrace();
//        }
//    }
//
//    public void optionInviteFriend() {
//     state = State.SHARE_IP;
//
//        try {
//            String ip = new IPGetter().execute().get();
//            if (ip == null) {
//                ui.checkConnectionWarning();
//            } else {
//                ui.showCode(Coder.encode(ip));
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        server = new Server();
//        try {
//            server.start("3030");
//        } catch (IOException e) {
//            ui.
//            e.printStackTrace();
//        }
//    }
//
//}
