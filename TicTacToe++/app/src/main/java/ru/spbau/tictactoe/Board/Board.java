package ru.spbau.tictactoe.Board;



import java.util.Arrays;

/**
 * Class which represents board where game goes.
 * The board consists of a grid of 9 blocks.
 * Each block contains 9 boxes arranged in 3 rows and 3 columns and
 * could be considered as a standard tic-tac-toe board.
 * At any moment there is a block where the next move will happen (initially it is the medium block).
 *
 */
public class Board {

    /**
     * An outer square or an inner board.
     */
    private static class InnerBoard {
        private int numberOfMarkedSquares;
        /**
         * Each box could be empty (GAME_CONTINUES), or marked as CROSS or NOUGHT (the BLOCK is not used here).
         */
        private Status[] innerBoard = new Status[9];
        /**
         * If the game in this block is not finished, the status is GAME_CONTINUES.
         * If one of the players won on this block the status is the player who won (CROSS or NOUGHT).
         * If nobody won but all boxes are filled, the status is BLOCK.
         */
        private Status status = Status.GAME_CONTINUES;

        /**
         * Initializes the values of squares in innerBoard with GAME_CONTINUES, as they should be empty in
         * the beginning of game.
         */
        private InnerBoard(){
            Arrays.fill(innerBoard, Status.GAME_CONTINUES);
        }

        /**
         * Changes a status of empty square to the player which makes a move,
         * then checks if the game on this block is over or the block is completely filled.
         * @param squareId is a number square from 0 to 8 to be changed
         * @param player is a player who makes move (CROSS or NOUGHT)
         * @throws IncorrectMoveException if the square is already marked
         */
        private void setSquare(int squareId, Status player) throws IncorrectMoveException {
            if(innerBoard[squareId] != Status.GAME_CONTINUES){
                throw new IncorrectMoveException("The square is busy");
            }
            innerBoard[squareId] = player;
            numberOfMarkedSquares++;
            if(isOver()){
                status = player;
            }
            if(numberOfMarkedSquares == 9){
                status = Status.BLOCK;
            }
        }

        /**
         * Checks if the game on this block is over.
         * Checks every row, column and diagonal if there are three squares
         * with the same status which is not GAME_CONTINUES.
         * @return true if the game on this block is over
         * (three squares in row, column or diagonal were marked by the same player)
         */
        private boolean isOver() {
            //check columns
            for(int i = 0; i < 3; i++){
                if(innerBoard[i] == innerBoard[i + 3]
                        && innerBoard[i] == innerBoard[i + 6]){
                    return true;
                }
            }
            //check rows
            for(int i = 0; i < 9; i += 3){
                if(innerBoard[i] != Status.GAME_CONTINUES && innerBoard[i] == innerBoard[i + 1]
                        && innerBoard[i] == innerBoard[i + 2]){
                    return true;
                }
            }
            //check diagonals
            return innerBoard[4] != Status.GAME_CONTINUES && (innerBoard[0] == innerBoard[4]
                    && innerBoard[4] == innerBoard[8] ||
                    innerBoard[2] == innerBoard[4] && innerBoard[4] == innerBoard[6]);
        }

        private Status getSquare(int squareId){
            return innerBoard[squareId];
        }
    }

    /**
     * Board consists of grid of nine inner boards.
     */
    private InnerBoard [] board = new InnerBoard[9];

    /**
     * The inner board where the next will be made.
     * Initially, it is the block in the center.
     * After each move the currentInnerBoard changes to the id
     * of the inner square that was changed during the move.
     * If the relevant block is busy (somebody won there or it is completely filled), it becomes -1,
     * which means that the next move can be made to any block.
     */
    private int currentInnerBoard = 4;

    /**
     * There are four opportunities:
     * the game continues - GAME_CONTINUES,
     * the first player won - CROSS,
     * the second player won - NOUGHT,
     * nobody won but the moves cannot be made - BLOCK.
     */
    private Status status = Status.GAME_CONTINUES;

    /**
     * The player who makes next move.
     */
    private Status currentPlayer = Status.CROSS;

    public Board(){
        for(int i = 0; i < 9; i++){
            board[i] = new InnerBoard();
        }
    }

    public int getCurrentInnerBoard(){
        return currentInnerBoard;
    }


    /**
     * Marks the specified square on current inner board with the sign of the specified player.
     * @param innerSquare is the id of the square to be marked
     * @throws IncorrectMoveException if the inner square is busy or current square must be specified.
     */
    public void makeMove(
            int innerSquare) throws IncorrectMoveException {
        if(currentInnerBoard == -1){
            throw new IncorrectMoveException(
                    "The inner board must be also specified");
        }
        board[currentInnerBoard].setSquare(innerSquare, currentPlayer);
        if(board[innerSquare].status == Status.GAME_CONTINUES){
            currentInnerBoard = innerSquare;
        }
        else{
            currentInnerBoard = -1;
        }
        if(isOver()){
            status = currentPlayer;
        }
        if(currentPlayer == Status.CROSS){
            currentPlayer = Status.NOUGHT;
        }
        else{
            currentPlayer = Status.CROSS;
        }
    }

    /**
     * Marks the specified square on specified inner board with the sign of the given player
     * if the current inner square is not specified yet.
     * @param block is the block id where a square will be marked
     * @param innerSquare is the id of the inner square to be marked
     * @throws IncorrectMoveException if the inner board could not be specified
     */
    public void makeMoveToAnyOuterSquare(
            int block, int innerSquare) throws IncorrectMoveException{

        if(currentInnerBoard != -1){
            throw new IncorrectMoveException(
                    "The inner board is already assigned");
        }
        board[block].setSquare(innerSquare, currentPlayer);
        if(board[innerSquare].status == Status.GAME_CONTINUES){
            currentInnerBoard = innerSquare;
        }
        else{
            currentInnerBoard = -1;
        }
        if(isOver()){
            status = currentPlayer;
        }
        if(currentPlayer == Status.CROSS){
            currentPlayer = Status.NOUGHT;
        }
        else{
            currentPlayer = Status.CROSS;
        }
    }

    public Status getSquare(int outerSquare, int innerSquare){
        return board[outerSquare].innerBoard[innerSquare];
    }

    public Status getBlockStatus(int outerSquare) {
        return board[outerSquare].status;
    }

    public Status getGameStatus(){
        return status;
    }

    /**
     * Checks if the game on board is over.
     * Checks every row, column and diagonal if there are three squares
     * with the same status which is not GAME_CONTINUES or BLOCK.
     * @return true if the game is over
     * (three squares in row, column or diagonal were marked by the same player)
     */
    public boolean isOver() {
        //check columns
        for(int i = 0; i < 3; i++){
            if(board[i].status != Status.GAME_CONTINUES
                    && board[i].status != Status.BLOCK
                    && board[i].status == board[i + 3].status
                    && board[i].status == board[i + 6].status){
                return true;
            }
        }
        //check rows
        for(int i = 0; i < 9; i += 3){
            if(board[i].status != Status.GAME_CONTINUES
                    && board[i].status != Status.BLOCK
                    && board[i].status == board[i + 1].status
                    && board[i].status == board[i + 2].status){
                return true;
            }
        }
        //check diagonals
        return board[4].status != Status.GAME_CONTINUES && board[4].status != Status.BLOCK
                && (board[0].status == board[4].status
                && board[4].status == board[8].status ||
                board[2].status == board[4].status && board[4].status == board[6].status);
    }
}

