package com.androidchallenge.searchpic.network;

import com.androidchallenge.searchpic.model.CardItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by waghup on 1/31/18.
 */

public class FetchImage {
    private static int API_TIMEOUT_IN_SECONDS = 5;
    private static CardItem cardItem;
    private static String IMGUR_URL="https://api.imgur.com/3/gallery/search/time/";
    private static String SEARCH_PARAM = "q";
    private static ArrayList<CardItem> arrayListPictInfo ;


    public static ArrayList<CardItem> getImageData(String searchParam,int pageNo) {

        arrayListPictInfo = new ArrayList<>();
        String newSearchParam = "?q="+searchParam;

        Request request = new Request.Builder()
                .url(IMGUR_URL+pageNo+newSearchParam)
                .addHeader("Authorization", "Client-ID 126701cd8332f32")
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(API_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(API_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .readTimeout(API_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            JSONObject jsonObject = new JSONObject(response.body().string());
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0;i<jsonArray.length();i++){

                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
              //  String imageUrl = jsonObject1.getString("link");
              //  String title = jsonObject1.getString("title");
             //   cardItem = new CardItem(title, imageUrl);
             //   arrayListPictInfo.add(cardItem);

                if (!jsonObject1.isNull("images")){

                    JSONArray imageArray = jsonObject1.getJSONArray("images");
                    for (int j = 0;j<imageArray.length();j++) {
                        JSONObject jsonObject2 = imageArray.getJSONObject(j);
                        String imageUrl = jsonObject2.getString("link");

                        if(imageUrl.contains("jpg")|imageUrl.contains("png")) {
                            cardItem = new CardItem(title, imageUrl);
                            arrayListPictInfo.add(cardItem);
                        }
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return arrayListPictInfo;
    }


}
