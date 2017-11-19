package ru.spbau.tictactoe.Logic.Board;


import java.util.Scanner;

import ru.spbau.tictactoe.NetAnotherPlayer.Bot.Bot;

public class Main {
    private static char toChar(Status t){
        if(t == Status.CROSS){
            return 'x';
        }
        if(t == Status.NOUGHT){
            return '0';
        }
        return '-';
    }

    private static void printBoard(Board board){
        Board.InnerBoard[] innerBoards = board.getBoard();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
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

    public static void go() {
        Board board = new Board();
        Bot bot = new Bot();
        Scanner reader = new Scanner(System.in);
        while(!board.isOver()){
            System.out.printf("Current board is %d\n", board.getCurrentInnerBoard());
            printBoard(board);
            if(board.getCurrentInnerBoard() == -1){
                int x = reader.nextInt();
                int y = reader.nextInt();
                try {
                    board.makeMoveToAnyOuterSquare(x, y);
                }
                catch(IncorrectMoveException e){
                    System.out.println("Two-argument incorrect move!");
                }
            }
            else{

                try {
                    int x = 0;
                    board.makeMove(x = reader.nextInt());
                    System.out.println(board.getBlockStatus(x));
                }
                catch(IncorrectMoveException e){
                    System.out.println("One-argument incorrect move!");
                }
            }
        }
    }
    public static void main(String [] args) {
        go();
    }
}
