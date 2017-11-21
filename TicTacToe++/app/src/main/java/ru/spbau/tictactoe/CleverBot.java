package ru.spbau.tictactoe;

import java.util.HashSet;
import java.util.Random;

import ru.spbau.tictactoe.Bot;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.IncorrectMoveException;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class CleverBot extends Bot {
    public CleverBot(Board board){
        super(board);
    }

    public int analyzeBlock(int block){
        HashSet<Integer> possibleMoves = new HashSet<>();
        for(int i = 0; i < 9; i++){
            Board.InnerBoard[] realBoard = board.getBoard();
            Board.InnerBoard square = realBoard[block];
            try{
                square.setSquare(i, board.currentPlayer);
                possibleMoves.add(i);
                if(square.isOver()){
                    return i;
                }
            }
            catch(IncorrectMoveException e){

            }
        }
        Random rand = new Random();
        return rand.nextInt(possibleMoves.size());
    }
}
