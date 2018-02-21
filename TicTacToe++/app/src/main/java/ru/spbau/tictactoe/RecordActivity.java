package ru.spbau.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import ru.spbau.tictactoe.Statistic.DataBase;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        DataBase dataBase = new DataBase(this);
        DataBase.Entry[] records = dataBase.readRecords();
        ArrayList<String> st = new ArrayList<>();
        for (DataBase.Entry elem : records) {
            st.add(Integer.toString(elem.rowNumber) + ".  " + elem.result
                    + "  " + elem.opponent + "  " + Integer.toString(elem.moves) + "  " + elem.myType);
        }
//        ListView listView = (ListView) findViewById(R.id.listView);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                R.layout.list_item, st);
//        listView.setAdapter(adapter);
    }
}
