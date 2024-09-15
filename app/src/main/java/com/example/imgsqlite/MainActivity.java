package com.example.imgsqlite;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase DB;
    ListView lvTeam;
    Button btnAdd;
    ArrayList<teamCls> teamList;
    adapterTeamCls adapterTeam;
    Cursor cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        MakeOrOpenDB();
        Init();
        Act();
        ShowData();
    }

    private void Act() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUIDActivity();
            }
        });
    }

    private void openUIDActivity() {
        Intent intent = new Intent(this, UIDActivity.class);
        intent.putExtra("ACTION","ADD");
        startActivity(intent);
    }

    private void Init() {
        btnAdd = findViewById(R.id.btnAdd);
        lvTeam = findViewById(R.id.lvTeam);
    }

    private void MakeOrOpenDB(){
        DB = openOrCreateDatabase("fcdb.sqlite",MODE_PRIVATE, null);
        String sql = "CREATE TABLE IF NOT EXISTS fcteam" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "NAME VARCHAR(100)," +
                "LOGO BLOB," +
                "STADIUM VARCHAR(30)," +
                "CAPACITY VARCHAR(30)" +
                ")";
        DB.execSQL(sql);
    }

    private void ShowData(){
        teamList = new ArrayList<>();
        cursor = DB.rawQuery("SELECT * FROM fcteam", null);
        if(cursor != null){
           // Toast.makeText(this, "ok",Toast.LENGTH_SHORT).show();
            while (cursor.moveToNext()){
                teamList.add(new teamCls(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getBlob(2),
                        cursor.getString(3),
                        cursor.getString(4)
                ));
            }
            adapterTeam = new adapterTeamCls(this, R.layout.row_team,teamList);
            lvTeam.setAdapter(adapterTeam);
        }else {
            Toast.makeText(this, "not ok",Toast.LENGTH_SHORT).show();
        }
    }
}