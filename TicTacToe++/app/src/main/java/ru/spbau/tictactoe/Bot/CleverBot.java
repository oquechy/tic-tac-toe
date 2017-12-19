package ru.spbau.tictactoe.Bot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import ru.spbau.tictactoe.Bot.Bot;
import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.IncorrectMoveException;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;


public class CleverBot extends Bot {
    public CleverBot(Board board){
        super(board);
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
                if(square.isOver() && square.getStatus() != Status.DRAW){
                    return i;
                }
                square.discardChanges(i);
            }
        }
        return -1;
    }

    public int avoidNextMoveToInvalidBlock(int block){
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        for(int i : indexes){
            if(board.verifyTurn(new Turn(block, i))
                    && board.getBlockStatus(i) == Status.GAME_CONTINUES){
                possibleMoves.add(i);
            }
        }
        if(possibleMoves.isEmpty()){
            return -1;
        }
        return possibleMoves.get(0);
    }


    @Override
    public Turn makeTurn(){
        int cur = board.getCurrentInnerBoard();
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        if(cur == -1){
            for(int i : indexes){
                if(board.getBlockStatus(i) == Status.GAME_CONTINUES){
                    int j = analyzeBlockForWin(i, board.currentPlayer);
                    if(j != -1){
                        return new Turn(i, j);
                    }
                }
            }
            for(int i : indexes){
                if(board.getBlockStatus(i) == Status.GAME_CONTINUES){
                    int j = analyzeBlockForWin(i, board.currentPlayer);
                    if(j != -1){
                        return new Turn(i, j);
                    }
                }
            }
            return super.makeTurn();
        }
        int x = analyzeBlockForWin(cur, board.currentPlayer);
        if(x != -1){
            return new Turn(cur, x);
        }
        return super.makeTurn();
    }
}
