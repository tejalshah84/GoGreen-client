package com.example.amanleenpuri.gogreen.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.example.amanleenpuri.gogreen.R;

import java.util.ArrayList;

import adapter.ProxyUser;
import model.User;


/**
 * Created by Tejal on 4/4/2016.
 */
public class FollowingActivity extends AppCompatActivity{

    private ArrayList<User> following = new ArrayList<User>();
    private ArrayList<User> follower = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = new Bundle();
        Bundle extras = getIntent().getExtras();
        following = (ArrayList<User>) extras.getSerializable("FOLLOWING");
        follower = (ArrayList<User>) extras.getSerializable("FOLLOWER");

        setContentView(R.layout.following_layout);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), FollowingActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Followers", "Following"};
        private Context context;

        public MyFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


        @Override
        public Fragment getItem(int position) {
            if(position==0){
                return FollowingFragment.newInstance(1, follower);
            }
            else if(position==1){
                return FollowingFragment.newInstance(2, following);
            }
            return FollowingFragment.newInstance(1, follower);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }
}
