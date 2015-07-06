package com.windroilla.guru;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.windroilla.guru.api.ApiService;
import com.windroilla.guru.api.responseclasses.UserProfile;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ProfilePictureDisplay extends ActionBarActivity {

    public final static String SELECT_CAMERA_ACTION = "camera";
    public final static String SELECT_GALLERY_ACTION = "gallery";
    public final static String ARG_PROFILE_PIC = "profile_picture";
    public final static String ARG_IMAGE_PATH = "image_path";
    public final static int REQUEST_CODE_UPDATE_PIC = 0x2;
    public static final String TEMP_PHOTO_FILE_NAME = "guru_profile.jpg";
    private final static String TAG = ProfilePictureDisplay.class.getSimpleName();
    @Inject
    ApiService apiService;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GuruApp.getsInstance().graph().inject(this);
        setContentView(R.layout.activity_profile_picture_display);
        imageView = (ImageView) findViewById(R.id.display_picture);
        apiService.getUserProfileObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UserProfile>() {
                               @Override
                               public void call(final UserProfile userProfile) {
                                   if (userProfile == null) {
                                       Log.e(TAG, "No user profile returned from the server");
                                       return;
                                   }
                                   if (!TextUtils.isEmpty(userProfile.image)) {
                                       byte[] file = Base64.decode(userProfile.image, Base64.DEFAULT);
                                       imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                                       imageView.setAdjustViewBounds(false);
                                       imageView.setImageBitmap(BitmapFactory.decodeByteArray(file, 0, file.length));
                                   } else {
                                       imageView.setImageResource(R.mipmap.ic_launcher);
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "Could not load user data " + throwable);
                            }
                        }
                );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_picture_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == REQUEST_CODE_UPDATE_PIC) {
            if (resultCode == RESULT_OK) {
                String imagePath = result.getStringExtra(ARG_IMAGE_PATH);
                if (!TextUtils.isEmpty(imagePath)) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
                    imageView.setImageBitmap(myBitmap);
                    Intent intent = new Intent();
                    intent.putExtra(ARG_IMAGE_PATH, imagePath);
                    setResult(RESULT_OK, intent);
                }
            } else if (resultCode == RESULT_CANCELED) {
                //TODO : Handle case
            } else {
                String errorMsg = result.getStringExtra(ImageCropActivity.ERROR_MSG);
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void OnImageModeSelection(View view) {
        String action;
        switch (view.getId()) {
            case R.id.display_select_camera:
                action = SELECT_CAMERA_ACTION;
                break;
            case R.id.display_select_gallery:
                action = SELECT_GALLERY_ACTION;
                break;
            default:
                action = SELECT_GALLERY_ACTION;
                break;
        }
        Intent intent = new Intent(this, ImageCropActivity.class);
        intent.putExtra("ACTION", action);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_PIC);
    }
}
