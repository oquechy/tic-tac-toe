package ru.spbau.tictactoe.Bot;

import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class MonteCarloBot extends CleverBot {
    public MonteCarloBot(Board board){
        super(board);
        level = 3;
        tree = new Tree();
    }

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MonteCarloBot.class);
    private static final int WIN_SCORE = 10;
    private int level;
    private Tree tree;


    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }

    public Turn makeTurn() {
        long start = System.currentTimeMillis();
        long end = start + 60 * getMillisForCurrentLevel();
        Tree tree = new Tree();
        Tree.Node rootNode = tree.getRoot();
        rootNode.getState().setBoard(board);
        int cnt = 0;
        while (System.currentTimeMillis() < end) {
            logger.debug("{}", cnt);
            cnt++;
            // Phase 1 - Selection
            logger.debug("select promising node");
            Tree.Node promisingNode = selectPromisingNode(rootNode);
            // Phase 2 - Expansion
            logger.debug("expand");
            if (promisingNode.getState().getBoard().getStatus() == Status.GAME_CONTINUES)
                expandNode(promisingNode);

            // Phase 3 - Simulation
            logger.debug("playout");
            Tree.Node nodeToExplore = promisingNode;
            if (promisingNode.getChildArray().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            Status playoutResult = simulateRandomPlayout(nodeToExplore);
            // Phase 4 - Update
            backPropogation(nodeToExplore, playoutResult);
        }
        logger.debug("final");
        //logger.debug("{}", cnt);
        Tree.Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
        return winnerNode.getState().getLastTurn();
    }

    private Tree.Node selectPromisingNode(Tree.Node rootNode) {
        Tree.Node node = rootNode;
        while (node.getChildArray().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        if(node.getState().getLastTurn() != null)
        logger.debug("{} {}", node.getState().getLastTurn().getInnerBoard(), node.getState().getLastTurn().getInnerSquare());
        return node;
    }

    private void expandNode(Tree.Node node) {
        List<State> possibleStates = node.getState().getAllPossibleStates();
        for(State state : possibleStates){
            Tree.Node newNode = new Tree.Node(state);
            newNode.setParent(node);
            node.getChildArray().add(newNode);
        }
    }

    private void backPropogation(Tree.Node nodeToExplore, Status result) {
        Tree.Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (result == Status.playerToStatus(player)){
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
            tempState.randomPlay();
            boardStatus = tempState.getBoard().getStatus();
        }
        BoardAnalyzer.printBoard(tempNode.getState().getBoard());
        logger.debug(boardStatus.name());
        return boardStatus;
    }

    public static void main(String[] args) {
        Board board = new Board();
        MonteCarloBot bot = new MonteCarloBot(board);
        bot.go();
    }

    private static class UCT {
         static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
            if (nodeVisit == 0) {
                return Integer.MAX_VALUE;
            }
            return (nodeWinScore / (double) nodeVisit)
                    + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
        }

        static Tree.Node findBestNodeWithUCT(Tree.Node node) {
            final int parentVisit = node.getState().getVisitCount();
            return Collections.max(
                    node.getChildArray(),
                    new Comparator<Tree.Node>() {
                        @Override
                        public int compare(Tree.Node node, Tree.Node t1) {
                            return ((Double) uctValue(parentVisit, node.getState().getWinScore(),
                                    node.getState().getVisitCount())).
                                    compareTo(uctValue(parentVisit,
                                            t1.getState().getWinScore(), t1.getState().getVisitCount()));
                        }
                    });
        }
    }

}
