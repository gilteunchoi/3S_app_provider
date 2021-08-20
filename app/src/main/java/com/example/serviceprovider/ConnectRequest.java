package com.example.serviceprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ConnectRequest {

    public String requestToServer(String serverUrl, String dataToSend){
        HttpURLConnection serverConnection = null;
        StringBuffer stringBuffer = new StringBuffer();

        if (dataToSend == null)
            stringBuffer.append("No Wifi"); // 보낼 데이터 없음( 디폴트값 설정 )
        else
            stringBuffer.append(dataToSend);

        try{
            URL url = new URL(serverUrl);
            serverConnection = (HttpURLConnection) url.openConnection();

            serverConnection.setConnectTimeout(16000);
            serverConnection.setReadTimeout(6000);
            serverConnection.setDoInput(true);
            serverConnection.setDoOutput(true);
            serverConnection.setUseCaches(false);
            serverConnection.setRequestMethod("POST"); // POST.
            serverConnection.setRequestProperty("Accept-Charset", "UTF-8");
            serverConnection.setRequestProperty("Context_Type", "application/x-www-form-urlencode");
            serverConnection.setRequestProperty("apikey", ""); // "" = apikey

            String strParams = stringBuffer.toString(); //toString wifi1&wifi2
            OutputStream os = serverConnection.getOutputStream();
            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if (serverConnection.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;


            BufferedReader reader = new BufferedReader(new InputStreamReader(serverConnection.getInputStream(), "UTF-8"));
            String line;
            String dataToShow = "";


            while ((line = reader.readLine()) != null){
                dataToShow += line;
            }
            return dataToShow;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverConnection != null) serverConnection.disconnect();
        } return null;
    }
}
