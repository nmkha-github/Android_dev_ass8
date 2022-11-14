package com.hcmus.newsreaderapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DownloadRssFeed extends AsyncTask<String, Void, ArrayList<SingleItem>> {

    // Use weak reference to avoid leaking context memory
    WeakReference<ShowHeadlines> callerContext;
    String urlAddress, urlCaption;

    public DownloadRssFeed(WeakReference<ShowHeadlines> callerContext) {
        this.callerContext = callerContext;
    }

    protected void onPreExecute() {
        ShowHeadlines activity = callerContext.get();
        activity.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.textview_msg).setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(ArrayList<SingleItem> result) {
        super.onPostExecute(result);

        ShowHeadlines activity = callerContext.get();
        activity.newsList = result;

        if (result == null || result.size() == 0)
            activity.findViewById(R.id.textview_empty).setVisibility(View.VISIBLE);
        else
            activity.findViewById(R.id.textview_empty).setVisibility(View.GONE);

        activity.findViewById(R.id.progress_bar).setVisibility(View.GONE);
        activity.findViewById(R.id.textview_msg).setVisibility(View.GONE);

        int layoutID = R.layout.my_simple_list_item_1;
        ArrayAdapter<SingleItem> adapterNews = new ArrayAdapter<>(activity, layoutID, result);
        activity.mListView.setAdapter(adapterNews);

    }

    public SingleItem dissectItemNode(NodeList nodeList, int i) {
        try {
            Element entry = (Element) nodeList.item(i);
            Element title = (Element) entry.getElementsByTagName("title").item(0);
            Element description = (Element) entry.getElementsByTagName("description").item(0);
            Element pubDate = (Element) entry.getElementsByTagName("pubDate").item(0);
            Element link = (Element) entry.getElementsByTagName("link").item(0);

            String titleValue = title.getFirstChild().getNodeValue();
            String descriptionValue = description.getFirstChild().getNodeValue();
            String dateValue = pubDate.getFirstChild().getNodeValue();
            String linkValue = link.getFirstChild().getNodeValue();

            return new SingleItem(dateValue, titleValue, descriptionValue, linkValue);
        } catch (DOMException e) {
            return new SingleItem("", "Error", e.getMessage(), null);
        }
    }

    @Override
    protected ArrayList<SingleItem> doInBackground(String... params) {
        ArrayList<SingleItem> newsList = new ArrayList<>();
        urlAddress = params[0];
        urlCaption = params[1];
        try {
            URL url = new URL(urlAddress);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();

                Document dom = db.parse(in);
                Element treeElements = dom.getDocumentElement();

                NodeList itemNodes = treeElements.getElementsByTagName("item");
                if ((itemNodes != null) && (itemNodes.getLength() > 0)) {
                    for (int i = 0; i < itemNodes.getLength(); i++) {
                        newsList.add(dissectItemNode(itemNodes, i));
                    }
                }
            }

            httpConnection.disconnect();
        } catch (Exception e) {
            Log.e("Error>> ", e.getMessage());
        }
        return newsList;
    }
}
