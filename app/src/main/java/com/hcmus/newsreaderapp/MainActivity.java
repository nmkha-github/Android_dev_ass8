package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {
    ArrayAdapter<String> adapterMainSubjects;
    ListView myMainListView;
    Context context;
    SingleItem selectedNewsItem;

    // hard-coding main NEWS categories (TODO: use a resource file)
    String[][] myUrlCaptionMenu = {
            {"https://feeds.npr.org/510289/podcast.xml", "Business"},
            {"https://feeds.npr.org/344098539/podcast.xml", "Comedy"},
            {"https://feeds.npr.org/510308/podcast.xml", "Science"},
            {"https://feeds.npr.org/510298/podcast.xml", "Technology"},
            {"https://feeds.npr.org/510306/podcast.xml", "Music"},
            {"https://feeds.npr.org/510354/podcast.xml", "Kid & family"},
            {"https://feeds.npr.org/510309/podcast.xml", "Society & culture"}
    };

    String[] myUrlCaption = new String[myUrlCaptionMenu.length];
    String[] myUrlAddress = new String[myUrlCaptionMenu.length];

    public static String niceDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d, yyyy",
                Locale.US);
        return sdf.format(new Date()); //Monday Apr 7, 2014
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < myUrlAddress.length; i++) {
            myUrlAddress[i] = myUrlCaptionMenu[i][0];
            myUrlCaption[i] = myUrlCaptionMenu[i][1];
        }
        context = getApplicationContext();
        this.setTitle("NPR Headline News\n" + niceDate());
// user will tap on a ListView’s row to request category’s headlines
        myMainListView = (ListView) this.findViewById(R.id.myListView);
        myMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
                String urlAddress = myUrlAddress[_index], urlCaption = myUrlCaption[_index];
//create an Intent to talk to activity: ShowHeadlines
                Intent callShowHeadlines = new Intent(MainActivity.this, ShowHeadlines.class);
//prepare a Bundle and add the input arguments: url & caption
                Bundle myData = new Bundle();
                myData.putString("urlAddress", urlAddress);
                myData.putString("urlCaption", urlCaption);
                callShowHeadlines.putExtras(myData);
                startActivity(callShowHeadlines);
            }
        });
// fill up the Main-GUI’s ListView with main news categories
        adapterMainSubjects = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myUrlCaption);
        myMainListView.setAdapter(adapterMainSubjects);
    }//onCreate
}