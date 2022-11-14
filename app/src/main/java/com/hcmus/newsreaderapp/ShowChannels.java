package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class ShowChannels extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items_activity);

        Intent callingIntent = getIntent();
        Bundle myBundle = callingIntent.getExtras();
        String titleString = myBundle.getString("title");
        TextView title = (TextView) findViewById(R.id.txtTitle);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setBackgroundResource(myBundle.getInt("image"));
        title.setText(String.format("CHANNELS IN %s", titleString));
        setTitle(titleString + " " + DateUtils.niceDate());
        String mCoreURLAddress = myBundle.getString("url");
        new ParseURL(new WeakReference<>(ShowChannels.this), myBundle.getInt("image")).execute(mCoreURLAddress);
    }
}