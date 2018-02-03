package com.androidchallenge.searchpic.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidchallenge.searchpic.R;
import com.squareup.picasso.Picasso;

public class ImageDetail extends AppCompatActivity {

    private ImageView imageView;
    private ImageView backImageView;
    private TextView  pictureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String title =  intent.getStringExtra("title");
       // getWindow().setStatusBarColor(Color.TRANSPARENT);

        backImageView = (ImageView) findViewById(R.id.iv_back);
        imageView     = (ImageView) findViewById(R.id.iv_card_image_detail);
        pictureTextView = (TextView) findViewById(R.id.tv_card_view_item_detail);

        if(title !=null) {
            pictureTextView.setText(title);
        }
        Picasso.with(this).load(image).resize(600,400).centerCrop().into(imageView);


        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickHome();
                //finish();
            }
        });

    }

    protected void onClickHome() {
        super.onBackPressed();
    }


}
