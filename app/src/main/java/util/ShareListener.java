package util;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.amanleenpuri.gogreen.ui.GoGreenApplication;

import adapter.ProxyUser;
import model.Following;
import model.Share;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ws.remote.GreenRESTInterface;

/**
 * Created by Tejal on 5/4/2016.
 */
public class ShareListener implements CompoundButton.OnCheckedChangeListener {

    private Context callFrom;
    private String userName;
    private int pstId;
    private int currId;

    public ShareListener(Context c, String name, int pstId){
        super();
        this.callFrom=c;
        this.userName=name;
        this.pstId=pstId;
        this.currId= ProxyUser.getInstance().getUserId(c);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            Toast toast= Toast.makeText(callFrom,
                    "You Shared "+userName+"'s Post", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
            setShareInfo(buttonView);
        } else {
            buttonView.setChecked(true);
            //TODO: TEST ALL CASES SHADY CODE
            // The toggle is disabled
        }
    }

    public void setShareInfo(final View v){
        Share s = new Share(currId, pstId);
        Log.d("ShareReq", s.toString());
        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)callFrom.getApplicationContext()).getGoGreenApiService();
        Call<Share> setShareCall = greenRESTInterface.setShare(s);

        setShareCall.enqueue(new Callback<Share>() {
            @Override
            public void onResponse(Call<Share> call, Response<Share> response) {
                if (response.isSuccessful()) {
                    Share temp = response.body();
                    if(temp.getPostId()==0 && temp.getUserId()==0){
                        Toast toast= Toast.makeText(callFrom,
                                "Share operation failed for post", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                        ((ToggleButton)v).setChecked(false);
                    }
                } else {
                    Log.e("GetShareData", "Error in response " + response.errorBody());
                }
            }
            @Override
            public void onFailure(Call<Share> call, Throwable t) {
                Log.e("Share", "Failure to get Share Data", t);
            }
        });
    }

}


