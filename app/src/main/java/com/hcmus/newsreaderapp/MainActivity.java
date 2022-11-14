package com.hcmus.newsreaderapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

/**
 * <h3>Mobile Development</h1>
 * <p><b>Ass8 - News App</b></p>
 * <ol>
 *     <li>20120497 - Nguyen Quang Huy</li>
 *     <li>20120502 - Nguyen Minh Kha</li>
 *     <li>20120545 - Le Hoai Phong</li>
 * </ol>
 *
 * <ul>
 *     <li>Tránh các hàm bị deprecated</li>
 *     <li>Tránh leak memory</li>
 *     <li>Hiện thông báo trang trống khi bản tin rỗng hoặc mất kết nối mạng</li>
 * </ul>
 */

public class MainActivity extends Activity {

    ArrayAdapter<String> adapterMainSubjects;
    GridView mainGridView;

    String[] myUrlCaption = {"VOV", "VTC NEWS", "TUỔI TRẺ", "VNEXPRESS",
            "THANH NIÊN", "NGƯỜI LAO ĐỘNG", "THE NEW YORK TIMES", "VIỆT NAM NEWS", "BUZZFEED"};
    String[] myURL = {"https://vov.vn/rss.html", "https://vtc.vn/main-rss.html",
            "https://tuoitre.vn/rss.htm", "https://vnexpress.net/rss",
            "https://thanhnien.vn/rss.html", "https://nld.com.vn/rss.htm",
            "https://www.nytimes.com/rss", "https://vietnamnews.vn/rss","https://www.buzzfeed.com/rss"};
    int[] imageSource = {R.drawable.logo_vov,  R.drawable.logo_vtcnews, R.drawable.logo_tuoitre,
            R.drawable.logo_vnexpress, R.drawable.logo_thanhnien, R.drawable.logo_nguoilaodong,
            R.drawable.logo_thenewyorktimes, R.drawable.logo_vietnamnews, R.drawable.logo_buzzfeed};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("NEWS APP " + DateUtils.niceDate());

        mainGridView = (GridView) this.findViewById(R.id.gridview_main);
        mainGridView.setOnItemClickListener((_av, _v, _index, _id) -> {
            Intent callShowHeadlines = new Intent(MainActivity.this, ShowChannels.class);
            Bundle myData = new Bundle();
            myData.putString("url", myURL[_index]);
            myData.putString("title", myUrlCaption[_index]);
            myData.putInt("image", imageSource[_index]);
            callShowHeadlines.putExtras(myData);
            startActivity(callShowHeadlines);
        });

        adapterMainSubjects = new ArrayAdapter<>(this, R.layout.my_simple_list_item_2, myUrlCaption);
        mainGridView.setAdapter(adapterMainSubjects);
    }
}
