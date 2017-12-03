package ru.spbau.tictactoe.Statistic;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase extends SQLiteOpenHelper {

    public DataBase(Context context) {
        super(context, "ttt-stats", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table stats ("
                + "id integer primary key autoincrement,"
                + "opponent text,"
                + "result text,"
                + "moves integer"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table mytable ");

        db.execSQL("create table stats ("
                + "id integer primary key autoincrement,"
                + "opponent text,"
                + "result text,"
                + "moves integer"
                + ");");
    }
}