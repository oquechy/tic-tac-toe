package ru.spbau.tictactoe.Logic.Board;


import ru.spbau.tictactoe.Logic.Turn.Turn;

public enum Status {
    GAME_CONTINUES,
    CROSS,
    NOUGHT,
    DRAW;
    public static Status playerToStatus(Turn.Player player){
        return player == Turn.Player.CROSS ? CROSS : NOUGHT;
    }
}
