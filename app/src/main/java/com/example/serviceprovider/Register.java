package com.example.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText location;
    Button register, createregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        location = (EditText) findViewById(R.id.location);
        register = (Button) findViewById(R.id.btnregister);
        createregister = (Button) findViewById((R.id.btncreateregister));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String location = Register.this.location.getText().toString();

                if(location.equals(""))
                    Toast.makeText(Register.this, "Please enter right location", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(Register.this, "wifi scanning", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), WifiScan.class);
                    startActivity(intent);
                }
            }
        });

        createregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRegister();
            }
        });
    }

    private void CreateRegister(){

    }
}