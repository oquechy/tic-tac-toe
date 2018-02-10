package ru.spbau.tictactoe.Bot;

import java.util.List;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class MonteCarloBot extends CleverBot {
    public MonteCarloBot(Board board){
        super(board);
        level = 3;
    }

    private static final int WIN_SCORE = 10;
    private int level;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }

    public Turn makeTurn() {
        long start = System.currentTimeMillis();
        long end = start + 60 * getMillisForCurrentLevel();

        Tree tree = new Tree();
        Tree.Node rootNode = tree.getRoot();
        rootNode.getState().setBoard(board);
        rootNode.getState().setPlayer(player);

        while (System.currentTimeMillis() < end) {
            // Phase 1 - Selection
            Tree.Node promisingNode = selectPromisingNode(rootNode);
            // Phase 2 - Expansion
            if (promisingNode.getState().getBoard().getStatus() == Status.GAME_CONTINUES)
                expandNode(promisingNode);

            // Phase 3 - Simulation
            Tree.Node nodeToExplore = promisingNode;
            if (promisingNode.getChildArray().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            Status playoutResult = simulateRandomPlayout(nodeToExplore);
            // Phase 4 - Update
            backPropogation(nodeToExplore, playoutResult);
        }

        Tree.Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
        return winnerNode.getState().getLastTurn();
    }

    private Tree.Node selectPromisingNode(Tree.Node rootNode) {
        Tree.Node node = rootNode;
        while (node.getChildArray().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    private void expandNode(Tree.Node node) {
        List<State> possibleStates = node.getState().getAllPossibleStates();
        for(State state : possibleStates){
            Tree.Node newNode = new Tree.Node(state);
            newNode.setParent(node);
            newNode.getState().setPlayer(node.getState().getOpponent());
            node.getChildArray().add(newNode);
        }
    }

    private void backPropogation(Tree.Node nodeToExplore, Status result) {
        Tree.Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (result == Status.playerToStatus(tempNode.getState().getPlayer())){
                tempNode.getState().addScore(WIN_SCORE);
            }
            tempNode = tempNode.getParent();
        }
    }

    private Status simulateRandomPlayout(Tree.Node node) {
        Tree.Node tempNode = new Tree.Node(node);
        State tempState = tempNode.getState();
        Status boardStatus = tempState.getBoard().getStatus();

        if (boardStatus == Status.playerToStatus(player.opponent())) {
            tempNode.getParent().getState().setWinScore(Integer.MIN_VALUE);
            return boardStatus;
        }
        while (boardStatus == Status.GAME_CONTINUES) {
            tempState.togglePlayer();
            tempState.randomPlay();
            boardStatus = tempState.getBoard().getStatus();
        }
        return boardStatus;
    }

}
