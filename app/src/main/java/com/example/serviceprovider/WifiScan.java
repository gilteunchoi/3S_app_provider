package com.example.serviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.serviceprovider.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WifiScan extends AppCompatActivity {

    private TextView textView;
    private Button start;
    private WifiManager wifiManager;
    MyHandler handler = null;

    private boolean isthread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);

        textView = (TextView) findViewById(R.id.textView);
        start = (Button) findViewById(R.id.Start);

        handler = new MyHandler();

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        // 5GHz 지원 여부 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (wifiManager.is5GHzBandSupported()) {
                Toast.makeText(this, "5g true", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "5g flase", Toast.LENGTH_SHORT).show();
            }
        }

        // 브로드캐스트 리스너 등록
        // 스캔 요청이 완료된 후에 호출되어 성공/실패를 제공
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);

                if (success) {
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        };

        // filter등록
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getApplicationContext().registerReceiver(wifiScanReceiver, intentFilter);

        // if click Scan button
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isthread) {
                    isthread = false;
                    start.setText("START SCAN");
                } else {
                    isthread = true;
                    start.setText("STOP SCAN");

                    // Create AP Scan Thread
                    Thread thread1 = new Thread() {
                        boolean success;

                        @Override
                        public void run() {
                            while (isthread) {
                                Log.e("thread status", "true");
                                success = wifiManager.startScan();

                                //Toast.makeText(MainActivity.this, "Scan Start", Toast.LENGTH_SHORT).show();

                                // 단시간에 지나치게 많은 스캔, 하드웨어가 스캔 실패 보고 등의 실패
                                if (!success) {
                                    handler.sendEmptyMessage(2);
                                }

                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.e("thread status", "false");
                        }
                    };

                    thread1.start();
                }
            }
        });
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

    class MyHandler extends Handler {
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int count;

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    List<ScanResult> results = wifiManager.getScanResults();
                    String text = "";
                    String resultText = "";
                    count = 1;

                    Toast.makeText(WifiScan.this, "Scan Success", Toast.LENGTH_SHORT).show();

                    // AP정보 수집
                    for (final ScanResult result : results) {
                        String SSID = result.SSID;
                        String BSSID = result.BSSID;
                        int RSSI = result.level;
                        int Frequency = result.frequency;

                        long time = 0;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            time = result.timestamp;
                        }

                        long Now = System.currentTimeMillis();
                        Date ReDate = new Date(Now);
                        String formatDate = Format.format(ReDate);

                        text = "NUMBER : " + count + "\n" + "SSID : " + SSID + "\n" + "BSSID : " + BSSID + "\n" + "Time1 : "
                                + time + "\n" + "Time2 : " + formatDate + "\n" + "RSSI : " + RSSI + "\n" + "Frequency : " + Frequency;
                        resultText += text + "\n\n\n";
                        count++;
                    }
                    textView.setText(resultText);
                    break;
                case 2:
                    Toast.makeText(WifiScan.this, "Scan Fail", Toast.LENGTH_SHORT).show();
                    // handle failure: new scan did NOT succeed
                    // consider using old scan results: these are the OLD results!
                    //List<ScanResult> results = wifiManager.getScanResults();
                    // 이전 검색 결과 사용
                    break;
            }
            ;
        }
    }
}
