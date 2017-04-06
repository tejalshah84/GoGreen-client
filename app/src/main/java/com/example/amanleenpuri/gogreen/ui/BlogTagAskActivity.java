package com.example.amanleenpuri.gogreen.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amanleenpuri.gogreen.R;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

import adapter.ProxyUser;
import model.GreenEntry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.ImageHelper;
import util.ImagePicker;
import ws.remote.GreenRESTInterface;

/**
 * Created by amrata on 4/3/16.
 */
public class BlogTagAskActivity extends AppCompatActivity implements View.OnClickListener {
    private String blogPost;
    private AlertDialog levelDialog;
    private String blogType;
    private int userId;
    Bitmap _bm;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PICK_IMAGE_FOR_AVATAR = 0;
    private String IMAGE_BIT_MAP_IN_STRING = "";
    private ImageView imageView ;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProxyUser pUser = ProxyUser.getInstance();
        userId = pUser.getUserId(getApplicationContext());
        if (userId == 0) {
            Intent i = new Intent(BlogTagAskActivity.this, LoginActivity.class);
            startActivity(i);
        }else{
            res = getResources();
            setContentView(R.layout.blog_tag_ask_layout);
            imageView = (ImageView) findViewById(R.id.iv_Photo);
            Button btnBlogPost = (Button) findViewById(R.id.btn_blogPost);
            final EditText blogMsgET = (EditText) findViewById(R.id.editTextBlog);

              if(getIntent().hasExtra("imgArray")) {
                  imageView.setVisibility(View.VISIBLE);
                _bm = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("imgArray"),0,getIntent().getByteArrayExtra("imgArray").length);		              _bm = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("imgArray"),0,getIntent().getByteArrayExtra("imgArray").length);
                  IMAGE_BIT_MAP_IN_STRING = ImageHelper.getBitMapToString(_bm);
                  imageView.setImageBitmap(_bm);
            }



            ImageButton cameraBtn = (ImageButton)findViewById(R.id.btn_camera);
            cameraBtn.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    clickpic();
                }
            });

            ImageButton uploadImageBtn = (ImageButton)findViewById(R.id.btn_uploadImage);
            uploadImageBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    upload();
                }
            });

            btnBlogPost.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    blogPost = blogMsgET.getText().toString();
                    if (!blogPost.isEmpty() || (imageView.getDrawable()) != null) {
                        final CharSequence[] items = {" This is a Question ", " This is a Blog "};
                        // Creating and Building the Dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(BlogTagAskActivity.this);
                        builder.setTitle("Please confirm");
                        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        blogType = "Question";
                                        makeGreenEntry(blogType);
                                        break;
                                    case 1:
                                        blogType = "Blog";
                                        makeGreenEntry(blogType);
                                        break;
                                }
                                levelDialog.dismiss();
                            }
                        });
                        levelDialog = builder.create();
                        levelDialog.show();
                    }
                }
            });
        }
    }


    private void makeGreenEntry(String postType) {
        GreenEntry greenEntry = new GreenEntry();
        greenEntry.setPostedByUserId(userId);
        greenEntry.setPostType(postType);
        greenEntry.setPostMessage(blogPost);
        greenEntry.setPostImageURL(IMAGE_BIT_MAP_IN_STRING);

        GreenRESTInterface greenRESTInterface = ((GoGreenApplication)getApplication()).getGoGreenApiService();
        Call<GreenEntry> createGreenEntryCall = greenRESTInterface.createGreenEntry(greenEntry);
        createGreenEntryCall.enqueue(new Callback<GreenEntry>() {
            @Override
            public void onResponse(Call<GreenEntry> call, Response<GreenEntry> response) {
                if (response.isSuccessful()) {
                    GreenEntry res = response.body();
                    Toast toast= Toast.makeText(getApplicationContext(), "Your Post has been published!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    Intent i = new Intent(BlogTagAskActivity.this, TimelineActivity.class);
                    startActivity(i);
                } else {
                    Log.e("BlogActivity", "Error in response " + response.errorBody());
                    Toast toast= Toast.makeText(getApplicationContext(), "Sorry! Post could not be created!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                }
            }
            @Override
            public void onFailure(Call<GreenEntry> call, Throwable t) {
                Log.e("Login", "Failure to create post", t);
                Toast toast= Toast.makeText(getApplicationContext(), "Failure to create post", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            final Bitmap imageBitmap = (Bitmap) extras.get("data");
            IMAGE_BIT_MAP_IN_STRING = ImageHelper.getBitMapToString(imageBitmap);
            imageView.setImageBitmap(imageBitmap);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent tagIntent = new Intent(BlogTagAskActivity.this, TagATreeActivity.class);
                    Bitmap bm = imageBitmap; // your bitmap
                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 50, bs);
                    tagIntent.putExtra("byteArray", bs.toByteArray());
                    startActivity(tagIntent);
                }
            });
        }
        if (requestCode == PICK_IMAGE_FOR_AVATAR && resultCode == RESULT_OK) {
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                try {
                    final Bitmap imageBitmap = getBitmapFromUri(uri);
                    IMAGE_BIT_MAP_IN_STRING = ImageHelper.getBitMapToString(imageBitmap);
                    imageView.setImageBitmap(imageBitmap);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent tagIntent = new Intent(BlogTagAskActivity.this, TagATreeActivity.class);
                            Bitmap bm = imageBitmap; // your bitmap
                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 50, bs);
                            tagIntent.putExtra("byteArray", bs.toByteArray());
                            startActivity(tagIntent);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    private void upload() {
        // Image location URL
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_FOR_AVATAR);
    }



    private void clickpic() {
        // Check Camera
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        } else {
            Toast.makeText(getApplication(), "Camera not supported", Toast.LENGTH_LONG).show();
        }
    }

    /*private View.OnClickListener clickListener = new View.OnClickListener() {

        public void onClick(View v) {
            Intent tagIntent = new Intent(BlogTagAskActivity.this, TagATreeActivity.class);
            Bitmap bm = null; // your bitmap
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 50, bs);
            tagIntent.putExtra("byteArray", bs.toByteArray());
            startActivity(tagIntent);
        }
    };*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {

    }
}
