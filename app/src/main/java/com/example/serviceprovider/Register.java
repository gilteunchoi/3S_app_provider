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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class Register extends AppCompatActivity {

    TextView map;
    EditText address, location;
    Button find, register, createregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        map = (TextView) findViewById(R.id.textView4);
        address = (EditText) findViewById(R.id.editText1);
        find = (Button) findViewById(R.id.button1);
        final Geocoder geocoder = new Geocoder(this);

        location = (EditText) findViewById(R.id.location);
        register = (Button) findViewById(R.id.btnregister);
        createregister = (Button) findViewById((R.id.btncreateregister));

        find.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // 주소입력후 지도2버튼 클릭시 해당 위도경도값의 지도화면으로 이동
                List<Address> list = null;

                String str = address.getText().toString();
                try {
                    list = geocoder.getFromLocationName
                            (str, // 지역 이름
                                    10); // 읽을 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test","입출력 오류 - 서버에서 주소변환시 에러발생");
                }

                if (list != null) {
                    if (list.size() == 0) {
                        map.setText("해당되는 주소 정보는 없습니다");
                    } else {
                        // 해당되는 주소로 인텐트 날리기
                        Address addr = list.get(0);
                        double lat = addr.getLatitude();
                        double lon = addr.getLongitude();

                        String sss = String.format("geo:%f,%f", lat, lon);

                        Intent intent = new Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(sss));
                        startActivity(intent);
                    }
                }
            }
        });
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