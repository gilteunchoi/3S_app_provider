package com.example.loginsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText wifissid, password;
    Button register;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        wifissid = (EditText) findViewById(R.id.wifissid);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.btnregister);
        DB = new DBHelper(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wifi = wifissid.getText().toString();
                String pass = password.getText().toString();

                if(wifi.equals("")||pass.equals(""))
                    Toast.makeText(Register.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuser = DB.checkusername(wifi);
                    if(checkuser==false){
                        Boolean insert = DB.insertData(wifi, pass);
                        if(insert==true){
                            Toast.makeText(Register.this, "Registered sucessfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Register.this, "User already exists! please", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}