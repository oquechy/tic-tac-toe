package ru.spbau.tictactoe.Board;


public class IncorrectMoveException extends Exception {
    public IncorrectMoveException(){
        super();
    }

    public IncorrectMoveException(String message){
        super(message);
    }
}