package com.example.serviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WifiScanActivity extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private WifiManager wifiManager;
    NewHandle handling = null;
    private boolean isthread = false;
    String wifiList;
    String DataList;
    int numberOfscans=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.Get);
        handling = new NewHandle();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean receiveSuccess = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);

                if (receiveSuccess) {
                    handling.sendEmptyMessage(1);
                } else {
                    handling.sendEmptyMessage(2);
                }
            }
        };

        setFilter(wifiScanReceiver);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isthread) {
                    isthread = false;
                    button.setText("START SCAN");
                } else {
                    isthread = true;
                    button.setText("STOP SCAN");
                    Toast.makeText(WifiScanActivity.this, "wifi scanning", Toast.LENGTH_SHORT).show();

                    // Create AP Scan Thread
                    Thread thread1 = new Thread() {
                        boolean success;

                        @Override
                        public void run() {
                            while (isthread) {
                                boolean scanSuccess = wifiManager.startScan();

                                if (!scanSuccess) {
                                    handling.sendEmptyMessage(2);
                                }

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

    private void setFilter(BroadcastReceiver wifiScanReceiver) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);
    }

    public void sendStringToServer(String url, String dataToSend){
        DataForServer dataForServer = new DataForServer(url, dataToSend);
        dataForServer.execute();
    }

    public class DataForServer extends AsyncTask<Void, Void, String> {
        private String url;
        private String data;

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
            TextView textView = (TextView)findViewById(R.id.textView);
            textView.setText(s);    // 결과 보이도록
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class NewHandle extends Handler {
        int count;

        @Override
        public void handleMessage(@NonNull Message handlerMessage) {
            switch (handlerMessage.what) {
                case 1:
                    List<ScanResult> results = wifiManager.getScanResults();
                    String tempText = "";
                    String textToShow = "{\"ID\":\"40dfb246-0026-11ec-9a03-0242ac130003\"";
                    count = 1;

                    for (final ScanResult result : results) {
                        if(count<11) {
                            String SSID = result.SSID;
                            if (SSID.contains("AndroidHotspot")) {        //핫스팟 제거
                                continue;
                            }
                            String BSSID = result.BSSID;
                            int RSSI = result.level;

                            tempText = "," + "\"" + "BSSID" + "\"" + ":" + "\"" + BSSID + "\"" + "," + "\"" + "RSSI" + "\"" + ":" + "\"" + RSSI + "\"";
                            textToShow += tempText;
                            count++;
                        }
                    }
                    textView = (TextView) findViewById(R.id.textView);
                    textView.setText(tempText);
                    numberOfscans++;
                    textToShow+="}";
                    wifiList=textToShow;
                    //sendStringToServer("http://52.78.131.107:8000/provider", wifiList);
                    /*
                    DataList+=wifiList;
                    if(numberOfscans==10) {
                        sendStringToServer("http://52.78.131.107:8000/provider", DataList);//서버로 보냄
                        Toast.makeText(context.this,"sent complete",1);
                    }*/
                    break;
                case 2:
                    //문제가 있을 때
                    break;
            }
            ;
        }
    }
}

