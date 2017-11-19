package ru.spbau.tictactoe.Logic;


import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.IncorrectMoveException;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.GameLog.GameLog;
import ru.spbau.tictactoe.Logic.Result.Result;
import ru.spbau.tictactoe.Logic.Turn.Turn;

public class Logic {
    private Board board = new Board();
    private GameLog gameLog = new GameLog();
    private Turn lastTurn;
    public Board setUpField(){
        return board;
    }

    public boolean verifyTurn(Turn turn){
        return board.verifyTurn(turn);
    }

    public boolean applyOpponentsTurn(Turn turn){
        lastTurn = turn;
        if(board.getCurrentInnerBoard() == -1){
            try {
                return board.makeMoveToAnyOuterSquare(
                        turn.getInnerBoard(), turn.getInnerSquare());
            }
            catch(IncorrectMoveException e){

            }
        }
        else{
            try {
                return board.makeMove(turn.getInnerSquare());
            }
            catch(IncorrectMoveException e){

            }
        }
        return false;
    }

    public GameLog getGameLog(){
        return gameLog;
    }

    public Result getResult(){
        if(board.getGameStatus() == Status.CROSS){
            return Result.CROSS;
        }
        if(board.getGameStatus() == Status.CROSS){
            return Result.NOUGHT;
        }
        return Result.DRAW;
    }

    int getLittleWinCoords() {
        return lastTurn.getInnerBoard();
    }

}
