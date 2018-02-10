package ru.spbau.tictactoe.Bot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Board.Status;
import ru.spbau.tictactoe.Logic.Turn.Turn;

import static ru.spbau.tictactoe.Bot.BoardAnalyzer.printBoard;


/**
 * Bot which looks over all possible moves and chooses the best according to my views.
 */
public class CleverBot extends Bot {
    private static final Logger logger = LoggerFactory.getLogger(CleverBot.class);

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
    protected TurnStatistics[] analyzeBlock(int block, Turn.Player player, Board copy) {
        logger.info("Analyzing block " +
                (Integer.valueOf(block)).toString() + ",player " + player.name());

        Board.InnerBoard[] realBoard = copy.getBoard();
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
            Board.InnerBoard square = realBoard[block];
            if (square.getSquare(i) == Status.GAME_CONTINUES) {
                square.setSquare(i, player);
                if (board.getBlockStatus(i) == Status.GAME_CONTINUES) {
                    if (square.isOver() && square.getStatus() != Status.DRAW) {
                        logger.info(player.name() + " can win " + Integer.valueOf(i).toString());
                        res[ind].isWin = true;
                        if (i == block) {
                            res[ind].nextMoveToAnySquare = true;
                        }
                    }
                } else {
                    res[ind].nextMoveToAnySquare = true;
                }
                square.discardChanges(i);
                if (analyzeBlockForLose(i, player, copy)) {
                    logger.info(player.opponent().name() + " can win " + Integer.valueOf(i).toString());
                    res[ind].sendToSquareWhereOpponentWins = true;
                }
                square.discardChanges(i);
                square.setSquare(i, player.opponent());
                if (square.isOver() && square.getStatus() != Status.DRAW) {
                    res[ind].blocksOpponentWin = true;
                }
                square.discardChanges(i);
            } else {
                logger.info(Integer.valueOf(block).toString()
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
    public boolean analyzeBlockForLose(int block, Turn.Player player, Board board) {
        logger.info("analyse block for lose " + Integer.valueOf(block).toString());
        Board.InnerBoard[] realBoard = board.getBoard();
        for (int i = 0; i < 9; i++) {
            Board.InnerBoard square = realBoard[block];
            if (square.getSquare(i) == Status.GAME_CONTINUES) {
                square.setSquare(i, player.opponent());
                if (board.getBlockStatus(i) == Status.GAME_CONTINUES) {
                    if (square.isOver() && square.getStatus() != Status.DRAW) {
                        square.discardChanges(i);
                        logger.info("can win on " +
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
        Board copy = board.deepCopy();
        ArrayList<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            indexes.add(i);
        }
        Collections.shuffle(indexes);
        if (cur == -1) {
            TurnStatistics[] bestInBlock = new TurnStatistics[9];
            for (int i : indexes) {
                if (board.getBlockStatus(i) == Status.GAME_CONTINUES) {
                    TurnStatistics[] statistics = analyzeBlock(i, board.getCurrentPlayer(), copy);
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
        TurnStatistics[] statistics = analyzeBlock(cur, board.getCurrentPlayer(), copy);
        for (int i = 0; i < 9; i++) {
            logger.info(Integer.valueOf(statistics[i].pos).toString() + (i == 8 ? "\n" : " "));
        }
        Arrays.sort(statistics);
        for (int i = 0; i < 9; i++) {
            logger.info(Integer.valueOf(statistics[i].pos).toString() + (i == 8 ? "\n" : " "));
        }
        return new Turn(cur, statistics[8].pos);
    }



    /**
     * Launches console version for debug.
     */
    protected void go() {
        Scanner reader = new Scanner(System.in);
        for(int i = 0; i < 9; i++){
            logger.debug(board.getBoard()[i].toString());
        }
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
        bot.go();
    }


}