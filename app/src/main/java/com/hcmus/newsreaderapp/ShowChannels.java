package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ShowChannels extends Activity {
    ArrayAdapter<String> adapterMainSubjects;
    ListView myMainListView;
    Context context;
    SingleItem selectedNewsItem;
    ImageView logo;
    TextView title;
    String mCoreURLaddress;
    // hard-coding main NEWS categories (TODO: use a resource file)
//    String[][] myUrlCaptionMenu = {
//            {"https://feeds.npr.org/510289/podcast.xml", "Business"},
//            {"https://feeds.npr.org/344098539/podcast.xml", "Comedy"},
//            {"https://feeds.npr.org/510308/podcast.xml", "Science"},
//            {"https://feeds.npr.org/510298/podcast.xml", "Technology"},
//            {"https://feeds.npr.org/510306/podcast.xml", "Music"},
//            {"https://feeds.npr.org/510354/podcast.xml", "Kid & family"},
//            {"https://feeds.npr.org/510309/podcast.xml", "Society & culture"}
//    };
    ArrayList<ArrayList<String>> myUrlCaptionMenu=new ArrayList<>();
    String[] myUrlCaption = new String[myUrlCaptionMenu.size()];
    String[] myUrlAddress = new String[myUrlCaptionMenu.size()];

    public static String niceDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d, yyyy",
                Locale.US);
        return sdf.format(new Date()); //Monday Apr 7, 2014
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items_activity);
        Intent callingIntent = getIntent();
        Bundle myBundle = callingIntent.getExtras();
        title=(TextView) findViewById(R.id.txtTitle);
        logo=(ImageView)findViewById(R.id.logo);
        logo.setBackgroundResource(myBundle.getInt("image"));
        title.setText(myBundle.getString("title"));
        mCoreURLaddress=myBundle.getString("url");
        System.out.println(mCoreURLaddress);
        new ParseURL(ShowChannels.this,myBundle.getInt("image")).execute(mCoreURLaddress);

    }//onCreate
    public void getURLList(){


    }
}