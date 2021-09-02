package com.example.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SubwayRegister extends AppCompatActivity {

    private boolean isthread = false;
    LinearLayout l1, l2, l3, l4;
    EditText address, location;
    Button find, register, createregister, subprovideservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subway_register);

        l1 = (LinearLayout)findViewById(R.id.linearlayout1);
        l2 = (LinearLayout)findViewById(R.id.linearlayout2);
        l3 = (LinearLayout)findViewById(R.id.linearlayout3);
        l4 = (LinearLayout)findViewById(R.id.linearlayout4);

        address = (EditText) findViewById(R.id.editText);
        find = (Button) findViewById(R.id.button);
        final Geocoder geocoder = new Geocoder(this);

        location = (EditText) findViewById(R.id.location1);
        register = (Button) findViewById(R.id.btnregister1);
        createregister = (Button) findViewById((R.id.btncreateregister));
        subprovideservice = (Button) findViewById(R.id.btnSubProvideService);

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

                String location = SubwayRegister.this.location.getText().toString();
                Intent intent = new Intent(getApplicationContext(), WifiScan.class);
                switch (view.getId()) {
                    case R.id.btnregister1:
                        if(location.equals(""))
                            Toast.makeText(SubwayRegister.this, "Please enter right location", Toast.LENGTH_SHORT).show();
                        else{
                            intent.putExtra("entrance", "1");
                            startActivity(intent);
                        }
                        break ;
                    case R.id.btnregister2 :
                        if(location.equals(""))
                            Toast.makeText(SubwayRegister.this, "Please enter right location", Toast.LENGTH_SHORT).show();
                        else {
                            intent.putExtra("entrance", "2");
                            startActivity(intent);
                        }
                            break ;
                    case R.id.btnregister3 :
                        if(location.equals(""))
                            Toast.makeText(SubwayRegister.this, "Please enter right location", Toast.LENGTH_SHORT).show();
                        else {
                            intent.putExtra("entrance", "3");
                            startActivity(intent);
                        }
                            break ;
                    case R.id.btnregister4 :
                        if(location.equals(""))
                            Toast.makeText(SubwayRegister.this, "Please enter right location", Toast.LENGTH_SHORT).show();
                        else {
                            intent.putExtra("entrance", "4");
                            startActivity(intent);
                        }
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


        createregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateRegister(v);
            }
        });

        subprovideservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isthread) {
                    isthread = false;
                    subprovideservice.setText("START");
                } else {
                    isthread = true;
                    subprovideservice.setText("STOP");

                    Thread thread1 = new Thread() {
                        boolean success;

                        @Override
                        public void run() {
                            while (isthread) {

                                String user= "{\"method\""+":"+"\"look\"}";
                                sendStringToServer("http://52.78.131.107:8000/provider", user);

                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    };
                    thread1.start();
                }
            }
        });
    }

    private void CreateRegister(View view){
        if(l2.getVisibility()==view.GONE) l2.setVisibility(view.VISIBLE);
        else if(l2.getVisibility()==view.VISIBLE && l3.getVisibility()==view.GONE) l3.setVisibility(view.VISIBLE);
        else if(l3.getVisibility()==view.VISIBLE && l4.getVisibility()==view.GONE) l4.setVisibility(view.VISIBLE);
    }

    public void sendStringToServer(String url, String dataToSend){
        SubwayRegister.DataForServer dataForServer = new SubwayRegister.DataForServer(url, dataToSend);
        dataForServer.execute();
    }

    public class DataForServer extends AsyncTask<Void, Void, String> {
        private String url;
        private String data;
        private String replyFromServer;

        public DataForServer(String url, String data) {
            this.url = url;
            this.data = data;
        }
        @Override
        protected String doInBackground(Void... params) {
            String result;
            ConnectRequest connectRequest = new ConnectRequest();
            result = connectRequest.requestToServer(url, data);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            replyFromServer = s;
            JSONObject json = null;

            try {
                json = new JSONObject(replyFromServer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                replyFromServer= json.getString("alarm");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!replyFromServer.equals("None")){
                AlertDialog.Builder builder = new AlertDialog.Builder(SubwayRegister.this);
                builder.setTitle("서비스제공");
                builder.setMessage("사용자가"+ replyFromServer + "번 출구에 있습니다.");
                builder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "확인하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.setCancelable(false);
                builder.show();
            }

        }
    }
}