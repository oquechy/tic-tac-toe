package ru.spbau.tictactoe.Bot.MonteCarloBot;

import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.spbau.tictactoe.Bot.CleverBot.CleverBot;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * An implementation of Monte Carlo tree search algorithm.
 */
public class MonteCarloBot extends CleverBot {
    public MonteCarloBot(Board board) {
        super(board);
        level = 7;
    }

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MonteCarloBot.class);
    private static final int WIN_SCORE = 10;
    private int level;


    private int getMillisForCurrentLevel() {
        return 2 * (this.level - 1) + 1;
    }

    @Override
    public Turn makeTurn() {
        long start = System.currentTimeMillis();
        long end = start + 60 * getMillisForCurrentLevel();
        Tree tree = new Tree();
        Tree.Node rootNode = tree.getRoot();
        rootNode.getState().setBoard(board);
        int cnt = 0;
        while (System.currentTimeMillis() < end) {
            cnt++;
            // Selection
            Tree.Node promisingNode = selectPromisingNode(rootNode);
            // Expansion
            if (promisingNode.getState().getBoard().getStatus() == Status.GAME_CONTINUES) {
                expandNode(promisingNode);
            }
            // Simulation
            Tree.Node nodeToExplore = promisingNode;
            if (promisingNode.getChildNodes().size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            Status playoutResult = simulateRandomPlayout(nodeToExplore);
            // Update
            backPropogation(nodeToExplore, playoutResult);
        }
        logger.debug("{}", cnt);
        Tree.Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
        return winnerNode.getState().getLastTurn();
    }

    /**
     * Selects node with the best UCT (Upper Confidence Bound) function.
     *
     * @param rootNode is the node which child node is to be selected
     * @return node with the best UCT function
     */
    private Tree.Node selectPromisingNode(Tree.Node rootNode) {
        Tree.Node node = rootNode;
        while (node.getChildNodes().size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    /**
     * When it can no longer apply UCT to find the successor node,
     * it expands the game tree by appending all possible states from the leaf node.
     *
     * @param node is a node to be expanded.
     */
    private void expandNode(Tree.Node node) {
        List<State> possibleStates = node.getState().getAllPossibleStates();
        for (State state : possibleStates) {
            Tree.Node newNode = new Tree.Node(state);
            newNode.setParent(node);
            node.getChildNodes().add(newNode);
        }
    }

    /**
     * Traverses upwards to the root and increments visit score for all visited nodes.
     * It also updates win score for each node if the player for that position has won the playout.
     *
     * @param nodeToExplore a node from which the score is to be updated.
     * @param result        is a result of the simulation
     */
    private void backPropogation(Tree.Node nodeToExplore, Status result) {
        Tree.Node tempNode = nodeToExplore;
        while (tempNode != null) {
            tempNode.getState().incrementVisit();
            if (result == Status.playerToStatus(player)) {
                tempNode.getState().addScore(WIN_SCORE);
            }
            tempNode = tempNode.getParent();
        }
    }

    /**
     * Simulates random playout on board relevant to the given node.
     *
     * @param node is a node on which board is to simulated
     * @return a result of the game
     */
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
        return boardStatus;
    }

    public static void main(String[] args) {
        Board board = new Board();
        MonteCarloBot bot = new MonteCarloBot(board);
        bot.go();
    }

    @Override
    public String getName() {
        return "David";
    }

    /**
     * Class for computation UCT (Upper Confidence Bound applied to trees) formula.
     */
    private static class UCT {
        /**
         * Calculates the UCT formula for the given node.
         *
         * @param totalVisit   a number of total visits
         * @param nodeWinScore node's win score
         * @param nodeVisit    a number of this node's visits
         * @return a calculated value of UCT function
         */
        private static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
            if (nodeVisit == 0) {
                return Integer.MAX_VALUE;
            }
            return (nodeWinScore / (double) nodeVisit)
                    + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
        }

        /**
         * Finds the child node with the greatest UCT function.
         *
         * @param node is the node which child to be returned
         * @return a child node of the given node with the maximum score
         */
        private static Tree.Node findBestNodeWithUCT(Tree.Node node) {
            final int parentVisit = node.getState().getVisitCount();
            return Collections.max(
                    node.getChildNodes(),
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
