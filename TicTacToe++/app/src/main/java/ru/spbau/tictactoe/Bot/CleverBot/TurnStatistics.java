package ru.spbau.tictactoe.Bot.CleverBot;


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

    @Override
    public int compareTo(@NonNull TurnStatistics other) {
        if (this.isBusy) {
            return other.isBusy ? 0 : -1;
        }
        if (other.isBusy) {
            return 1;
        }
        if (this.nextMoveToAnySquare && !other.nextMoveToAnySquare) {
            return -1;
        }
        if (!this.nextMoveToAnySquare && other.nextMoveToAnySquare) {
            return 1;
        }
        if (this.sendToSquareWhereOpponentWins && !other.sendToSquareWhereOpponentWins) {
            return -1;
        }
        if (!this.sendToSquareWhereOpponentWins && other.sendToSquareWhereOpponentWins) {
            return 1;
        }
        if (this.isWin && !other.isWin) {
            return 1;
        }
        if (!this.isWin && other.isWin) {
            return -1;
        }
        if (this.blocksOpponentWin && !other.blocksOpponentWin) {
            return 1;
        }
        if (!this.blocksOpponentWin && other.blocksOpponentWin) {
            return -1;
        }
        return 0;
    }
}
