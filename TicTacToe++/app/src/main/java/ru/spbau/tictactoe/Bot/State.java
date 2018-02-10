package ru.spbau.tictactoe.Bot;

import java.util.ArrayList;
import java.util.List;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Turn.Turn;



    public class State {
        private Board board;
        private Turn lastTurn;
        private int visitCount;
        private double winScore;
        private Bot cross;
        private Bot nought;

        public State() {
            board = new Board();
            cross = new Bot(board);
            nought = new Bot(board);
            cross.player = Turn.Player.CROSS;
            nought.player = Turn.Player.NOUGHT;
        }

        public State(State state) {
            board = (state.getBoard().deepCopy());
            visitCount = state.getVisitCount();
            winScore = state.getWinScore();
            cross = new Bot(board);
            nought = new Bot(board);
            cross.player = Turn.Player.CROSS;
            nought.player = Turn.Player.NOUGHT;
            lastTurn = new Turn(state.lastTurn.getInnerBoard(), state.lastTurn.getInnerSquare());
        }

        public State(Board board) {
            this.board = board.deepCopy();
            cross = new Bot(board);
            nought = new Bot(board);
            cross.player = Turn.Player.CROSS;
            nought.player = Turn.Player.NOUGHT;
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
            return board.getCurrentPlayer();
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
            if(board.getCurrentPlayer() == Turn.Player.CROSS){
                board.makeMove(cross.makeTurn());
            }
            else{
                board.makeMove(nought.makeTurn());
            }
        }
    }

