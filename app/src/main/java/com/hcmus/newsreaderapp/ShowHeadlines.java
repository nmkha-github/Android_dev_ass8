package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ShowHeadlines extends Activity {

    ArrayList<SingleItem> newsList = new ArrayList<>();
    ListView mListView;
    String urlAddress = "", titleString = "", urlCaption = "";
    SingleItem selectedNewsItem;
    TextView title;
    ImageView logo;
    int logoID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items_activity);
        mListView = (ListView) this.findViewById(R.id.myListView);

        Intent callingIntent = getIntent();
        Bundle mBundle = callingIntent.getExtras();
        urlCaption = mBundle.getString("urlCaption");
        urlAddress = mBundle.getString("urlAddress");
        titleString = mBundle.getString("title");
        title = (TextView) findViewById(R.id.txtTitle);
        title.setText(String.format("ITEMS IN CHANNEL %s", mBundle.getString("title")));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        logo = (ImageView) findViewById(R.id.logo);
        logoID = mBundle.getInt("image");
        logo.setBackgroundResource(logoID);

        setTitle(titleString + " \t" + DateUtils.niceDate());
        mListView = (ListView) this.findViewById(R.id.myListView);
        mListView.setOnItemClickListener((av, v, index, id) -> {
            selectedNewsItem = newsList.get(index);
            showNiceDialogBox(selectedNewsItem);
        });

        DownloadRssFeed downloader = new DownloadRssFeed(new WeakReference<>(ShowHeadlines.this));
        downloader.execute(urlAddress, titleString);
    }

    public void showNiceDialogBox(SingleItem selectedStoryItem) {
        String title = selectedStoryItem.getTitle();
        String description = selectedStoryItem.getDescription();
        if (title.equalsIgnoreCase(description)) {
            description = "";
        }
        try {
            final Uri storyLink = Uri.parse(selectedStoryItem.getLink());
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);

            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_dialog, null);
            TextView txtTitle = (TextView) dialogView.findViewById(R.id.txt_title);
            TextView txtDescription = (TextView) dialogView.findViewById(R.id.txt_description);
            Button btnMore = (Button) dialogView.findViewById(R.id.btn_more);
            Button btnClose = (Button) dialogView.findViewById(R.id.btn_close);
            txtTitle.setText(HtmlCompat.fromHtml(titleString, HtmlCompat.FROM_HTML_MODE_LEGACY));
            String showDescription = title + "\n\n" + HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_LEGACY);
            if (showDescription.length() > 500) {
                showDescription = showDescription.substring(0, 500);
                showDescription = showDescription + "..." + "\n";
            } else {
                showDescription += "\n";
            }
            txtDescription.setText(showDescription);
            myBuilder.setView(dialogView);
            AlertDialog dialogShow = myBuilder.create();
            dialogShow.show();
            btnMore.setOnClickListener(view -> {
                Intent browser = new Intent(Intent.ACTION_VIEW, storyLink);
                startActivity(browser);
            });
            btnClose.setOnClickListener(view -> dialogShow.dismiss());

        } catch (Exception e) {
            Log.e("Error DialogBox", e.getMessage());
        }
    }
}