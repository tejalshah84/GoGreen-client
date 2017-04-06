package com.example.amanleenpuri.gogreen.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amanleenpuri.gogreen.R;

import adapter.ProxyUser;
import model.GreenEntry;
import util.ImageHelper;
import util.TimeAgoHelper;

/**
 * Created by amanleenpuri on 5/4/16.
 */
public class BlogActivity extends AppCompatActivity {
    static String GREEN_ENTRY_POSTED_BY = "GREEN_ENTRY_POSTED_BY";
    static String GREEN_ENTRY_POSTED_DATE = "GREEN_ENTRY_POSTED_DATE";
    static String GREEN_ENTRY_POSTED_BY_USER_PIC = "GREEN_ENTRY_POSTED_BY_USER_PIC";
    static String GREEN_ENTRY_POST_MSG = "GREEN_ENTRY_POST_MSG";
    static String GREEN_ENTRY_POST_IMG = "GREEN_ENTRY_POST_IMG";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        Intent intent = getIntent();
        String userName = intent.getStringExtra(GREEN_ENTRY_POSTED_BY);
        String postedDate = intent.getStringExtra(GREEN_ENTRY_POSTED_DATE);
        String userPic = intent.getStringExtra(GREEN_ENTRY_POSTED_BY_USER_PIC);
        String postMsg = intent.getStringExtra(GREEN_ENTRY_POST_MSG);
        String postImg = intent.getStringExtra(GREEN_ENTRY_POST_IMG);

        ImageView profilePicIV = (ImageView) findViewById(R.id.iv_profile_pic);
        profilePicIV.setImageBitmap(ImageHelper.getStringToBitMap(userPic));

        ImageView postPicIV = (ImageView) findViewById(R.id.iv_Photo);
        postPicIV.setImageBitmap(ImageHelper.getStringToBitMap(postImg));

        TextView blogMsgTV = (TextView) findViewById(R.id.tv_blogPostMessage);
        blogMsgTV.setText(postMsg);

        TextView userNameTV = (TextView)findViewById(R.id.tv_user_name_on_blog);
        userNameTV.setText(userName);

        TextView timePostedTV = (TextView)findViewById(R.id.tv_time_ago_on_blog);
        timePostedTV.setText(TimeAgoHelper.getTimeAgoForInputString(postedDate));
//
    }
}
