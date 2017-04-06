package util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.example.amanleenpuri.gogreen.ui.CreateEventActivity;

/**
 * Created by amrata on 4/21/16.
 */

public class TouchableWrapper extends FrameLayout {

    public TouchableWrapper(Context context) {
        super(context);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                CreateEventActivity.mMapIsTouched = true;
                Log.v("%%%%%%%%%%%%%%%","MAP IS TOUCHED");
                //Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345"));
                /*Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("google.navigation:q=Nasa Ames Research Park, Moutain View, California"));
                getContext().startActivity(intent);*/
                break;

            case MotionEvent.ACTION_UP:
                CreateEventActivity.mMapIsTouched = false;
                Log.v("%%%%%%%%%%%%%%%","MAP IS NOT TOUCHED");
                break;
        }
        return super.dispatchTouchEvent(event);
    }
}