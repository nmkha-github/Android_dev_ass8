package com.hcmus.newsreaderapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ParseURL extends AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

    // Use weak reference to avoid leaking context memory
    WeakReference<ShowChannels> callerContext;
    int logoID;

    public ParseURL(WeakReference<ShowChannels> contextParent, int logoID) {
        this.callerContext = contextParent;
        this.logoID = logoID;
    }

    protected void onPreExecute() {
        ShowChannels activity = callerContext.get();
        activity.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.textview_msg).setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... params) {
        ArrayList<ArrayList<String>> URLCaptionList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(params[0]).get();
            Elements hyperlinks = document.getElementsByTag("a");
            int i = 0;
            for (Element hyperlink : hyperlinks) {
                String url = hyperlink.attr("href");
                if (url.contains(".rss") || url.contains(".xml")) {
                    String title = hyperlink.text();
                    if (title.trim().isEmpty() || title.contains("Trang chủ") || title.contains("Home") || title.contains("USNEWS")) {
                        continue;
                    }
                    title = title.replace("RSS", "");
                    if (url.startsWith("/")) {
                        url = hyperlink.attr("abs:href");
                    }
                    title = title.toUpperCase();
                    URLCaptionList.add(new ArrayList<>());
                    URLCaptionList.get(i).add(url);
                    URLCaptionList.get(i).add(title);
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return URLCaptionList;
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> URLCaptionList) {
        super.onPostExecute(URLCaptionList);

        ShowChannels activity = callerContext.get();
        ListView mainListView = (ListView) (activity.findViewById(R.id.myListView));
        TextView title = (TextView) activity.findViewById(R.id.txtTitle);

        if (URLCaptionList.size() == 0)
            activity.findViewById(R.id.textview_empty).setVisibility(View.VISIBLE);
        else
            activity.findViewById(R.id.textview_empty).setVisibility(View.GONE);

        String[] myUrlCaption = new String[URLCaptionList.size()];
        String[] myUrlAddress = new String[URLCaptionList.size()];

        for (int i = 0; i < myUrlAddress.length; i++) {
            myUrlAddress[i] = URLCaptionList.get(i).get(0);
            myUrlCaption[i] = URLCaptionList.get(i).get(1);
        }

        mainListView.setOnItemClickListener((_av, _v, _index, _id) -> {
            String urlAddress = myUrlAddress[_index], urlCaption = myUrlCaption[_index];
            Intent callShowHeadlines = new Intent(activity, ShowHeadlines.class);
            Bundle myData = new Bundle();
            myData.putString("urlAddress", urlAddress);
            myData.putString("urlCaption", urlCaption);
            myData.putString("title", myUrlCaption[_index] + " - " + title.getText().toString().substring(12));
            myData.putInt("image", logoID);
            callShowHeadlines.putExtras(myData);
            activity.startActivity(callShowHeadlines);
        });

        ArrayAdapter<String> adapterMainSubjects = new ArrayAdapter<>(activity, R.layout.my_simple_list_item_1, myUrlCaption);
        mainListView.setAdapter(adapterMainSubjects);

        activity.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        activity.findViewById(R.id.textview_msg).setVisibility(View.GONE);
    }
}
