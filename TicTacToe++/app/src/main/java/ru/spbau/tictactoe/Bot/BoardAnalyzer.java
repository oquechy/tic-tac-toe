package ru.spbau.tictactoe.Bot;


import java.util.ArrayList;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class BoardAnalyzer {
    protected Board board;
    public BoardAnalyzer(Board board){
        this.board = new Board(board);
    }

    public void update(Board board) {
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
    }
}
