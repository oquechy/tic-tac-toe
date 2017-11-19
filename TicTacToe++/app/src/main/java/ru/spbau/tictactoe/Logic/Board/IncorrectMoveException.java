package ru.spbau.tictactoe.Logic.Board;


public class IncorrectMoveException extends Exception {
    public IncorrectMoveException(){
        super();
    }

    public IncorrectMoveException(String message){
        super(message);
    }
}