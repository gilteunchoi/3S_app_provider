package com.example.serviceprovider;


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<SearchEntity> itemLists = new ArrayList<>();
    private RecyclerViewAdapterCallback callback;

    public static class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView address;

        public CustomViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            address = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int ItemPosition = position;
        if( holder instanceof CustomViewHolder) {
            CustomViewHolder viewHolder = (CustomViewHolder)holder;

            viewHolder.title.setText(itemLists.get(position).getTitle());
            for(int i=0;i<20;i++)
                Log.d("###",itemLists.get(i).getTitle());
            viewHolder.address.setText(itemLists.get(position).getAddress());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //callback.showToast(ItemPosition);
                    Log.d("%%%",itemLists.get(ItemPosition).getLattitude());
                    String dataForServer ="{\"ID\":\"339fb371-9a57-401b-87cf-7390dbea449d\""+","+"\"method\":\"look\""+","+"\"latitude\""+":"+ "\""+itemLists.get(ItemPosition).getLattitude()+"\""+","+"\"longtitude\""+":"+ "\""+itemLists.get(ItemPosition).getLongtitude()+"\""+"}";
                    Log.d("%%%",dataForServer);
                    sendStringToServer("http://52.78.131.107:8000/provider",dataForServer);
                    int dur = (int)1.0;
                    Toast.makeText(v.getContext() ,itemLists.get(position).getTitle(), dur).show();
                    String data = itemLists.get(position).getTitle();
                    //Intent intent = new Intent(getApplicationContext(), Register.class);
                    //intent.putExtra("address", data);
                    //startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemLists.size();
    }

    public void setData(ArrayList<SearchEntity> itemLists) {
        this.itemLists = itemLists;
    }

    public void setCallback(RecyclerViewAdapterCallback callback) {
        this.callback = callback;
    }

    public void filter(String keyword) {
        if (keyword.length() >= 2) {
            try {
                com.example.serviceprovider.AutoCompleteParse parser = new com.example.serviceprovider.AutoCompleteParse(this);
                itemLists.addAll(parser.execute(keyword).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
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
            String replyFromServer = s;    // 결과 보이도록
            /*
            JSONObject json = null;
            try {
                json = new JSONObject(replyFromServer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                replyFromServer= json.getString("location");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            */

        }
    }
}
