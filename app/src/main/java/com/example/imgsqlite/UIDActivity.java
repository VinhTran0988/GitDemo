package com.example.imgsqlite;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class UIDActivity extends AppCompatActivity {

    Button btnSave, btnChooseImg, btnDelete, btnCancel;
    EditText edtName, edtStadium, edtCapacity;
    ImageView imgView;
    Bitmap bitmap;
    int RQC_CHOOSE_IMG = 111;
    SQLiteDatabase DB;
    String ACTION = null;
    int ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_uid);
        Init();
        MakeOrOpenDB();
        Act();
        getReqAndData();
    }

    private void getReqAndData() {
        Intent intent = this.getIntent();
        ACTION = intent.getStringExtra("ACTION");
        switch (ACTION){
            case "ADD":
                btnDelete.setEnabled(false);
                break;
            case "UPDATE":
                btnDelete.setEnabled(true);
                ID = intent.getIntExtra("ID", -1);
                if(ID==-1){
                    finish();
                }else {
                    Cursor cursor = DB.rawQuery("SELECT * FROM fcteam WHERE ID=?", new String[] {ID +""});
                    cursor.moveToFirst();
                    edtName.setText(cursor.getString(1));
                    imgView.setImageBitmap(BitmapUtility.getImage(cursor.getBlob(2)));
                    bitmap = BitmapUtility.getImage(cursor.getBlob(2));
                    edtStadium.setText(cursor.getString(3));
                    edtCapacity.setText(cursor.getString(4));
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + ACTION);
        }
    }

    private void Act() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTeam();
            }
        });
    }

    private void DeleteTeam() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.img);
        builder.setTitle("Xoa doi bong");
        builder.setMessage("Ban co muon xoa?");

        builder.setPositiveButton("Co", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int i = DB.delete("fcteam", "ID="+ID+"",null);
                if(i<1){
                    Toast.makeText(UIDActivity.this,"Xoa that bai",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UIDActivity.this,"Xoa thanh cong",Toast.LENGTH_SHORT).show();
                }
                BacktoMainActivity();
            }
        });
        builder.setNeutralButton("khong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void BacktoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void SaveData() {
        String msg= "";
        String NAME = edtName.getText().toString().trim();
        String STADIUM = edtStadium.getText().toString().trim();
        String CAPACITY = edtCapacity.getText().toString().trim();

        switch (ACTION){
            case "ADD":
                msg= "ADD";
                if(!isEmptyData()){
                    msg = "Du lieu chua hop le";
                }else{
                    byte[] LOGO = BitmapUtility.getBytes(bitmap);
                    ContentValues values = new ContentValues();
                    values.put("NAME",NAME);
                    values.put("STADIUM",STADIUM);
                    values.put("CAPACITY",CAPACITY);
                    values.put("LOGO",LOGO);

                    long r = DB.insert("fcteam","_ID",values);

                    if (r==-1) {
                        msg = "them that bai";
                    }else{
                        msg = "them thanh cong!";
                        ClearControl();
                    }
                }
                break;
            case "UPDATE":
                byte[] LOGO = BitmapUtility.getBytes(bitmap);
                ContentValues values = new ContentValues();
                values.put("NAME",NAME);
                values.put("STADIUM",STADIUM);
                values.put("CAPACITY",CAPACITY);
                values.put("LOGO",LOGO);
                int u = DB.update("fcteam",values,"ID = '"+ID+"'",null);
                if(u>0){
                    msg ="Cap nhat thanh cong";
                }
                else {
                    msg = "cap nha that bai";
                }
                break;
        }

        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MainActivity.class));
    }

    private void ClearControl() {
        edtName.getText().clear();
        edtCapacity.getText().clear();
        edtStadium.getText().clear();
        bitmap = null;
        imgView.setImageBitmap(null);
    }

    private void chooseImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,RQC_CHOOSE_IMG);
    }

    private void Init() {
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
        btnSave = findViewById(R.id.btnSave);
        btnChooseImg = findViewById(R.id.btnChooseImg);

        edtName = findViewById(R.id.edtName);
        edtCapacity = findViewById(R.id.edtCapacity);
        edtStadium = findViewById(R.id.edtStadium);

        imgView = findViewById(R.id.imgView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RQC_CHOOSE_IMG && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
                imgView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isEmptyData() {
        String NAME = edtName.getText().toString().trim();
        String STADIUM = edtStadium.getText().toString().trim();
        String CAPACITY = edtCapacity.getText().toString().trim();
        if (NAME.isEmpty())
            return false;
        if (STADIUM.isEmpty())
            return false;
        if (CAPACITY.isEmpty())
            return false;
        if (bitmap == null)
            return false;
        return true;
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
}