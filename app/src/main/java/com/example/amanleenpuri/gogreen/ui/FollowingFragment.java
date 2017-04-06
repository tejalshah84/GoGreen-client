package com.example.amanleenpuri.gogreen.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.example.amanleenpuri.gogreen.R;

import java.util.ArrayList;
import java.util.HashMap;

import adapter.ProxyUser;
import model.User;
import util.FollowListener;
import util.ImageHelper;


/**
 * Created by Tejal.
 */
public class FollowingFragment  extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private String pkgName;
    private ArrayList<User> followData;
    private int currId;
    private HashMap<Integer,ToggleButton> followIconMap =new HashMap<Integer,ToggleButton>();

    public static FollowingFragment newInstance(int page, ArrayList<User> followlist) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable("FOLLOW_DATA", followlist);
        FollowingFragment fragment = new FollowingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        followData = (ArrayList<User>)getArguments().getSerializable("FOLLOW_DATA");
        pkgName = getContext().getPackageName();
        currId = ProxyUser.getInstance().getUserId(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.following_page, container, false);
        ListView followListView= (ListView)view.findViewById(R.id.lv_following);
        followListView.setAdapter(new FollowListViewAdapter(getContext(), followData));
        return view;
    }

    /*private void getFollowData(){
        ProcessUser pu = new ProcessUser(getContext());
        if(mPage==1){
            followData = pu.fetchFollowers();
        }
        else if(mPage==2){
            followData = pu.fetchFollowing();
        }
    }*/

    public Bitmap getStringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    private class FollowListViewAdapter extends ArrayAdapter<User> {

        FollowListViewAdapter(Context context, ArrayList<User> list){
            super(context, android.R.layout.simple_list_item_1,list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            User p = getItem(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.following_item, parent, false);
            }
            final TextView userNameFollowList = (TextView)convertView.findViewById(R.id.username_follow);
            final TextView userIdFollowList = (TextView) convertView.findViewById(R.id.userIdInfo);
            TextView roleFollowList = (TextView)convertView.findViewById(R.id.role_follow);
//            ImageView userProfilePicFollowList = (ImageView)convertView.findViewById(R.id.user_image_follow);
            ToggleButton followIcon = (ToggleButton) convertView.findViewById(R.id.become_follower_icon);

            userIdFollowList.setText(String.valueOf(p.getUserId()));
            userNameFollowList.setText(p.getFirstName() + " " + p.getLastName());
            roleFollowList.setText(p.getRoleType());

//            userProfilePicFollowList.setImageBitmap(getStringToBitMap(p.getImageURL()));
            //userProfilePicFollowList.setImageResource(R.mipmap.profilepic);

//            if(p.getImageURL()!=null){
//                userProfilePicFollowList.setImageBitmap(ImageHelper.getStringToBitMap(p.getImageURL()));
//            }
//            else{
//                userProfilePicFollowList.setImageResource(R.mipmap.profilepic);
//            }

            if(mPage==2){
                followIcon.setVisibility(View.VISIBLE);
                followIcon.setChecked(true);
                followIcon.setOnCheckedChangeListener(new FollowListener(getContext(), p.getFirstName(), p.getUserId()));
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainTimelineActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}