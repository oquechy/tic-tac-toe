package ru.spbau.tictactoe.Bot;


import java.util.ArrayList;
import java.util.Collections;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class BoardAnalyzer {
    protected Board board;
    protected static class TurnStatistics {
        protected int pos;
        protected boolean isWin;
        protected boolean blocksOpponentWin;
        protected boolean nextMoveToAnySquare;
    }
    public BoardAnalyzer(Board board){
        this.board = board;
    }

    public int analyzeBlockForWin(int block, Turn.Player player){
        Board.InnerBoard[] realBoard = board.getBoard();
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        for(int i : indexes){
            Board.InnerBoard square = realBoard[block];
            if(board.verifyTurn(new Turn(block, i)) &&
                    realBoard[i].getStatus() == Status.GAME_CONTINUES){
                square.setSquare(i, player);
                if(square.isOver() && square.getStatus() != Status.DRAW
                        && board.getBlockStatus(i) == Status.GAME_CONTINUES){
                    return i;
                }
                square.discardChanges(i);
            }
        }
        return -1;
    }

    /*public void update(Board board) {
        this.board = new Board(board);
    }

    protected ArrayList<Turn> winOnBoard;
    protected ArrayList<Turn> winOnInnerBoard;
    protected ArrayList<Turn> preventOpponentWinOnBoard;
    protected ArrayList<Turn> getPreventOpponentWinOnInnerBoard;
    protected ArrayList<Turn> possibleTurns;
    protected ArrayList<Turn> deprecatedTurns;

    public void analyzeBoard(){
        if(board.getCurrentInnerBoard() != -1){
            InnerBoardAnalyzer analyzer = new InnerBoardAnalyzer(
                    board.board[board.getCurrentInnerBoard()], board.currentPlayer);
            if(analyzer.canWin(board.currentPlayer)){
                for(int c : analyzer.turnsForWin){
                    if(board.board[c].getStatus() == Status.GAME_CONTINUES){

                    }
                }
            }
        }
    }

    public static class InnerBoardAnalyzer {
        ArrayList<Integer> turnsForWin;
        ArrayList<Integer> turnsForOpponentWin;
        ArrayList<Integer> possibleTurns;
        boolean isInvalid;

         InnerBoardAnalyzer(Board.InnerBoard innerBoardToAnalyze, Turn.Player player){
             if(innerBoardToAnalyze.getStatus() == Status.GAME_CONTINUES) {
                 for (int i = 0; i < 9; i++) {
                     if (innerBoardToAnalyze.innerBoard[i] == Status.GAME_CONTINUES) {
                         innerBoardToAnalyze.setSquare(i, player);
                         if (innerBoardToAnalyze.isOver() && innerBoardToAnalyze.getStatus() != Status.DRAW) {
                             turnsForWin.add(i);
                         } else {
                             possibleTurns.add(i);
                         }
                         innerBoardToAnalyze.discardChanges(i);
                         innerBoardToAnalyze.setSquare(i,  player.opponent());
                         if (innerBoardToAnalyze.isOver() && innerBoardToAnalyze.getStatus() != Status.DRAW) {
                             turnsForOpponentWin.add(i);
                         }
                         innerBoardToAnalyze.discardChanges(i);
                     }
                 }
             }
             else {
                 isInvalid = true;
             }
        }

        boolean canWin(Turn.Player player){
             return player == Turn.Player.CROSS ?
                     !turnsForWin.isEmpty() : !turnsForOpponentWin.isEmpty();
        }
    }*/

    /*public int dfs(int x, int y){
        if(board.status == Status.playerToStatus(board.currentPlayer)){
            return 1;
        }
        if(board.status == Status.playerToStatus(board.currentPlayer.opponent())){
            return -1;
        }
        if(board.status == Status.DRAW){
            return 0;
        }
        if(board.getCurrentInnerBoard() == -1){
            board.makeMoveToAnyOuterSquare(x, y);
            for(int i = 0; i < 9; i++){
                if(board.verifyTurn(new Turn(board.currentInnerBoard, i))){
                    int res = dfs(board.currentInnerBoard, i);
                    if(res == 0){
                        return 0;
                    }
                    if(res == -1){
                        return 1;
                    }
                    board.
                }
            }
        }
    }*/
}
