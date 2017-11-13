package ru.spbau.tictactoe;

import android.app.Activity;

public class Controller {

    private enum State {
        MAIN_MENU, // game with friend, game with bot, stats
        SHARE_IP,  // for connection to friend
        CONNECT_FRIEND,
        CREATE_FIELD,
        MY_TURN,
        FRIENDS_TURN,
        END_OF_GAME,
        STATS,
        EXIT
    }

    private State state = State.MAIN_MENU;
    private UI ui = new UI();
    private Server server = new Server();
    private Logic logic = new Logic();
    private Stats stats = new Stats();
    private AnotherPlayer friend = new AnotherPlayer();

    Controller(Activity mainMenu) {
        ui.setUpMenu(mainMenu);
    }

    public void start() {
        boolean run = true;

        while (run) {
            switch (state) {
                case MAIN_MENU:
                    state = ui.getMainMenuOption();
                    break;
                case CONNECT_FRIEND:
                    IP ip = ui.getFriendsIP();
                    if (ip == null) {
                        state = State.MAIN_MENU;
                    } else if (Server.connectTo(ip)) {
                        state = State.CREATE_FIELD;
                    } else {
                        state = State.MAIN_MENU;    // TODO: show error
                    }
                    break;
                case SHARE_IP:
                    IP myIp = Server.getMyIP();
                    ui.showIP(myIp);
                    if (server.waitForConnection()) {
                        state = State.CREATE_FIELD;
                    } else {
                        state = State.MAIN_MENU;   // TODO: show error
                    }
                    break;
                case CREATE_FIELD:
                    Board board = logic.setUpField();
                    ui.setUpGame(board);
                    break;
                case MY_TURN:
                case FRIENDS_TURN:
                    Turn turn = (state == State.MY_TURN ? ui.getNewTurn() : friend.getNewTurn());

                    BoardChanges changes = logic.applyFriendsTurn(turn);

                    if (logic.isEndOfGame()) {
                        state = State.END_OF_GAME;
                    }
                    else {
                        ui.applyChanges(changes);
                        state = State.FRIENDS_TURN;
                    }

                    break;
                case END_OF_GAME:
                    GameLog gameLog = logic.getGameLog();
                    stats.getNewRecord(gameLog);
                    ui.displayResult(logic.getResult());

                    state = State.MAIN_MENU;
                    break;
                case STATS:
                    break;
                case EXIT:
                    run = false;
            }
        }
    }
}
