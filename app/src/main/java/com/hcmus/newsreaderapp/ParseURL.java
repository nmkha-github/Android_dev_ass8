package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

public class ParseURL extends AsyncTask<String,Void, ArrayList<ArrayList<String>>> {
    Context contextParent;
    int logoID;
    public ParseURL(Context contextParent,int logoID) {
        this.contextParent=contextParent;
        this.logoID=logoID;
    }
    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... params) {
        ArrayList<ArrayList<String>> URLcaptionList=new ArrayList<>();
        try {
            Document document = Jsoup.connect(params[0]).get();
            Elements hyperlinks=document.getElementsByTag("a");
            int i=0;
            for(Element hyperlink:hyperlinks){
                String url=hyperlink.attr("href");
                if(url.contains(".rss")||url.contains(".xml")){
                    String title=hyperlink.text();
                    if(title.trim().isEmpty()||title.contains("Trang chủ")||title.contains("Home")||title.contains("USNEWS")){
                        continue;
                    }
                    title=title.replace("RSS","");
                    if(url.startsWith("/")) {
                        url = hyperlink.attr("abs:href");
                    }
                    title=title.toUpperCase();
                    URLcaptionList.add(new ArrayList<>());
                    URLcaptionList.get(i).add(url);
                    URLcaptionList.get(i).add(title);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return URLcaptionList;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> URLcaptionList) {
        super.onPostExecute(URLcaptionList);
        ListView mainListView=(ListView) ((Activity)contextParent).findViewById(R.id.myListView);
        TextView title=(TextView)((Activity)contextParent).findViewById(R.id.txtTitle);
        String[] myUrlCaption = new String[URLcaptionList.size()];
        String[] myUrlAddress = new String[URLcaptionList.size()];
        for (int i = 0; i < myUrlAddress.length; i++) {
            myUrlAddress[i] = URLcaptionList.get(i).get(0);
            myUrlCaption[i] = URLcaptionList.get(i).get(1);
        }
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
                String urlAddress = myUrlAddress[_index], urlCaption = myUrlCaption[_index];
                Intent callShowHeadlines = new Intent(contextParent, ShowHeadlines.class);
                Bundle myData = new Bundle();
                myData.putString("urlAddress", urlAddress);
                myData.putString("urlCaption", urlCaption);
                myData.putString("title","ITEMS IN CHANNEL "+myUrlCaption[_index]+" - "+title.getText().toString().substring(12));
                myData.putInt("image",logoID);
                callShowHeadlines.putExtras(myData);
                contextParent.startActivity(callShowHeadlines);
            }
        });
        ArrayAdapter<String> adapterMainSubjects = new ArrayAdapter<String>(contextParent, R.layout.my_simple_list_item_1, myUrlCaption);
        mainListView.setAdapter(adapterMainSubjects);
    }
}
