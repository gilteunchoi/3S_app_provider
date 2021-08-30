package com.example.serviceprovider;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class WifiScan extends AppCompatActivity {

    private TextView textView;
    private Button button;
    private WifiManager wifiManager;
    NewHandle handling = null;
    private boolean isthread = false;
    String wifiList;
    String DataList;
    String num;
    int numberOfscans = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);

        TextView re = (TextView)findViewById(R.id.result);
        Intent intent = getIntent();
        String ent = intent.getExtras().getString("entrance");
        re.setText(ent);

        textView = (TextView) findViewById(R.id.wifi);
        button = (Button) findViewById(R.id.Get);
        handling = new NewHandle();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // 5GHz 지원 여부 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (wifiManager.is5GHzBandSupported()) {
                Toast.makeText(this, "5g true", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "5g flase", Toast.LENGTH_SHORT).show();
            }
        }
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);

                if (success) {
                    handling.sendEmptyMessage(1);
                } else {
                    handling.sendEmptyMessage(2);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isthread) {
                    isthread = false;
                    button.setText("START SCAN");
                } else {
                    isthread = true;
                    button.setText("STOP SCAN");

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
            TextView textView = (TextView)findViewById(R.id.wifi);
            textView.setText(s);    // 결과 보이도록
            Toast.makeText(WifiScan.this, "sent complete", Toast.LENGTH_SHORT).show();

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
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    class NewHandle extends Handler {
        int count;
        @Override
        public void handleMessage(@NonNull Message handlerMessage) {
            switch (handlerMessage.what) {
                case 1:
                    List<ScanResult> results = wifiManager.getScanResults();
                    String tempText = "";
                    //+","+"\"method\""+":"+"\"look\""
                    String textToShow = "{\"ID\":\"f96ecf5a-0020-11ec-9a03-0242ac130003\""+","+"\"method\""+":"+"\"scan\""+","+"\"location\""+":"+"\"look\"";
                    count = 0;

                    for (final ScanResult result : results) {
                        if(count<10) {
                            String SSID = result.SSID;
                            if (SSID.contains("AndroidHotspot")) {        //핫스팟 제거
                                continue;
                            }
                            String BSSID = result.BSSID;
                            int RSSI = result.level;

                            tempText = "," + "\"" + "BSSID" + count + "\"" + ":" + "\"" + BSSID + "\"" + "," + "\"" + "RSSI" + count + "\"" + ":" + "\"" + RSSI + "\"";
                            textToShow += tempText;
                            count++;
                        }

                    }
                    textToShow+="}";
                    wifiList=textToShow;
                    numberOfscans++;
                    num = Integer.toString(numberOfscans);

                    final TextView textview_address = (TextView) findViewById(R.id.wifi);
                    final TextView tv_num = (TextView) findViewById(R.id.num);

                    textview_address.setText(wifiList);
                    tv_num.setText(num);

                    DataList += textToShow;

                    if(numberOfscans==10) {
                        sendStringToServer("http://52.78.131.107:8000/user", wifiList);//신호전달
                    }
                    break;
                case 2:
                    Toast.makeText(WifiScan.this, "Scan Fail", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}

