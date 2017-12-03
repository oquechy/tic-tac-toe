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

    public void write() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlite:ttt-stats.db")) {
            try (Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("drop table if exists cities");
                stmt.executeUpdate("create table cities (id integer, name varchar(50))");
                stmt.executeUpdate("insert into cities values(1, 'St. Petersburg')");
                stmt.executeUpdate("insert into cities values(2, 'Moscow')");
                try (ResultSet rs = stmt.executeQuery("select * from cities")) {
                    System.out.println("Writing:");
                    while (rs.next()) {
                        System.out.println("name = " + rs.getString("name"));
                        System.out.println("id = " + rs.getInt("id"));
                    }
                }
            }
        }
    }

    public ResultSet read() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");

        try (Connection conn = DriverManager.getConnection(
                "jdbc:sqlite:ttt-stats.db")) {
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery( "SELECT * FROM cities" )
            ) {
                System.out.println("Reading:");
                while (rs.next()) {
                    System.out.println("name = " + rs.getString("name"));
                    System.out.println("id = " + rs.getInt("id"));
                }
                return rs;
            }
        }
    }

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