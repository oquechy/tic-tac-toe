package ru.spbau.tictactoe.Bot;

import java.util.ArrayList;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
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

    /**
     * Prints board in console. Used for debug.
     *
     * @param board is a board to be printed
     */
    protected static void printBoard(Board board) {
        Board.InnerBoard[] innerBoards = (Board.InnerBoard[])board.getBoard();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(toChar(innerBoards[3 * i].getSquare(3 * j)));
                System.out.print(toChar(innerBoards[3 * i].getSquare(3 * j + 1)));
                System.out.print(toChar(innerBoards[3 * i].getSquare(3 * j + 2)));
                System.out.print(' ');
                System.out.print(toChar(innerBoards[3 * i + 1].getSquare(3 * j)));
                System.out.print(toChar(innerBoards[3 * i + 1].getSquare(3 * j + 1)));
                System.out.print(toChar(innerBoards[3 * i + 1].getSquare(3 * j + 2)));
                System.out.print(' ');
                System.out.print(toChar(innerBoards[3 * i + 2].getSquare(3 * j)));
                System.out.print(toChar(innerBoards[3 * i + 2].getSquare(3 * j + 1)));
                System.out.print(toChar(innerBoards[3 * i + 2].getSquare(3 * j + 2)));
                System.out.print('\n');
            }
            System.out.print('\n');
        }
    }

    /**
     * Transforms square status to char. Used for console output in debug.
     *
     * @param t is a Status to be transformed
     * @return char relevant to the given status
     */
    private static char toChar(Status t) {
        if (t == Status.CROSS) {
            return 'x';
        }
        if (t == Status.NOUGHT) {
            return '0';
        }
        return '-';
    }

}
