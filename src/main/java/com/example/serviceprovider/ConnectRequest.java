package com.example.serviceprovider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class ConnectRequest {

    public String request(String Url, String dataToSend){
        HttpURLConnection server = null;
        StringBuffer Buffer = new StringBuffer();

        if (dataToSend == null)
            Buffer.append("No data");
        else
            Buffer.append(dataToSend);

        try{
            URL url = new URL(Url);
            server = (HttpURLConnection) url.openConnection();

            server.setConnectTimeout(16000);
            server.setReadTimeout(7000);
            server.setDoInput(true);
            server.setDoOutput(true);
            server.setUseCaches(false);
            server.setRequestMethod("POST");
            server.setRequestProperty("Accept-Charset", "UTF-8");
            server.setRequestProperty("Context_Type", "application/x-www-form-urlencode");
            server.setRequestProperty("apikey", "");

            String strParams = Buffer.toString();
            OutputStream os = server.getOutputStream();
            os.write(strParams.getBytes("UTF-8"));
            os.flush();
            os.close();

            if (server.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;


            BufferedReader reader = new BufferedReader(new InputStreamReader(server.getInputStream(), "UTF-8"));
            String line;
            String data = "";


            while ((line = reader.readLine()) != null){
                data += line;
            }
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) server.disconnect();
        } return null;
    }
}
