package com.example.serviceprovider;

import android.os.AsyncTask;
import android.util.Log;

import com.example.serviceprovider.autosearch.Poi;
import com.example.serviceprovider.autosearch.TMapSearchInfo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class AutoCompleteParse extends AsyncTask<String, Void, ArrayList<SearchEntity>>
{
    private final String TMAP_API_KEY = "l7xxa7619246ec224970a14441f3386b2a10";
    private final int SEARCH_COUNT = 20;  // minimum is 20
    private ArrayList<SearchEntity> mListData;
    private RecyclerViewAdapter mAdapter;

    public AutoCompleteParse(RecyclerViewAdapter adapter) {
        this.mAdapter = adapter;
        mListData = new ArrayList<SearchEntity>();
    }

    @Override
    protected ArrayList<SearchEntity> doInBackground(String... word) {
        return getAutoComplete(word[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<SearchEntity> autoCompleteItems) {
        mAdapter.setData(autoCompleteItems);
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<SearchEntity> getAutoComplete(String word){

        try{
            String encodeWord = URLEncoder.encode(word, "UTF-8");
            URL acUrl = new URL(
                    "https://apis.openapi.sk.com/tmap/pois?version=1&page=1&count=" + SEARCH_COUNT + "&searchKeyword="+encodeWord+"&areaLLCode=&areaLMCode=&resCoordType=WGS84GEO&searchType=&searchtypCd=&radius=&reqCoordType=&centerLon=&centerLat=&multiPoint=&callback=&appKey=l7xxa7619246ec224970a14441f3386b2a10"
            );
//	"https://apis.openapi.sk.com/tmap/pois?version={1}&page={1}&count={" + SEARCH_COUNT + "}&searchKeyword={"+encodeWord+"}&areaLLCode=&areaLMCode=&resCoordType={WGS84GEO}&searchType=&searchtypCd=&radius=&reqCoordType=&centerLon=&centerLat=&multiPoint=&callback=&appKey={l7xxa7619246ec224970a14441f3386b2a10}"
            HttpURLConnection acConn = (HttpURLConnection)acUrl.openConnection();
            acConn.setRequestProperty("Accept", "application/json");


            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    acConn.getInputStream()));

            String line = reader.readLine();
            if(line == null){
                mListData.clear();
                return mListData;
            }

            reader.close();

            mListData.clear();

            TMapSearchInfo searchPoiInfo = new Gson().fromJson(line, TMapSearchInfo.class);

            ArrayList<Poi> poi =  searchPoiInfo.getSearchPoiInfo().getPois().getPoi();
            for(int i =0; i < poi.size(); i++){
                String fullAddr = poi.get(i).getUpperAddrName() + " " + poi.get(i).getMiddleAddrName() +
                        " " + poi.get(i).getLowerAddrName() + " " + poi.get(i).getDetailAddrName();
                Log.d("@@@",fullAddr + poi.get(i).getName());
                mListData.add(new SearchEntity(poi.get(i).getName(), fullAddr, poi.get(i).getFrontLat(), poi.get(i).getFrontLon()));
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return mListData;
    }
}
