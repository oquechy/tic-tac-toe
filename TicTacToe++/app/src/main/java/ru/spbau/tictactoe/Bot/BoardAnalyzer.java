package ru.spbau.tictactoe.Bot;

import java.util.ArrayList;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * Created by olga on 2/10/2018.
 */

public class BoardAnalyzer {
    public static ArrayList<Turn> getAvailableMoves(Board board){
        ArrayList<Turn> possibleMoves = new ArrayList<>();
        if (board.getCurrentInnerBoard() == -1) {
            for (int i = 0; i < 9; i++) {
                for(int j = 0; j < 9; j++){
                    Turn turn = new Turn(i, j);
                    if(board.verifyTurn(turn)){
                        possibleMoves.add(turn);
                    }
                }
            }
        }
        else{
            for(int j = 0; j < 9; j++) {
                Turn turn = new Turn(board.getCurrentInnerBoard(), j);
                if (board.verifyTurn(turn)) {
                    possibleMoves.add(turn);
                }
            }
        }
        return possibleMoves;
    }
}
