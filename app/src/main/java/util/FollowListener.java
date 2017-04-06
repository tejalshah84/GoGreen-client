package util;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.amanleenpuri.gogreen.ui.GoGreenApplication;

import java.util.HashSet;

import adapter.ProxyUser;
import model.Following;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.remote.GreenRESTInterface;

/**
 * Created by Tejal on 5/4/2016.
 */
public class FollowListener implements CompoundButton.OnCheckedChangeListener {

    private Context callFrom;
    private String userName;
    private int followId;
    private int currId;
    private HashSet<Integer> followlist;

    public FollowListener(Context c, String name, int followId){
        super();
        this.callFrom=c;
        this.userName=name;
        this.followId=followId;
        this.currId= ProxyUser.getInstance().getUserId(c);
    }

    public FollowListener(Context c, String name, int followId, HashSet<Integer> hs){
        super();
        this.callFrom=c;
        this.userName=name;
        this.followId=followId;
        this.currId= ProxyUser.getInstance().getUserId(c);
        this.followlist = new HashSet<Integer>();
        this.followlist=hs;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            // The toggle is enabled
            Toast toast= Toast.makeText(callFrom,
                    "You are now following "+userName, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            setFollowingInfo(Integer.parseInt(String.valueOf(followId)), "Y", buttonView);
        } else {
            // The toggle is disabled
            Toast toast= Toast.makeText(callFrom,
                    "Unfollowing "+userName, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            setFollowingInfo(Integer.parseInt(String.valueOf(followId)), "N", buttonView);
        }
    }

    public void setFollowingInfo(int fid, String status, final View v){
        Following f = new Following(currId, fid, status);
        Log.d("FollowingReq", f.toString());
        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)callFrom.getApplicationContext()).getGoGreenApiService();
        Call<Following> setFollowingCall = greenRESTInterface.setFollowing(f);

        setFollowingCall.enqueue(new Callback<Following>() {
            Following temp;

            @Override
            public void onResponse(Call<Following> call, Response<Following> response) {
                if (response.isSuccessful()) {
                    temp = response.body();
                    if(temp.getStatus().equalsIgnoreCase("X")){
                        if(((ToggleButton)v).isChecked()){
                            Toast toast= Toast.makeText(callFrom,
                                    "Follow operation failed for "+userName, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                        else{
                            Toast toast= Toast.makeText(callFrom,
                                    "Unfollow operation failed for "+userName, Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                        ((ToggleButton)v).toggle();
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

