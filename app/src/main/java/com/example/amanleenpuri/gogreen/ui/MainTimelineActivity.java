package com.example.amanleenpuri.gogreen.ui;

/**
 * Created by amrata on 4/27/16.
 */

import adapter.ProxyUser;
import model.FeedItem;
import util.UtilNotify;
import util.VolleyAppController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.amanleenpuri.gogreen.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainTimelineActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final String TAG = MainTimelineActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
    private int mNotificationsCount = 0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ENSURE USER's DETAILS ARE CACHED
        ProxyUser pUser = ProxyUser.getInstance();
        String userName = pUser.getUsername(getApplicationContext());
        if(userName.isEmpty()){
            Intent i = new Intent(MainTimelineActivity.this, LoginActivity.class);
            startActivity(i);
        }else {
            setContentView(R.layout.activity_timeline);
            Toast toast = Toast.makeText(getApplicationContext(), "USER_NAME = " + userName, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

            listView = (ListView) findViewById(R.id.list);

            feedItems = new ArrayList<FeedItem>();

            listAdapter = new FeedListAdapter(this, feedItems);
            listView.setAdapter(listAdapter);

            // These two lines not needed,
            // just to get the look of facebook (changing background color & hiding the icon)
            //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
            //getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

            // We first check for cached request
            Cache cache = VolleyAppController.getInstance().getRequestQueue().getCache();
            Entry entry = cache.get(URL_FEED);
            if (entry != null) {
                // fetch the data from cache
                try {
                    String data = new String(entry.data, "UTF-8");
                    try {
                        parseJsonFeed(new JSONObject(data));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else {
                // making fresh volley request and getting json
                JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                        URL_FEED, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.d(TAG, "Response: " + response.toString());
                        if (response != null) {
                            parseJsonFeed(response);
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });

                // Adding request to volley request queue
                VolleyAppController.getInstance().addToRequestQueue(jsonReq);
            }

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            new FetchCountTask().execute();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    //      .setAction("Action", null).show();
                    Intent i = new Intent(getApplicationContext(), BlogTagAskActivity.class);
                    startActivity(i);

                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");

            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);

                FeedItem item = new FeedItem();
                item.setId(feedObj.getInt("id"));
                item.setName(feedObj.getString("name"));

                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                item.setImge(image);
                item.setStatus(feedObj.getString("status"));
                item.setProfilePic(feedObj.getString("profilePic"));
                item.setTimeStamp(feedObj.getString("timeStamp"));

                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                item.setUrl(feedUrl);

                feedItems.add(item);
            }

            // notify data changes to list adapater
            listAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);

        MenuItem item = menu.findItem(R.id.action_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        UtilNotify.setBadgeCount(this, icon, mNotificationsCount);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_notifications) {
            Intent intent = new Intent(this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    class FetchCountTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            // example count. This is where you'd
            // query your data store for the actual count.
            return 5;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_following) {
            Intent i = new Intent(getApplicationContext(), FollowingActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_mywall) {
            Intent i = new Intent(getApplicationContext(), TimelineActivity.class);
            startActivity(i);

        } else if (id == R.id.event_creation) {
            Intent i = new Intent(getApplicationContext(), CreateEventActivity.class);
            startActivity(i);

        } else if (id == R.id.action_notifications) {
            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
            r.play();

            // intent triggered, you can add other intent for other actions
            Intent intent = new Intent(MainTimelineActivity.this, NotificationActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(MainTimelineActivity.this, 0, intent, 0);

            // this is it, we'll build the notification!
            // in the addAction method, if you don't want any icon, just set the first param to 0
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_cast_light, "Previous", pIntent).build();
            Notification mNotification = new Notification.Builder(this)

                    .setContentTitle("New Post!")
                    .setContentText("There is something new for you on GoGreen!")
                    .setSmallIcon(R.drawable.ic_cast_light)
                    .setContentIntent(pIntent)
                    .setSound(soundUri)
                    //.addAction(action)
                    .build();


            mNotification.defaults |= Notification.DEFAULT_VIBRATE;
            //use the above default or set custom valuse as below
            long[] vibrate = {0, 200, 100, 200};
            mNotification.vibrate = vibrate;

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            // If you want to hide the notification after it was selected, do the code below
            // myNotification.flags |= Notification.FLAG_AUTO_CANCEL;

            notificationManager.notify(0, mNotification);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}