package ru.spbau.tictactoe.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * Bot which looks over all possible moves and chooses the best according to my views.
 */
public class CleverBot extends Bot {
    Logger logger = new Logger();

    public CleverBot(Board board) {
        super(board);
    }

    /**
     * Returns array of TurnStatistics for every square in block.
     *
     * @param block  is an inner board to be analysed
     * @param player is a player who makes a turn
     * @return TurnStatistics for every square on board
     */
    protected TurnStatistics[] analyzeBlock(int block, Turn.Player player) {
        logger.printLog("Analyzing block " +
                (Integer.valueOf(block)).toString() + ",player " + player.name());
        Board.InnerBoard[] realBoard = board.getBoard();
        TurnStatistics[] res = new TurnStatistics[9];
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        for (int ind = 0; ind < 9; ind++) {
            res[ind] = new TurnStatistics();
            int i = indexes.get(ind);
            res[ind].pos = i;
            res[ind].block = block;
            Board.InnerBoard square = null;
            try {
                square = (Board.InnerBoard) realBoard[block].clone();
            } catch (CloneNotSupportedException e) {
                //TODO
            }
            if (square.getSquare(i) == Status.GAME_CONTINUES) {
                square.setSquare(i, player);
                if (board.getBlockStatus(i) == Status.GAME_CONTINUES) {
                    if (square.isOver() && square.getStatus() != Status.DRAW) {
                        logger.printLog(player.name() + " can win " + Integer.valueOf(i).toString());
                        res[ind].isWin = true;
                        if (i == block) {
                            res[ind].nextMoveToAnySquare = true;
                        }
                    }
                } else {
                    res[ind].nextMoveToAnySquare = true;
                }
                square.discardChanges(i);
                if (analyzeBlockForLose(i, player)) {
                    logger.printLog(player.opponent().name() + " can win " + Integer.valueOf(i).toString());
                    res[ind].sendToSquareWhereOpponentWins = true;
                }
                square.discardChanges(i);
                square.setSquare(i, player.opponent());
                if (square.isOver() && square.getStatus() != Status.DRAW) {
                    res[ind].blocksOpponentWin = true;
                }
                square.discardChanges(i);
            } else {
                logger.printLog(Integer.valueOf(block).toString()
                        + ", " + Integer.valueOf(i).toString() + " is busy");
                res[ind].isBusy = true;
            }
        }
        return res;
    }

    /**
     * Analyses if the opponent can win on this board.
     *
     * @param block  is a block to be analysed
     * @param player is a player who makes a turn
     * @return true if player's opponent can win on board, false otherwise
     */
    public boolean analyzeBlockForLose(int block, Turn.Player player) {
        logger.printLog("analyse block for lose " + Integer.valueOf(block).toString());
        Board.InnerBoard[] realBoard = board.getBoard();
        for (int i = 0; i < 9; i++) {
            Board.InnerBoard square = null;
            try {
                square = (Board.InnerBoard) realBoard[block].clone();
            } catch (CloneNotSupportedException e) {
                //TODO
            }
            if (square.getSquare(i) == Status.GAME_CONTINUES) {
                square.setSquare(i, player.opponent());
                if (board.getBlockStatus(i) == Status.GAME_CONTINUES) {
                    if (square.isOver() && square.getStatus() != Status.DRAW) {
                        square.discardChanges(i);
                        logger.printLog("can win on " +
                                Integer.valueOf(block).toString() +
                                " square " + Integer.valueOf(i).toString());
                        return true;
                    }
                }
                square.discardChanges(i);
            }
        }
        return false;
    }

    /**
     * Analyses board and returns the best move.
     *
     * @return the best turn
     */
    @Override
    public Turn makeTurn() {
        int cur = board.getCurrentInnerBoard();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        if (cur == -1) {
            TurnStatistics[] bestInBlock = new TurnStatistics[9];
            for (int i : indexes) {
                if (board.getBlockStatus(i) == Status.GAME_CONTINUES) {
                    TurnStatistics[] statistics = analyzeBlock(i, board.getCurrentPlayer());
                    Arrays.sort(statistics);
                    bestInBlock[i] = statistics[8];
                } else {
                    bestInBlock[i] = new TurnStatistics();
                    bestInBlock[i].isBusy = true;
                    bestInBlock[i].block = i;
                }
            }
            Arrays.sort(bestInBlock);
            return new Turn(bestInBlock[8].block, bestInBlock[8].pos);
        }
        TurnStatistics[] statistics = analyzeBlock(cur, board.getCurrentPlayer());
        for (int i = 0; i < 9; i++) {
            logger.printLog(Integer.valueOf(statistics[i].pos).toString() + (i == 8 ? "\n" : " "));
        }
        Arrays.sort(statistics);
        for (int i = 0; i < 9; i++) {
            logger.printLog(Integer.valueOf(statistics[i].pos).toString() + (i == 8 ? "\n" : " "));
        }
        return new Turn(cur, statistics[8].pos);
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

    /**
     * Prints board in console. Used for debug.
     *
     * @param board is a board to be printed
     */
    private static void printBoard(Board board) {
        Board.InnerBoard[] innerBoards = board.getBoard();
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
     * Launches console version for debug.
     */
    private void go() {
        Bot bot = new Bot(board);
        Scanner reader = new Scanner(System.in);
        while (!board.isOver()) {
            System.out.printf("Current board is %d\n", board.getCurrentInnerBoard());
            printBoard(board);
            if (board.getCurrentInnerBoard() == -1) {
                int x = reader.nextInt();
                int y = reader.nextInt();
                board.makeMoveToAnyOuterSquare(x, y);
            } else {
                int x = 0;
                board.makeMove(x = reader.nextInt());
                System.out.println(board.getBlockStatus(x));
            }
            Turn turn = makeTurn();
            if (board.getCurrentInnerBoard() == -1) {
                board.makeMoveToAnyOuterSquare(turn.getInnerBoard(), turn.getInnerSquare());
            } else {
                board.makeMove(turn.getInnerSquare());
            }
        }
    }

    public static void main(String[] args) {
        Board board = new Board();
        CleverBot bot = new CleverBot(board);
        bot.logger.debug = true;
        bot.go();
    }


}
