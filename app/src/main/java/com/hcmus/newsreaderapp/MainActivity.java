package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity{
    ArrayAdapter<String> adapterMainSubjects;
    GridView myMainListView;
    Context context;
    String[] myUrlCaption={"TUỔI TRẺ","VNEXPRESS","THANH NIÊN","NGƯỜI LAO ĐỘNG"};
    String[] myURL={"https://tuoitre.vn/rss.htm","https://vnexpress.net/rss","https://thanhnien.vn/rss.html","https://nld.com.vn/rss.htm"};
    int[] imageSource={R.drawable.tuoitre,R.drawable.vnexpress,R.drawable.thanhnien,R.drawable.nguoilaodong};
    public static String niceDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d, yyyy",
                Locale.US);
        return sdf.format(new Date()); //Monday Apr 7, 2014
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        this.setTitle("NPR Headline News\n" + niceDate());
// user will tap on a ListView’s row to request category’s headlines
        myMainListView = (GridView) this.findViewById(R.id.myGridView);
        myMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> _av, View _v, int _index, long _id) {
                String urlAddress = "";
                Intent callShowHeadlines = new Intent(MainActivity.this, ShowChannels.class);
                Bundle myData = new Bundle();
                myData.putString("url", myURL[_index]);
                myData.putString("title","CHANNELS IN "+myUrlCaption[_index]);
                myData.putInt("image",imageSource[_index]);
                callShowHeadlines.putExtras(myData);
                startActivity(callShowHeadlines);
            }
        });
// fill up the Main-GUI’s ListView with main news categories
        adapterMainSubjects = new ArrayAdapter<String>(this, R.layout.my_simple_list_item_2, myUrlCaption);
        myMainListView.setAdapter(adapterMainSubjects);
    }//onCreate
}
