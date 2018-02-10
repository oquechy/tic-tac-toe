package ru.spbau.tictactoe.Bot;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * Created by olga on 2/10/2018.
 */


    public class State {
        private Board board;
        private Turn lastTurn;
        private Turn.Player player;
        private int visitCount;
        private double winScore;
        private Bot bot;

        public State() {
            board = new Board();
            bot = new CleverBot(board);
        }

        public State(State state) {
            this.board = (state.getBoard().deepCopy());
            this.player = state.getPlayer();
            this.visitCount = state.getVisitCount();
            this.winScore = state.getWinScore();
            bot = new CleverBot(board);
        }

        public State(Board board) {
            this.board = board.deepCopy();
            bot = new CleverBot(board);
        }

        public Turn getLastTurn(){
            return lastTurn;
        }

        Board getBoard() {
            return board;
        }

        void setBoard(Board board) {
            this.board = board;
        }

        Turn.Player getPlayer() {
            return player;
        }

        void setPlayer(Turn.Player player) {
            this.player = player;
        }

        Turn.Player getOpponent() {
            return player.opponent();
        }

        public int getVisitCount() {
            return visitCount;
        }

        public void setVisitCount(int visitCount) {
            this.visitCount = visitCount;
        }

        double getWinScore() {
            return winScore;
        }

        void setWinScore(double winScore) {
            this.winScore = winScore;
        }

        public List<State> getAllPossibleStates() {
            List<State> possibleStates = new ArrayList<>();
            List<Turn> availablePositions = BoardAnalyzer.getAvailableMoves(board);
            for(Turn turn : availablePositions) {
                State newState = new State(this.board);
                newState.setPlayer(player.opponent());
                newState.getBoard().makeMove(turn);
                newState.lastTurn = turn;
                possibleStates.add(newState);
            }
            return possibleStates;
        }

        void incrementVisit() {
            this.visitCount++;
        }

        void addScore(double score) {
            if (this.winScore != Integer.MIN_VALUE)
                this.winScore += score;
        }

        void randomPlay() {
            board.makeMove(bot.makeTurn());
        }

        void togglePlayer() {
            this.player = player.opponent();
        }
    }

