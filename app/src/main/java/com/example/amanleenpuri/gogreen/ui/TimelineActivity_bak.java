package com.example.amanleenpuri.gogreen.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.Arrays;
import java.util.Collections;

import java.util.List;
import java.util.Vector;

import adapter.ProxyUser;
import model.GreenEntry;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.UtilNotify;
import ws.remote.GreenRESTInterface;

public class TimelineActivity_bak extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private  int mNotificationsCount = 0;
    ArrayList<model.Notification> noteData;
    ArrayList<GreenEntry> qaData;
    private ArrayList<User> following = null;
    private ArrayList<User> follower = null;
    private GreenEntry[] greenEntryArr;
    private ArrayList<GreenEntry> greenEntryListArray;
    private ArrayList<GreenEntry> bufferGreenEntryListArray;
    private ListView timelinelv;
    private int userId=0;
    private TimeLineListViewAdapter timelineLVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProxyUser pUser = ProxyUser.getInstance();
        String userName = pUser.getUsername(getApplicationContext());
        userId = pUser.getUserId(getApplicationContext());
        System.out.println("*********** userId="+userId);
        System.out.println("*********** userName=" + userName);
        greenEntryListArray = new ArrayList<GreenEntry>();
        bufferGreenEntryListArray = new ArrayList<GreenEntry>();

        if (userName.isEmpty() || userId == 0) {
            Intent i = new Intent(TimelineActivity_bak.this, LoginActivity.class);
            startActivity(i);

        } else {
            setContentView(R.layout.activity_timeline);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                updateNotificationsBadge(extras.getInt("BADGE_COUNT"));
            }


            new FetchCountTask().execute();
            ListView timelinelv = (ListView) findViewById(R.id.list);
            greenEntryListArray = new ArrayList<GreenEntry>();
           // timelineLVAdapter = new TimeLineListViewAdapter(getBaseContext(),greenEntryListArray, userId);
            timelinelv.setAdapter(timelineLVAdapter);

            GreenRESTInterface greenRESTInterface = ((GoGreenApplication) getApplication()).getGoGreenApiService();
            Call<ArrayList<GreenEntry>> getTimeLineCall = greenRESTInterface.getTimeline(1);
            getTimeLineCall.enqueue(new Callback<ArrayList<GreenEntry>>() {
                @Override
                public void onResponse(Call<ArrayList<GreenEntry>> call, Response<ArrayList<GreenEntry>> response) {
                    if (response.isSuccessful()) {
                        ArrayList<GreenEntry> res = response.body();
                        Collections.reverse(res);
                        bufferGreenEntryListArray = res;
                        timelineLVAdapter.addAll(res);

                    } else {
                        Log.e("Timeline", "Error in response " + response.errorBody());
                        Toast toast = Toast.makeText(getApplicationContext(), "Sorry! Could't fetch data!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<GreenEntry>> call, Throwable t) {
                    Log.e("Login", "Failure to authenticate user", t);

                    Toast toast = Toast.makeText(getApplicationContext(), "on failure", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
//        MenuItemCompat.setOnActionExpandListener(searchItem,  new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem menuItem) {
//                // Return true to allow the action view to expand
//                return false;
//            }
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
//                // When the action view is collapsed, reset the query
//               restoreTimeline();
//                // Return true to allow the action view to collapse
//                return true;
//            }
//        });
        //SearchView searchView = (SearchView) searchItem.getActionView();
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
//        System.out.println("MATCHED :: handleIntent");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            restoreTimeline();
//            System.out.println("MATCHED :: search");
            doMySearch(query);
        }
    }

    private void doMySearch(String query) {
        System.out.println("************ in do my search ****************");
        ArrayList<GreenEntry> searchArrayList = new ArrayList<GreenEntry>();
        if(query.equalsIgnoreCase("done")){
            restoreTimeline();
            Toast toast= Toast.makeText(getApplicationContext(),
                    " done is end search", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();

        }else {
            for (int i = 0; i < greenEntryListArray.size(); i++) {
                String postMessage = greenEntryListArray.get(i).getPostMessage();
                if (postMessage.contains(query)) {
                    searchArrayList.add(greenEntryListArray.get(i));
                    System.out.println("MATCHED :: " + postMessage);
                }
            }
        }
        System.out.println("************ searchArrayList.size()="+searchArrayList.size()+" ******** greenEntryListArray="+greenEntryListArray.size());
        if(searchArrayList.size()>0){
            System.out.println("************ 1="+searchArrayList.size()+" ******** greenEntryListArray="+greenEntryListArray.size());
            timelineLVAdapter.clear();
            timelineLVAdapter.addAll(searchArrayList);
            timelineLVAdapter.notifyDataSetChanged();
        }else if(searchArrayList.size()==0) {
            restoreTimeline();

//            System.out.println("************ 2 =" + searchArrayList.size() + " ******** greenEntryListArray=" + greenEntryListArray.size());
//            timelineLVAdapter.clear();
//            greenEntryListArray = bufferGreenEntryListArray;
//            timelineLVAdapter.addAll(greenEntryListArray);
//            timelineLVAdapter.notifyDataSetChanged();
//            System.out.println("************ 2 1 =" + searchArrayList.size() + " ******** greenEntryListArray=" + greenEntryListArray.size());
        }

    }

    public void restoreTimeline(){
        timelineLVAdapter.clear();
        greenEntryListArray = bufferGreenEntryListArray;
        timelineLVAdapter.addAll(greenEntryListArray);
        timelineLVAdapter.notifyDataSetChanged();
        System.out.println("******************** greenEntryListArray=" + greenEntryListArray.size());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
//            Intent intent = new Intent(this, SearchResultsActivity.class);
//            startActivity(intent);
            onSearchRequested();
            return true;
        }
        if (id == R.id.action_notifications) {
            Log.v("AAAAAAAAAAAA","I AM CLICKED");
            updateNotificationsBadge(0);
            noteData=new ArrayList<model.Notification>();
            GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
            Call<List<model.Notification>> getNs = greenRESTInterface.getAllNotifications();
            Log.v("AAAAAAAAAAAA",getNs.toString());
            getNs.enqueue(new Callback<List<model.Notification>>() {

                List<model.Notification> arrN=new ArrayList<model.Notification>();
                @Override
                public void onResponse(Call<List<model.Notification>> call, Response<List<model.Notification>> response) {
                    if (response.isSuccessful()) {

                        arrN = response.body();
                        Log.v("AAAAAAAAAAAA",arrN.toString());
                        for(int i=0;i<arrN.size();i++){

                            noteData.add(arrN.get(i));

                        }
                        Log.v("AAAAAAAAAAAA",noteData.toString());
                        Intent intent = new Intent(TimelineActivity_bak.this, NotificationActivity.class);
                        Bundle extras = new Bundle();
                        extras.putSerializable("NOTIFS",noteData);
                        intent.putExtras(extras);
                        startActivity(intent);
                    } else {
                        Log.e("Signup", "Error in response " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<List<model.Notification>> call, Throwable t) {
                    Log.e("NOTIFICATIONS", "Failure to fetch Notifications", t);
                }
            });

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNotificationsBadge(int count) {
        mNotificationsCount = count;

        // force the ActionBar to relayout its MenuItems.
        // onCreateOptionsMenu(Menu) will be called again.
        invalidateOptionsMenu();
    }

    class FetchCountTask extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... params) {

            int i = CreateEventActivity.count;
            return i;
        }

        @Override
        public void onPostExecute(Integer count) {
            updateNotificationsBadge(count);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        ProxyUser pUser = ProxyUser.getInstance();
        String userName = pUser.getUsername(getApplicationContext());
        int userId = pUser.getUserId(getApplicationContext());
        System.out.println("@@@@@@@ userId="+userId);
        System.out.println("*********** userName="+userName);
        // Handle navigation view item clicks here.

        int id = item.getItemId();
        System.out.println("*********** id="+id);

        if (id == R.id.edit_profile) {
            System.out.println("*********** IN CLICK ************");

            GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
            Call<User> getUserDetailsCall = greenRESTInterface.getUserDetails(userId);
            getUserDetailsCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        User responseUser = response.body();
//                        responseUser.setImageURL(null);
                        Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
                        i.putExtra("USER_DETAILS_OBJECT", responseUser);
                        startActivity(i);
                    } else {
                        Log.e("Timeline", "Error in response " + response.errorBody());
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("Signup", "Failure to create user", t);
                }
            });

        } else if (id == R.id.nav_following) {
            following = new ArrayList<User>();
            follower = new ArrayList<User>();
            getFollowingData(1);

        } else if (id == R.id.nav_mywall) {
            Intent i = new Intent(getApplicationContext(), TimelineActivity_bak.class);
            startActivity(i);

        } else if (id == R.id.event_creation) {
            Intent i = new Intent(getApplicationContext(), CreateEventActivity.class);
            startActivity(i);

        } else if (id == R.id.qa_forum) {
            qaData = new ArrayList<GreenEntry>();

            GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
            Call<List<GreenEntry>> getAllQuestions = greenRESTInterface.getQuestions(2);
            getAllQuestions.enqueue(new Callback<List<GreenEntry>>() {

                List<GreenEntry> arrGE=new ArrayList<GreenEntry>();
                @Override
                public void onResponse(Call<List<GreenEntry>> call, Response<List<GreenEntry>> response) {
                    if (response.isSuccessful()) {

                        arrGE = response.body();
                        for(int i=0;i<arrGE.size();i++){
                            qaData.add(arrGE.get(i));

                        }


                        Intent i = new Intent(getApplicationContext(), QuestionForumActivity.class);
                        Bundle extras = new Bundle();
                        extras.putSerializable("Q_DATA",qaData);
                        //extras.putSerializable("QA_HASH",qaHash);
                        i.putExtras(extras);
                        startActivity(i);
                    } else {
                        Log.e("Signup", "Error in response " + response.errorBody());
                    }
                }

                @Override
                public void onFailure(Call<List<GreenEntry>> call, Throwable t) {
                    Log.e("QA_Forum", "Failure to fetch Questions", t);
                }
            });

        } /*else if (id == R.id.activate_notification) {
            // define sound URI, the sound to be played when there's a notification
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), soundUri);
            r.play();

            // intent triggered, you can add other intent for other actions
            Intent intent = new Intent(TimelineActivity.this, NotificationActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(TimelineActivity.this, 0, intent, 0);

            // this is it, we'll build the notification!
            // in the addAction method, if you don't want any icon, just set the first param to 0
            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_cast_light, "Previous", pIntent).build();
            Notification mNotification = new Notification.Builder(this)
                    .setContentTitle("New Post!")
                    .setContentText("There is an event for you on GoGreen!")
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

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void getFollowingData(final int opId) {
        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
        int currUserId = ProxyUser.getInstance().getUserId(getApplicationContext());
        Call<ArrayList<User>> getFollowingCall = greenRESTInterface.getFollowingDetails(currUserId,opId);

        getFollowingCall.enqueue(new Callback<ArrayList<User>>() {
            ArrayList<User> temp = new ArrayList<User>();
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()) {
                    if(opId==1 && response.body()!=null) {
                        temp = response.body();
                        for(int i=0; i<temp.size();i++){
                            following.add(temp.get(i));
                        }
                        getFollowingData(2);
                    }
                    else if(opId==2 && response.body()!=null) {
                        temp = response.body();
                        for(int i=0; i<temp.size();i++){
                            follower.add(temp.get(i));
                        }
                        Intent i = new Intent(getApplicationContext(), FollowingActivity.class);
                        Bundle extras = new Bundle();
                        extras.putSerializable("FOLLOWING",following);
                        extras.putSerializable("FOLLOWER",follower);
                        i.putExtras(extras);
                        startActivity(i);
                    }
                } else {
                    Log.e("GetFollowingData", "Error in response " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("Signup", "Failure to get Following Data", t);
            }
        });
    }


}
