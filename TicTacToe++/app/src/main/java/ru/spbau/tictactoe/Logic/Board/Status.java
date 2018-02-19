package ru.spbau.tictactoe.Logic.Board;


import java.io.Serializable;

import ru.spbau.tictactoe.Logic.Turn.Turn;

/**
 * Enum for current status on board or square (inner board is both board and square).
 * DRAW is used only for board.
 */
public enum Status implements Serializable {
    GAME_CONTINUES,
    CROSS,
    NOUGHT,
    DRAW;

    /**
     * Transforms player to Status. When a player wins on board, it's status becomes relevant to the player.
     *
     * @param player is a player to be transformed
     * @return Status.CROSS in case of Player.CROSS, Status.NOUGHT otherwise
     */
    public static Status playerToStatus(Turn.Player player) {
        return player == Turn.Player.CROSS ? CROSS : NOUGHT;
    }
}
