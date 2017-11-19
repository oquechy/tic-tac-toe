package ru.spbau.tictactoe.AnotherPlayer;


import ru.spbau.tictactoe.Logic.Board.Board;
import ru.spbau.tictactoe.Logic.Turn.Turn;

public interface AnotherPlayer {
    Turn makeTurn(Board board);
    String getName();
}
