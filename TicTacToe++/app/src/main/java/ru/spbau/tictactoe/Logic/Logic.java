package ru.spbau.tictactoe.Logic;


import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.GameLog.GameLog;
import ru.spbau.tictactoe.Logic.Result.Result;
import ru.spbau.tictactoe.Logic.Turn.Turn;

public class Logic {
    private Board board = new Board();
    private GameLog gameLog = new GameLog();
    private Turn lastTurn;
    private Status isLittleWin;
    private int turnCounter;
    public Board setUpField(){
        return board;
    }

    public boolean verifyTurn(Turn turn){
        return board.verifyTurn(turn);
    }

    public void applyOpponentsTurn(Turn turn){
        lastTurn = turn;
        if(board.getCurrentInnerBoard() == -1){
//            try {
//                isLittleWin = board.makeMoveToAnyOuterSquare(
//                        turn.getInnerBoard(), turn.getInnerSquare());
//            }
//            catch(IncorrectMoveException e){
//
//            }
//        }
//        else{
//            try {
//                isLittleWin = board.makeMove(turn.getInnerSquare());
//            }
//            catch(IncorrectMoveException e){
//
//            }
        }
    }

    public void applyMyTurn(Turn turn){
        turnCounter++;
        applyOpponentsTurn(turn);
    }

    public int getTurnCounter(){
        return turnCounter;
    }

    public Status isLittleWin() {
        return isLittleWin;
    }

    public GameLog getGameLog(){
        return gameLog;
    }

    public Result getResult(){
        if(board.getGameStatus() == Status.CROSS){
            return Result.CROSS;
        }
        if(board.getGameStatus() == Status.NOUGHT){
            return Result.NOUGHT;
        }
        return Result.DRAW;
    }

    public int getLittleWinCoords() {
        return lastTurn.getInnerBoard();
    }

    public boolean isEndOfGame(){
        return board.getGameStatus() != Status.GAME_CONTINUES;
    }

    public Board getBoard(){
        return board;
    }

    public void reset(){
        board = new Board();
    }
    
    public Status getStatusOfInner(int block){
        return board.getBlockStatus(block);
    }

}
