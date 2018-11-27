/*
 * Copyright (c) Team 7, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.team7.cmput301.android.theirisproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.team7.cmput301.android.theirisproject.ImageConverter;
import com.team7.cmput301.android.theirisproject.R;
import com.team7.cmput301.android.theirisproject.controller.IrisController;
import com.team7.cmput301.android.theirisproject.model.BodyPhoto;

/**
 * AddBodyPhotoActivity is an form activity that allows user
 * to add their body photo for the records to map to
 *
 * @author itstc
 * */
public class AddBodyPhotoActivity extends IrisActivity<BodyPhoto> {
    private static final int REQUEST_CAMERA_IMAGE = 1;
    private ImageView newBodyPhotoView;
    private EditText labelField;
    private Button submitButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_body_photo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newBodyPhotoView = findViewById(R.id.add_bodyphoto_image);
        labelField = findViewById(R.id.add_bodyphoto_label);
        submitButton = findViewById(R.id.add_bodyphoto_submit);

        newBodyPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchCameraIntent();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * dispatchCameraIntent will start the camera app to take a picture
     * if the patient wants to add a body photo
     * */
    private void dispatchCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = ImageConverter.scaleBitmapPhoto((Bitmap) extras.get("data"), 256, 256);
            newBodyPhotoView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    protected IrisController createController(Intent intent) {
        return null;
    }
}