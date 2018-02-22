package ru.spbau.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import ru.spbau.tictactoe.Logic.Result.Result;
import ru.spbau.tictactoe.Statistic.DataBase;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        DataBase dataBase = new DataBase(this);
        DataBase.Entry[] records = dataBase.readRecords();
        ArrayList<String> st = new ArrayList<>();
        st.add("RESULT");
        st.add("Result Opponent Moves");
        for (DataBase.Entry elem : records) {
            st.add(Integer.toString(elem.rowNumber) + ".  " + ((elem.result == Result.CROSS) ? "WIN" : "LOSE")
                    + "  " + elem.opponent + "  " + Integer.toString(elem.moves));
        }
        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.list_item, st);
        listView.setAdapter(adapter);
    }
}
