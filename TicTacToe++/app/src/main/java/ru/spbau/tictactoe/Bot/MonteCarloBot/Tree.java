package ru.spbau.tictactoe.Bot.MonteCarloBot;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import ru.spbau.tictactoe.Bot.MonteCarloBot.State;

/**
 * Basic implementation for Tree and Node classes for a tree search functionality.
 */
public class Tree {
    private Node root;

    public Tree() {
        root = new Node();
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    /**
     * Class which represents a tree node.
     */
    public static class Node implements Comparable<Node> {
        private State state;
        private Node parent;
        private List<Node> childNodes;

        public Node() {
            this.state = new State();
            childNodes = new ArrayList<>();
        }

        public Node(State state) {
            this.state = state;
            childNodes = new ArrayList<>();
        }

        /**
         * Copy constructor.
         *
         * @param node a node which copy is to be created
         */
        public Node(Node node) {
            this.childNodes = new ArrayList<>();
            this.state = new State(node.getState());
            if (node.getParent() != null) {
                this.parent = node.getParent();
            }
            for (Node child : node.getChildNodes()) {
                this.childNodes.add(new Node(child));
            }
        }

        public State getState() {
            return state;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public List<Node> getChildNodes() {
            return childNodes;
        }

        /**
         * @return random child of this node
         */
        public Node getRandomChildNode() {
            int noOfPossibleMoves = this.childNodes.size();
            Random rand = new Random(System.currentTimeMillis());
            int selectRandom = (int) (rand.nextDouble() * ((noOfPossibleMoves - 1) + 1));
            return this.childNodes.get(selectRandom);
        }

        /**
         * @return child node with max score
         */
        public Node getChildWithMaxScore() {
            return Collections.max(this.childNodes);
        }

        @Override
        public int compareTo(@NonNull Node node) {
            return ((Integer) getState().getVisitCount()).
                    compareTo(node.getState().getVisitCount());
        }
    }

}