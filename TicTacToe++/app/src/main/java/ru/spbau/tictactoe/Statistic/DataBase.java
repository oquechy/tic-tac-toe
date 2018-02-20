package ru.spbau.tictactoe.Statistic;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Consumer;

import ru.spbau.tictactoe.Controller;
import ru.spbau.tictactoe.Logic.Result.Result;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context) {
        super(context, "ttt-stats", null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table stats ("
                + "id integer primary key autoincrement,"
                + "opponent text,"
                + "result text,"
                + "moves integer,"
                + "my_type bit"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table stats ");

        db.execSQL("create table stats ("
                + "id integer primary key autoincrement,"
                + "opponent text,"
                + "result text,"
                + "moves integer,"
                + "my_type bit"
                + ");");
    }

    public static class Entry {
        public int rowNumber;
        public Result result;
        public String opponent;
        public int moves;
        public boolean myType;
        public String message;

        public Entry(String message) {
            this.message = message;
        }

        public Entry(int rowNumber, Result result, String opponent, int moves, boolean myType) {
            this.rowNumber = rowNumber;
            this.result = result;
            this.opponent = opponent;
            this.moves = moves;
            this.myType = myType;
        }
    }

    public Entry[] readRecords() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.query(
                "stats",
                null,
                null,
                null,
                null,
                null,
                "id desc"
        );

        if (c.moveToFirst()) {
            final Entry[] log = new Entry[c.getCount()];

            int idColIndex = c.getColumnIndex("id");
            int opponentColIndex = c.getColumnIndex("opponent");
            int resultColIndex = c.getColumnIndex("result");
            int movesColIndex = c.getColumnIndex("moves");
            int myTypeColIndex = c.getColumnIndex("my_type");

            int i = 0;
            do {
                System.out.println(
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(opponentColIndex) +
                                ", result = " + c.getString(resultColIndex) +
                                ", moves = " + c.getString(movesColIndex));
                log[i++] = new Entry(
                        c.getInt(idColIndex),
                        Result.valueOf(c.getString(resultColIndex)),
                        c.getString(opponentColIndex),
                        c.getInt(movesColIndex),
                        c.getInt(myTypeColIndex) != 0
                        );
            } while (c.moveToNext());
            c.close();
            return log;
        } else {
            c.close();
            return new Entry[]{
                    new Entry("Empty statistics... \nI strongly recommend you try this game!")
            };
        }
    }

    public void addRecord(Result resultState, String opponent, int moves, boolean myType) {
        ContentValues cv = new ContentValues();

        String result = resultState.toString();

        SQLiteDatabase db = getWritableDatabase();

        cv.put("opponent", opponent);
        cv.put("result", result);
        cv.put("moves", moves);
        cv.put("my_type", myType);
        db.insert("stats", null, cv);
    }
}

//