package ru.spbau.tictactoe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ru.spbau.tictactoe.Statistic.DataBase;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        DataBase dataBase = new DataBase(this);
        DataBase.Entry[] records = dataBase.readRecords();
        TextView recordField = (TextView) findViewById(R.id.recordsTextView);
        recordField.setTextSize(20);
        StringBuilder st = new StringBuilder();
        for (DataBase.Entry elem : records) {
            st.append(Integer.toString(elem.rowNumber) + ' ' + elem.result
                    + ' ' + elem.opponent + ' ' + Integer.toString(elem.moves) + ' ' + elem.myType + '\n');
        }
        recordField.setText(st.toString());
    }
}
