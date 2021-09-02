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

    private ArrayList<SearchEntity> items = new ArrayList<>();
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

            viewHolder.title.setText(items.get(position).getTitle());
            for(int i=0;i<20;i++)
                Log.d("###", items.get(i).getTitle());
            viewHolder.address.setText(items.get(position).getAddress());

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.d("%%%", items.get(ItemPosition).getLattitude());
                    String dataForServer ="{\"ID\":\"339fb371-9a57-401b-87cf-7390dbea449d\""+","+"\"method\":\"look\""+","+"\"latitude\""+":"+ "\""+ items.get(ItemPosition).getLattitude()+"\""+","+"\"longtitude\""+":"+ "\""+ items.get(ItemPosition).getLongtitude()+"\""+"}";
                    Log.d("%%%",dataForServer);
                    sendStringToServer("http://52.78.131.107:8000/provider",dataForServer);
                    int dur = (int)1.0;
                    Toast.makeText(v.getContext() , items.get(position).getTitle(), dur).show();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setData(ArrayList<SearchEntity> itemLists) {
        this.items = itemLists;
    }

    public void setCallback(RecyclerViewAdapterCallback callback) {
        this.callback = callback;
    }

    public void filter(String keyword) {
        if (keyword.length() >= 2) {
            try {
                com.example.serviceprovider.AutoCompleteParse parser = new com.example.serviceprovider.AutoCompleteParse(this);
                items.addAll(parser.execute(keyword).get());
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
            String result;
            ConnectRequest connectRequest = new ConnectRequest();
            result = connectRequest.requestToServer(url, data);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            String replyFromServer = s;

        }
    }
}
