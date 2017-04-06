package com.example.amanleenpuri.gogreen.ui;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;

import java.util.ArrayList;

/**
 * Created by Kumaril on 4/5/2016.
 */
public class SearchResultsActivity extends AppCompatActivity {

    ImageView cancel, search;
    EditText searchQuery;
    ArrayList<Person> searchResults;
    ListView searchList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);
        cancel=(ImageView) findViewById(R.id.search_cancel);
        search=(ImageView) findViewById(R.id.searchpage_icon);
        searchQuery=(EditText)findViewById(R.id.search_text);
        searchList=(ListView)findViewById(R.id.search_list);
        searchResults = new ArrayList<Person>();
        searchList.setAdapter(new SearchListViewAdapter(this,searchResults));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchResultsActivity.this, TimelineActivity.class);
                startActivity(intent);
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchSearchResults(searchQuery.getText().toString());
            }
        });
    }

    private void fetchSearchResults(String s){
        searchResults = new ArrayList<Person>();
        searchResults.add(new Person("Amrata",R.mipmap.profilepic));
        searchResults.add(new Person("Amanleen",R.mipmap.profilepic));
        searchResults.add(new Person("Amar",R.mipmap.profilepic));
        searchResults.add(new Person("Aman", R.mipmap.profilepic));
        searchResults.add(new Person("Amandeep", R.mipmap.profilepic));
        searchList.setAdapter(new SearchListViewAdapter(this,searchResults));
    }

    private class Person{
        String name;
        int imageId;

        protected Person(){}

        protected Person(String n, int img){
            name=n;
            imageId=img;
        }
    }

    private class SearchListViewAdapter extends ArrayAdapter<Person> {

        SearchListViewAdapter(Context context, ArrayList<Person> list){
            super(context, android.R.layout.simple_list_item_1,list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Person p = getItem(position);
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
            }
            TextView userNameSearchList = (TextView)convertView.findViewById(R.id.username_search);
            ImageView userProfilePicSearchList = (ImageView)convertView.findViewById(R.id.user_image_search);

            userNameSearchList.setText(p.name);
            userProfilePicSearchList.setImageResource(p.imageId);

            ImageView followIcon = (ImageView) convertView.findViewById(R.id.become_follower_icon);
            followIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "You are following now", Toast.LENGTH_LONG).show();
                }
            });
            //Picasso.with(getContext()).load(p.getImageUrl()).placeholder(R.color.colorPrimary).into(userProfilePicFollowList);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), TimelineActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }

}