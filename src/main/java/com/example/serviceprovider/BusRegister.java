package com.example.serviceprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class BusRegister extends AppCompatActivity {

    private boolean isthread = false;

    EditText numofbus;
    Button registerbusnum, busprovideservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_register);

        numofbus = (EditText) findViewById(R.id.editText);
        registerbusnum = (Button) findViewById(R.id.button);
        busprovideservice = (Button) findViewById(R.id.btnBusProvideService);

        registerbusnum.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String numofbus = BusRegister.this.numofbus.getText().toString();
                if(numofbus.equals(""))
                    Toast.makeText(BusRegister.this, "Please enter right number", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(getApplicationContext(), "등록하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        busprovideservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isthread) {
                    isthread = false;
                    busprovideservice.setText("START");
                } else {
                    isthread = true;
                    busprovideservice.setText("STOP");

                    // Create AP Scan Thread
                    Thread thread1 = new Thread() {
                        boolean success;

                        @Override
                        public void run() {
                            while (isthread) {

                                String user= "{\"method\""+":"+"\"look\"}";
                                sendStringToServer("http://52.78.131.107:8000/bprovider", user);

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

    public void sendStringToServer(String url, String dataToSend){
        BusRegister.DataForServer dataForServer = new BusRegister.DataForServer(url, dataToSend);
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
            if (replyFromServer.equals("ok")){
                AlertDialog.Builder builder = new AlertDialog.Builder(BusRegister.this);
                builder.setTitle("서비스제공");
                builder.setMessage("다음 정류장에 탑승자가 있습니다.");
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