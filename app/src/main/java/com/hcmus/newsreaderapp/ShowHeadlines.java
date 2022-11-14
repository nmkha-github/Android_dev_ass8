package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ShowHeadlines extends Activity {
    // Main category has already been selected by user: ‘World News’, Business’, ...
// ["urlCaption", "urlAddress"] comes in a bundle sent by main thread
// here we access RSS-feed and show corresponding headlines
    ArrayList<SingleItem> newsList = new ArrayList<SingleItem>();
    ListView myListView;
    String urlAddress = "", urlCaption = "";
    SingleItem selectedNewsItem;
    TextView title;
    ImageView logo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_items_activity);
        myListView = (ListView) this.findViewById(R.id.myListView);
// find out which intent is calling us & grab data bundle holding selected url & caption sent to us
        Intent callingIntent = getIntent();
        Bundle myBundle = callingIntent.getExtras();
        urlAddress = myBundle.getString("urlAddress");
        urlCaption = myBundle.getString("title");
        title=(TextView)findViewById(R.id.txtTitle);
        title.setText(myBundle.getString("title"));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        logo=(ImageView)findViewById(R.id.logo);
        logo.setBackgroundResource(myBundle.getInt("image"));
// update app’s top ‘TitleBar’ (eg. ‘NPR - Business Wed April 09, 2014’)
        this.setTitle("NPR – " + urlCaption + " \t" + ShowChannels.niceDate());
        myListView = (ListView) this.findViewById(R.id.myListView);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> av, View v, int index, long id) {
                selectedNewsItem = newsList.get(index);
                showNiceDialogBox(selectedNewsItem, getApplicationContext());
            }
        });
// get stories for the selected news option
        DownloadRssFeed downloader = new DownloadRssFeed(ShowHeadlines.this);
        downloader.execute(urlAddress, urlCaption);
    }

    public void showNiceDialogBox(SingleItem selectedStoryItem, Context context) {
// make a nice-looking dialog box (story summary, btnClose, btnMore)
// CAUTION: (check)on occasions title and description are the same!
        String title = selectedStoryItem.getTitle();
        String description = selectedStoryItem.getDescription();
        if (title.toLowerCase().equals(description.toLowerCase())) {
            description = "";
        }
        try {
//CAUTION: sometimes TITLE and DESCRIPTION include HTML markers
            final Uri storyLink = Uri.parse(selectedStoryItem.getLink());
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
//            myBuilder.setTitle(Html.fromHtml(urlCaption))
//                    .setMessage(title + "\n\n" + Html.fromHtml(description) + "\n")
//.setPositiveButton("Close", null)
//                    .setNegativeButton("More", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichOne) {
//                            Intent browser = new Intent(Intent.ACTION_VIEW, storyLink);
//                            startActivity(browser);
//                        }
//                    }) //setNegativeButton
            LayoutInflater inflater	= this.getLayoutInflater();
            View dialogView=inflater.inflate(R.layout.custom_dialog,(ViewGroup) findViewById(R.layout.list_items_activity));
            TextView txtTitle=(TextView)dialogView.findViewById(R.id.txt_title);
            TextView txtDescription=(TextView) dialogView.findViewById(R.id.txt_description);
            Button btnMore=(Button)dialogView.findViewById(R.id.btn_more);
            Button btnClose=(Button)dialogView.findViewById(R.id.btn_close);
            txtTitle.setText(Html.fromHtml(urlCaption));
            String showDescription=title + "\n\n" + Html.fromHtml(description);
            if(showDescription.length()>500){
                showDescription=showDescription.substring(0,500);
                showDescription=showDescription+"..."+"\n";
            }
            else{
                showDescription+="\n";
            }
            txtDescription.setText(showDescription);
            myBuilder.setView(dialogView);
            AlertDialog dialogShow=myBuilder.create();
            dialogShow.show();
            btnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browser = new Intent(Intent.ACTION_VIEW, storyLink);
                    startActivity(browser);
                }
            });
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogShow.dismiss();
                }
            });

        } catch (Exception e) {
            Log.e("Error DialogBox", e.getMessage());
        }
    }//showNiceDialogBox
}//ShowHeadlines