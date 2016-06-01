package com.mingle.myapplication.research.view;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingle.myapplication.R;
import com.mingle.myapplication.research.model.SampleData;
import com.yalantis.phoenix.PullToRefreshView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


public class NaverSearchActivity extends Activity {
    private static final String TAG = NaverSearchActivity.class.getSimpleName();
    private static final String clientId = "4OUr6eIicnv3_ebCax81";
    private static final String clientSecret = "FqrI3qdVDv";
    private static final String ITEM_VALUE = "value";

    private ArrayList<SampleData> arrayList = new ArrayList<SampleData>();
    private int valueNum;

    private boolean inItem = false, inTitle = false, inLink = false, inImage = false, inLprice = false, inMallName = false;
    String title = null, link = null, Image = null, Lprice = null, MallName = null;

    private PullToRefreshView mPullToRefreshView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);

        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                        arrayList.clear();
                        naverSearchAPI(makeUrl(true));
                    }
                }, 1000);
            }
        });


        naverSearchAPI(makeUrl(false));
    }
    public String makeUrl(boolean isValue){
        Intent intent = getIntent();
        valueNum = intent.getIntExtra(ITEM_VALUE, 0);

        String valueStr = null;
        if(valueNum == 1){
            valueStr = "모자";
        }
        else if(valueNum == 2){
            valueStr = "티셔츠";
        }
        else if(valueNum == 3){
            valueStr = "바지";
        }
        else if(valueNum == 4){
            valueStr = "신발";
        }
        else{
            valueStr = "옷";
        }
        String apiURL = "";
        try {
            String text = URLEncoder.encode(valueStr, "UTF-8");
            if(isValue == false) {
                apiURL = "https://openapi.naver.com/v1/search/shop.xml?query=" + text + "&start=1&display=50&sort=sim";
            }
            else{
                apiURL = "https://openapi.naver.com/v1/search/shop.xml?query=" + text + "&start=1&display=50&sort=asc";
            }
        } catch (Exception e) {
            Log.e(TAG,"Exception : "+e);
        }

        return apiURL;
    }
    public void naverSearchAPI(String apiURL){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

            // response 수신
            int responseCode = con.getResponseCode();
            Log.e(TAG, "responseCode=" + responseCode);
            if (responseCode == 200) {

                XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parserCreator.newPullParser();

                parser.setInput(con.getInputStream(), null);

                int parserEvent = parser.getEventType();

                while (parserEvent != XmlPullParser.END_DOCUMENT){
                    switch(parserEvent){
                        case XmlPullParser.START_TAG:  //parser가 시작 태그를 만나면 실행
                            if(parser.getName().equals("item")){
                                inItem = true;
                            }
                            if(parser.getName().equals("title")){ //title 만나면 내용을 받을수 있게 하자
                                inTitle = true;
                            }
                            if(parser.getName().equals("link")){ //title 만나면 내용을 받을수 있게 하자
                                inLink = true;
                            }
                            if(parser.getName().equals("image")){ //address 만나면 내용을 받을수 있게 하자
                                inImage = true;
                            }
                            if(parser.getName().equals("lprice")){ //mapx 만나면 내용을 받을수 있게 하자
                                inLprice = true;
                            }
                            if(parser.getName().equals("mallName")){ //mapy 만나면 내용을 받을수 있게 하자
                                inMallName = true;
                            }
                            if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                                Log.e(TAG,"에러");
                                //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                            }
                            break;

                        case XmlPullParser.TEXT://parser가 내용에 접근했을때
                            if(inTitle){ //isTitle이 true일 때 태그의 내용을 저장.
                                title = parser.getText();
                                inTitle = false;
                            }
                            if(inLink){ //isAddress이 true일 때 태그의 내용을 저장.
                                link = parser.getText();
                                inLink = false;
                            }
                            if(inImage){ //isAddress이 true일 때 태그의 내용을 저장.
                                Image = parser.getText();
                                inImage = false;
                            }
                            if(inLprice){ //isMapx이 true일 때 태그의 내용을 저장.
                                Lprice = parser.getText();
                                inLprice = false;
                            }
                            if(inMallName){ //isMapy이 true일 때 태그의 내용을 저장.
                                MallName = parser.getText();
                                inMallName = false;
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if(parser.getName().equals("item")){
                                //status1.setText(status1.getText()+"제목 : "+ title + "\n구매링크 : "+ link + "\n이미지 : "+ Image +"\n최저가 : " + Lprice + "\n매장명 : " + MallName+"\n\n");
                                arrayList.add(new SampleData(title, link, Image, Lprice, MallName));
                                inItem = false;
                            }
                            break;
                    }
                    parserEvent = parser.next();
                    ListView listView = (ListView)findViewById(R.id.research_ListView);
                    SampleAdapter sampleAdapter = new SampleAdapter(this);
                    sampleAdapter.setDataList(arrayList);

                    listView.setAdapter(sampleAdapter);
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                }
            } else {
                Log.e(TAG, "API 호출 에러 발생 : 에러코드=" + responseCode);
            }
            for(int i=0;i<arrayList.size();i++){
                Log.e(TAG,"TITLE : "+arrayList.get(i).getTitle());
            }
        } catch (Exception e) {
            Log.e(TAG,"Exception : "+e);
        }
    }

}