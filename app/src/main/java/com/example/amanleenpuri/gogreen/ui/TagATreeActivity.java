package com.example.amanleenpuri.gogreen.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.amanleenpuri.gogreen.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by amrata on 4/18/16.
 */
public class TagATreeActivity extends AppCompatActivity {
    ImageView ivTree;
    Spinner sp1;
    Spinner sp2;
    Spinner sp3;
    Button tagB;
    Bitmap _bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_a_tree_layout);

        ivTree= (ImageView) findViewById(R.id.iv_tree);
        sp1= (Spinner) findViewById(R.id.spinner1);
        sp2= (Spinner) findViewById(R.id.spinner2);
        sp3= (Spinner) findViewById(R.id.spinner3);
        tagB= (Button) findViewById(R.id.tagButton);


        if(getIntent().hasExtra("byteArray")) {
            //ivTree= new ImageView(this);
            _bitmap = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            ivTree.setImageBitmap(_bitmap);
        }

        setUpSpinner();
        if (tagB != null) {
            tagB.setOnClickListener(new View.OnClickListener() {
                String tagVal1 = sp1.getSelectedItem().toString();
                String tagVal2 = sp2.getSelectedItem().toString();
                String tagVal3 = sp3.getSelectedItem().toString();
                String allTag = tagVal1+"\n"+tagVal2+"\n"+tagVal3;


                @Override
                public void onClick(View v) {
                    Bitmap mutableBitmap = _bitmap.copy(Bitmap.Config.ARGB_8888, true);
                    Canvas canvas = new Canvas(mutableBitmap);

                    Paint paint = new Paint();
                    paint.setColor(Color.BLUE);
                    paint.setTextSize(11);
                    //paint.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    //paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                    paint.setFakeBoldText(true);
                    paint.setTextAlign(Paint.Align.CENTER);
                    int xPos = (canvas.getWidth() / 2);
                    int yPos = (int) ((canvas.getHeight() / 1.3) - ((paint.descent() + paint.ascent()) / 2)) ;
                    //((textPaint.descent() + textPaint.ascent()) / 2) is the distance from the baseline to the center.

                    //canvas.drawText("Hello", xPos, yPos, paint);
                    canvas.drawText(tagVal1, xPos, yPos, paint);
                    canvas.drawText(tagVal2, xPos, yPos+10, paint);
                    canvas.drawText(tagVal3, xPos, yPos+20, paint);
                    //Bitmap bmp =drawTextToBitmap(TagATreeActivity.this,R.id.iv_tree,allTag);
                    //Bitmap bmp = null;
                    Intent intent = new Intent(TagATreeActivity.this, BlogTagAskActivity.class);
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    mutableBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);
                    intent.putExtra("imgArray", bs.toByteArray());
                    startActivity(intent);
                }
            });

        }



    }

    public Bitmap drawTextToBitmap(Context mContext, int resourceId, String mText) {
        try {
            Resources resources = mContext.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceId);

            android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
            // set default bitmap config if none
            if(bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }
            // resource bitmaps are imutable,
            // so we need to convert it to mutable one
            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(110,110, 110));
            // text size in pixels
            paint.setTextSize((int) (12 * scale));
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(mText, 0, mText.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width())/6;
            int y = (bitmap.getHeight() + bounds.height())/5;

            canvas.drawText(mText, x * scale, y * scale, paint);

            return bitmap;
        } catch (Exception e) {
            return null;
        }

    }


    public void setUpSpinner(){
        String[] itemsSP1 = new String[]{"Indoor", "Outdoor"};
        ArrayAdapter<String> adapterSP1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsSP1);
        sp1.setAdapter(adapterSP1);

        String[] itemsSP2 = new String[]{"Mango", "Apple", "Avacado", "MoneyPlant", "LuckyBamboo"};
        ArrayAdapter<String> adapterSP2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsSP2);
        sp2.setAdapter(adapterSP2);

        String[] itemsSP3 = new String[]{"Mangifera indica", "Malus domestica", "Persea americana","Epipremnum aureum","Dracaena braunii"};
        ArrayAdapter<String> adapterSP3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemsSP3);
        sp3.setAdapter(adapterSP3);



    }

}
