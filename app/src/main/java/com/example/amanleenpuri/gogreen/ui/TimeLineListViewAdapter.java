package com.example.amanleenpuri.gogreen.ui;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.example.amanleenpuri.gogreen.R;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import model.Following;
import model.GreenEntry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.FollowListener;
import util.ImageHelper;
import util.ShareListener;
import util.TimeAgoHelper;
import ws.remote.GreenRESTInterface;

/**
 * Created by amanleenpuri on 4/3/16.
 */
public class TimeLineListViewAdapter extends ArrayAdapter<GreenEntry> {

    private int userId=0;
    Context context;
    private HashSet<Integer> followlist;


    TimeLineListViewAdapter(Context context, ArrayList<GreenEntry> list, int userId, HashSet<Integer> hs){
        super(context, android.R.layout.simple_list_item_1,list);
        this.context = context;
        this.userId = userId;
        this.followlist = hs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final GreenEntry ge = getItem(position);
        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timeline_list_item, parent, false);
        }
        TextView userNameOnTimeLinetv = (TextView)convertView.findViewById(R.id.tv_user_name_on_timeline);
        TextView timeAgoOnTimeLinetv = (TextView)convertView.findViewById(R.id.tv_time_ago_on_timeline);
        final TextView numberOfStarstv = (TextView)convertView.findViewById(R.id.tv_number_of_stars_on_timeline);
        ImageView userProfilePicOnTimeLineiv = (ImageView)convertView.findViewById(R.id.iv_timeline_image);
        ImageView articleImageiv = (ImageView)convertView.findViewById(R.id.iv_article_on_timeline);
        ToggleButton followIcon = (ToggleButton) convertView.findViewById(R.id.become_follower_icon);
        final ToggleButton sharePostIcon = (ToggleButton) convertView.findViewById(R.id.share_post_icon);
        ToggleButton starIcon = (ToggleButton) convertView.findViewById(R.id.star_icon);
        ExpandableTextView postMessage = (ExpandableTextView)convertView.findViewById(R.id.tv_postMsg);

        if(ge.getPostMessage()!=null) {
            postMessage.setText(ge.getPostMessage());
        }
        if(ge.getPostImageURL()!=null) {
            articleImageiv.setImageBitmap(ImageHelper.getStringToBitMap(ge.getPostImageURL()));
        }

        if(ge.getUserImage()!=null){
            userProfilePicOnTimeLineiv.setImageBitmap(ImageHelper.getStringToBitMap(ge.getUserImage()));
        }
        timeAgoOnTimeLinetv.setText(TimeAgoHelper.getTimeAgoForInputString(ge.getDatePosted()));

        //TODO: ADD CONDITION TO SET START ICON STATE
//        starIcon.setChecked(true);
        userNameOnTimeLinetv.setText(ge.getPostByUserName());

        numberOfStarstv.setText(String.valueOf(ge.getNumOfStars()));
//        starIcon.setAlpha(0.5f);
        starIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int count = Integer.valueOf(numberOfStarstv.getText().toString());
                if (isChecked) {
                    // The toggle is enabled
                    count++;
                    numberOfStarstv.setText(String.valueOf(count));
                } else {
                    // The toggle is disabled
                    count--;
                    numberOfStarstv.setText(String.valueOf(count));
                }
            }
        });

        final View finalConvertView = convertView;

        if(userId==ge.getPostedByUserId() && !(followlist.contains(ge.getPostedByUserId()))){
            followIcon.setVisibility(View.INVISIBLE);
        }else{
            followIcon.setVisibility(View.VISIBLE);
            if(followlist.contains(ge.getPostedByUserId())){
                followIcon.setChecked(true);
                System.out.println("^^^^^^^^^^ SET VISIBLE TRUE ^^^^^^^^^^^^");
            }
            else{
                followIcon.setChecked(false);
                System.out.println("^^^^^^^^^^ SET VISIBLE  FALSE^^^^^^^^^^^^");
            }
            System.out.println("********* ge.getPostByUserName(=" + ge.getPostByUserName() + " ******* followlist=" + followlist);
//            followIcon.setOnCheckedChangeListener( new FollowListener(getContext(),ge.getPostByUserName(),ge.getPostedByUserId(),followlist));
            followIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    System.out.println("********* INSIDE onCheckedChanged ***********");
                    if (isChecked) {
                        System.out.println("********* INSIDE IS CHECKED ***********");
                        // The toggle is enabled
                        setFollowingInfo(ge.getPostedByUserId(), "Y", context);
                        Toast toast= Toast.makeText(finalConvertView.getContext(),
                                "You are now following "+ge.getPostByUserName(), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        buttonView.setVisibility(View.INVISIBLE);
                    } else {
//                        System.out.println("********* INSIDE ELSE CHKD ***********");
//                        // The toggle is disabled
//                        setFollowingInfo(ge.getPostedByUserId(), "N", context);
//                        Toast toast= Toast.makeText(finalConvertView.getContext(),
//                                "Unfollowing "+ge.getPostByUserName(), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                        toast.show();
                    }
                }
            });
        }

        sharePostIcon.setOnCheckedChangeListener( new ShareListener(getContext(),ge.getPostByUserName(),ge.getPostId()));

//        if(userId==ge.getPostedByUserId()){
//            followIcon.setVisibility(View.INVISIBLE);
//        }else{
//            followIcon.setVisibility(View.VISIBLE);
//            followIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        // The toggle is enabled
//                        Toast toast= Toast.makeText(finalConvertView.getContext(),
//                                "You are now following "+ge.getPostByUserName(), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                        toast.show();
//                    } else {
//                        // The toggle is disabled
//                        Toast toast= Toast.makeText(finalConvertView.getContext(),
//                                "Unfollowing "+ge.getPostByUserName(), Toast.LENGTH_SHORT);
//                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                        toast.show();
//                    }
//                }
//            });
//        }
//
//        sharePostIcon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    // The toggle is enabled
//                    Toast toast= Toast.makeText(finalConvertView.getContext(),
//                            "You Shared "+ge.getPostByUserName()+" Post", Toast.LENGTH_SHORT);
//                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
//                    toast.show();
//                } else {
//                    sharePostIcon.setChecked(true);
//                    //TODO: TEST ALL CASES SHADY CODE
//                    // The toggle is disabled
//
//                }
//            }
//        });
        return convertView;
    }

    public void setFollowingInfo(int fid, String status, Context context){

        Following f = new Following(userId, fid, status);
        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)context.getApplicationContext()).getGoGreenApiService();
        Call<Following> setFollowingCall = greenRESTInterface.setFollowing(f);

        setFollowingCall.enqueue(new Callback<Following>() {
            Following temp;

            @Override
            public void onResponse(Call<Following> call, Response<Following> response) {
                if (response.isSuccessful()) {
                    temp = response.body();
                    if(temp.getStatus().equalsIgnoreCase("X")){
                        //TODO: SHOW IT FAILED
                    }
                    else{
                        if(temp.getStatus().equalsIgnoreCase("Y")){
                            System.out.println("*************INSIDE Y*************");
                            followlist.add(Integer.valueOf(temp.getFollowId()));
                        }
                        else{
                            System.out.println("*************INSIDE ELSE*************");
                            followlist.remove(Integer.valueOf(temp.getFollowId()));
                        }
                    }
                } else {
                    Log.e("GetFollowingData", "Error in response " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Following> call, Throwable t) {
                Log.e("Signup", "Failure to get Following Data", t);
            }
        });
    }

}