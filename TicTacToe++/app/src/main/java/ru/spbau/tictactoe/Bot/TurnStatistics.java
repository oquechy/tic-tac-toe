package ru.spbau.tictactoe.Bot;


import android.support.annotation.NonNull;

/**
 * Additional class used for CleverBot.
 * Nested class TurnStatistics is main utility of this class.
 */
public class TurnStatistics implements Comparable<TurnStatistics> {

    /**
     * Square id in block.
     */
    int pos;

    /**
     * Block id.
     */
    int block;

    /**
     * Square is not empty.
     */
    boolean isBusy;

    /**
     * After marking this square player will win on inner board.
     */
    boolean isWin;

    /**
     * If this square will be marked by player's opponent, he will win on inner board.
     */
    boolean blocksOpponentWin;

    /**
     * If this square will be marked by player, next opponent's move will be on any square.
     */
    boolean nextMoveToAnySquare;

    /**
     * If this square will be marked by player, opponent can win on inner board in his next move.
     */
    boolean sendToSquareWhereOpponentWins;

    /**
     * Logger used for debug.
     */
    Logger logger = new Logger();

    @Override
    public int compareTo(@NonNull TurnStatistics other) {
        if (this.isBusy) {
            return other.isBusy ? 0 : -1;
        }
        if (other.isBusy) {
            return 1;
        }
        if (this.nextMoveToAnySquare && !other.nextMoveToAnySquare) {
            logger.printfLog("%d %d next move to any square\n", this.block, this.pos);
            return -1;
        }
        if (!this.nextMoveToAnySquare && other.nextMoveToAnySquare) {
            logger.printfLog("%d %d next move to any square\n", other.block, other.pos);
            return 1;
        }
        if (this.sendToSquareWhereOpponentWins && !other.sendToSquareWhereOpponentWins) {
            logger.printfLog("%d %d send to square where opponent wins\n", this.block, this.pos);
            return -1;
        }
        if (!this.sendToSquareWhereOpponentWins && other.sendToSquareWhereOpponentWins) {
            logger.printfLog("%d %d send to square where opponent wins\n", other.block, other.pos);
            return 1;
        }
        if (this.isWin && !other.isWin) {
            logger.printfLog("%d %d wins\n", this.block, this.pos);
            return 1;
        }
        if (!this.isWin && other.isWin) {
            logger.printfLog("%d %d wins\n", other.block, other.pos);
            return -1;
        }
        if (this.blocksOpponentWin && !other.blocksOpponentWin) {
            logger.printfLog("%d %d blocks opponent's win\n", this.block, this.pos);
            return 1;
        }
        if (!this.blocksOpponentWin && other.blocksOpponentWin) {
            logger.printfLog("%d %d blocks opponent's win\n", other.block, other.pos);
            return -1;
        }
        return 0;
    }
}
