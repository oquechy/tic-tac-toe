package ru.spbau.tictactoe.Logic.Board;

public class IncorrectMoveException extends RuntimeException {
    public IncorrectMoveException(){
        super();
    }

    public IncorrectMoveException(String message){
        super(message);
    }
}