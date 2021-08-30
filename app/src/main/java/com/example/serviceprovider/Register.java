package com.example.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class Register extends AppCompatActivity {

    TextView map;
    LinearLayout l1, l2, l3, l4;
    EditText address, location;
    Button find, register, createregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        l1 = (LinearLayout)findViewById(R.id.linearlayout1);
        l2 = (LinearLayout)findViewById(R.id.linearlayout2);
        l3 = (LinearLayout)findViewById(R.id.linearlayout3);
        l4 = (LinearLayout)findViewById(R.id.linearlayout4);

        map = (TextView) findViewById(R.id.map);
        address = (EditText) findViewById(R.id.editText1);
        find = (Button) findViewById(R.id.button1);
        final Geocoder geocoder = new Geocoder(this);

        location = (EditText) findViewById(R.id.location1);
        register = (Button) findViewById(R.id.btnregister1);
        createregister = (Button) findViewById((R.id.btncreateregister));

        find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TextView textView1 = (TextView) findViewById(R.id.textView1);
                String location = Register.this.location.getText().toString();
                Intent intent = new Intent(getApplicationContext(), WifiScan.class);
                switch (view.getId()) {
                    case R.id.btnregister1:
                        //Intent intent = new Intent(getApplicationContext(), WifiScanActivity.class);
                        intent.putExtra("entrance", "1");
                        startActivity(intent);
                        break ;
                    case R.id.btnregister2 :
                        //Intent intent = new Intent(getApplicationContext(), WifiScanActivity.class);
                        intent.putExtra("entrance", "2");
                        startActivity(intent);
                        break ;
                    case R.id.btnregister3 :
                        //Intent intent = new Intent(getApplicationContext(), WifiScanActivity.class);
                        intent.putExtra("entrance", "3");
                        startActivity(intent);
                        break ;
                    case R.id.btnregister4 :
                        //Intent intent = new Intent(getApplicationContext(), WifiScanActivity.class);
                        intent.putExtra("entrance", "4");
                        startActivity(intent);
                        break ;
                }
            }
        } ;

        Button register1 = (Button) findViewById(R.id.btnregister1) ;
        register1.setOnClickListener(onClickListener) ;
        Button register2 = (Button) findViewById(R.id.btnregister2) ;
        register2.setOnClickListener(onClickListener) ;
        Button register3 = (Button) findViewById(R.id.btnregister3) ;
        register3.setOnClickListener(onClickListener) ;
        Button register4 = (Button) findViewById(R.id.btnregister4) ;
        register4.setOnClickListener(onClickListener) ;

        /*
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String location = Register.this.location.getText().toString();

                if(location.equals(""))
                    Toast.makeText(Register.this, "Please enter right location", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getApplicationContext(), WifiScan.class);
                    startActivity(intent);
                }
            }
        });
         */

        createregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRegister(v);
            }
        });
    }

    private void CreateRegister(View view){
        if(l2.getVisibility()==view.GONE) l2.setVisibility(view.VISIBLE);
        else if(l2.getVisibility()==view.VISIBLE && l3.getVisibility()==view.GONE) l3.setVisibility(view.VISIBLE);
        else if(l3.getVisibility()==view.VISIBLE && l4.getVisibility()==view.GONE) l4.setVisibility(view.VISIBLE);
    }
}