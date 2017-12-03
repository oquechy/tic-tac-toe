package ru.spbau.tictactoe;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.SQLException;

import ru.spbau.tictactoe.Statistic.DataBase;
import ru.spbau.tictactoe.ui.UI;

public class EntryPoint extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_point);

        //create buttons
        Button singleButton = (Button) findViewById(R.id.singleButton);
        Button inviteFriendButton = (Button) findViewById(R.id.inviteFriendButton);
        Button joinAFriendButton = (Button) findViewById(R.id.joinAFriendButton);
        Button settingsButton = (Button) findViewById(R.id.settingsButton);
        Button recordsButton = (Button) findViewById(R.id.recordsButton);
        //create OnClickListener for all buttons
        View.OnClickListener oclSingleButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.optionGameWithBot();
                Intent intent = new Intent(EntryPoint.this, UI.class);
                startActivity(intent);
            }
        };

        View.OnClickListener oclInviteFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        Controller.getIPtoShow(EntryPoint.this),
                        Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),
                        WordCoder.encode(Controller.getIP(EntryPoint.this)),
                        Toast.LENGTH_LONG).show();
            }
        };
        View.OnClickListener oclJoinFriendButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),
                        "Input address of server",
                        Toast.LENGTH_LONG).show();
            }
        };
        View.OnClickListener oclSettingsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        };
        View.OnClickListener oclRecordsButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.initDB(EntryPoint.this);

                // создаем объект для данных
                ContentValues cv = new ContentValues();

                // получаем данные из полей ввода
                String opponent = "Lis";
                int moves = 3;
                String result = "Lis wins";

                // подключаемся к БД
                SQLiteDatabase db = Controller.dataBase.getWritableDatabase();


                //write
                cv.put("opponent", opponent);
                cv.put("result", result);
                cv.put("moves", moves);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("stats", null, cv);

                //read
                        Cursor c = db.query("stats", null, null, null, null, null, null);

                        // ставим позицию курсора на первую строку выборки
                        // если в выборке нет строк, вернется false
                        if (c.moveToFirst()) {

                            // определяем номера столбцов по имени в выборке
                            int idColIndex = c.getColumnIndex("id");
                            int opponentColIndex = c.getColumnIndex("opponent");
                            int resultColIndex = c.getColumnIndex("result");
                            int movesColIndex = c.getColumnIndex("moves");

                            do {
                                // получаем значения по номерам столбцов и пишем все в лог
                                System.out.println(
                                        "ID = " + c.getInt(idColIndex) +
                                                ", name = " + c.getString(opponentColIndex) +
                                                ", email = " + c.getString(movesColIndex));
                                // переход на следующую строку
                                // а если следующей нет (текущая - последняя), то false - выходим из цикла
                            } while (c.moveToNext());
                        } else
                            System.out.println("0 rows");
                        c.close();
                }

        };

        singleButton.setOnClickListener(oclSingleButton);
        inviteFriendButton.setOnClickListener(oclInviteFriendButton);
        joinAFriendButton.setOnClickListener(oclJoinFriendButton);
        settingsButton.setOnClickListener(oclSettingsButton);
        recordsButton.setOnClickListener(oclRecordsButton);
    }
}
