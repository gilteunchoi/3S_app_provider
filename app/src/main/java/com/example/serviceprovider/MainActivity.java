package com.example.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private boolean isthread = false;
    Button registprovider, provideservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registprovider = (Button) findViewById(R.id.btnRegistProvider);
        provideservice = (Button) findViewById(R.id.btnProvideService);

        registprovider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        provideservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isthread) {
                    isthread = false;
                    provideservice.setText("START");
                } else {
                    isthread = true;
                    provideservice.setText("STOP");

                    // Create AP Scan Thread
                    Thread thread1 = new Thread() {
                        boolean success;

                        @Override
                        public void run() {
                            while (isthread) {

                                String user= "{\"method\""+":"+"\"look\"}";
                                sendStringToServer("http://52.78.131.107:8000/provider", user);//신호전달

                                try {
                                    Thread.sleep(10000);    //아마도 10 초마다 ? 아닐수도
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
    public void sendStringToServer(String url, String dataToSend){
        DataForServer dataForServer = new DataForServer(url, dataToSend);
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
            String result; // 요청 결과
            ConnectRequest connectRequest = new ConnectRequest();
            result = connectRequest.requestToServer(url, data); // 해당 URL로 부터 결과물을 얻어온다.
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s); //요청 결과

            replyFromServer = s;    // 결과 보이도록
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
            if (replyFromServer == "None"){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("서비스제공");
                builder.setMessage("사용자가 "+replyFromServer+" 번 출구에 있습니다."); // 결과 보이도록
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
                builder.show();
            }

            /*
            if(wifiSendCheck) {
                showMessage(replyFromServer);
                wifiSendCheck = false;
            }
             */

        }
    }
}