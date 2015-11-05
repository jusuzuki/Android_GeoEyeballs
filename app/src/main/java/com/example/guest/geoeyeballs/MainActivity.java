package com.example.guest.geoeyeballs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.signupActivityButton) Button signupActivityButton;
    @Bind(R.id.gotoLoginButton) Button gotoLoginButton;
    @Bind(R.id.logoutButton) Button logoutButton;
    @Bind(R.id.uploadPhotoButton) Button uploadPhotoButton;


    public static final int PICK_PHOTO_REQUEST = 2; //what does this mean?
    public static final int MEDIA_TYPE_IMAGE = 4;

    protected Uri mMediaUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null){
            logoutButton.setVisibility(View.VISIBLE);
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Toast.makeText(getApplicationContext(),"You have been logged out",Toast.LENGTH_SHORT).show();
                logoutButton.setVisibility(View.INVISIBLE);
            }
        });

        signupActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        gotoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent choosePhotoIntent = new Intent(Intent.ACTION_GET_CONTENT);
                choosePhotoIntent.setType("image/*");
                startActivityForResult(choosePhotoIntent, PICK_PHOTO_REQUEST);
                }
        });
    }

    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode == PICK_PHOTO_REQUEST){
                if (data == null){
                    Toast.makeText(getApplicationContext(),"PROBLEM DATA NULL",Toast.LENGTH_SHORT).show();
                }
                else {
                    mMediaUri = data.getData();
                    try {
                        Bitmap bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mMediaUri);

                        Log.i("AppInfo", "Image Received");

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();

                        bitmapImage.compress(Bitmap.CompressFormat.PNG, 10, stream);

                        byte[] byteArray = stream.toByteArray();

                        ParseFile file = new ParseFile("image.png", byteArray);

                        ParseObject object = new ParseObject("Image");

                        //object.put("username", ParseUser.getCurrentUser().getUsername());
                        object.put("image", file);
                        ParseACL parseACL = new ParseACL();
                        parseACL.setPublicReadAccess(true);
                        object.setACL(parseACL);

                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if( e == null) {
                                    Toast.makeText(getApplication().getBaseContext(), "Your image has been posted", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplication().getBaseContext(), "There was an error - please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } catch (IOException e) {
                        Toast.makeText(getApplication().getBaseContext(), "There was an error - please try again", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                }
            }
        }
        else if (resultCode != RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"PROBLEM",Toast.LENGTH_SHORT).show();
        }
    }
}
