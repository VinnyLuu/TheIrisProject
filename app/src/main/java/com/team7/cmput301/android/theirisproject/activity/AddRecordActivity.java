/*
 * Copyright (c) Team X, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.team7.cmput301.android.theirisproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.team7.cmput301.android.theirisproject.Extras;
import com.team7.cmput301.android.theirisproject.ImageConverter;
import com.team7.cmput301.android.theirisproject.ImageListAdapter;
import com.team7.cmput301.android.theirisproject.IrisProjectApplication;
import com.team7.cmput301.android.theirisproject.R;
import com.team7.cmput301.android.theirisproject.controller.AddRecordController;
import com.team7.cmput301.android.theirisproject.model.RecordPhoto;
import com.team7.cmput301.android.theirisproject.task.Callback;

/**
 * AddRecordActivity is a form to add an activity
 *
 * @author jtfwong
 * */
public class AddRecordActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_IMAGE = 1;
    private static final int REQUEST_MAP_LOCATION = 2;
    private static final int REQUEST_BODY_LOCATION = 3;

    private AddRecordController controller;
    private TextView titleField;
    private TextView descField;
    private ImageView bodyLocationImage;
    private Button bodyLocationButton;

    private RecyclerView recordPhotoListView;
    private ImageListAdapter<RecordPhoto> recordPhotoImageListAdapter;

    private FloatingActionButton cameraButton;
    private FloatingActionButton mapButton;

    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = new AddRecordController(getIntent());

        titleField = findViewById(R.id.record_title_edit_text);
        descField = findViewById(R.id.record_description_edit_text);
        bodyLocationImage = findViewById(R.id.record_body_location_image);

        recordPhotoListView = findViewById(R.id.record_add_image_list);
        recordPhotoListView.setAdapter(new ImageListAdapter<RecordPhoto>(this, controller.getRecordPhotos(), true));
        recordPhotoListView.setLayoutManager(new LinearLayoutManager(this));
        ((LinearLayoutManager)recordPhotoListView.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);

        bodyLocationButton = findViewById(R.id.record_body_location_button);
        bodyLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchBodyLocationForm();
            }
        });

        cameraButton = findViewById(R.id.record_camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchCameraIntent();
            }
        });

        mapButton = findViewById(R.id.record_map_button);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchMapIntent();
            }
        });

        submitButton = findViewById(R.id.record_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.submitRecord(
                        titleField.getText().toString(),
                        descField.getText().toString(),
                        submitRecordCallback());
            }
        });
    }

    private void setErrorMessage() {
        Toast.makeText(AddRecordActivity.this, "Error making Record!", Toast.LENGTH_LONG).show();
    }

    private Callback<String> submitRecordCallback() {
        return new Callback<String>() {
            @Override
            public void onComplete(String res) {
                if (res != null) dispatchRecordActivity(res);
                else setErrorMessage();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA_IMAGE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            controller.addRecordPhoto(imageBitmap);
        } else if (requestCode == REQUEST_BODY_LOCATION && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bp = ImageConverter.scaleBitmapPhoto((Bitmap) extras.get("data_img"), 512, 512);
            controller.setBodyLocation((String) extras.get("data_src"), (float[]) extras.get("data_xy"));
            bodyLocationImage.setImageBitmap(bp);

        }
        else if (requestCode == REQUEST_MAP_LOCATION && resultCode == RESULT_OK) {
            double location[] = data.getDoubleArrayExtra(Extras.EXTRA_LOCATION);
            controller.addLocation(location);
        }
    }

    private void dispatchBodyLocationForm() {
        Intent intent = new Intent(AddRecordActivity.this, BodyPhotoListActivity.class);
        intent.putExtra(Extras.EXTRA_BODYPHOTO_FORM, true);
        intent.putExtra(Extras.EXTRA_BODYPHOTO_USER, IrisProjectApplication.getCurrentUser().getId());
        startActivityForResult(intent, REQUEST_BODY_LOCATION);
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

    /**
     * dispatchRecordActivity will goto a view record
     * activity with the given id
     * */
    private void dispatchRecordActivity(String id) {
        Intent intent = new Intent(AddRecordActivity.this, ViewRecordActivity.class);
        intent.putExtra(Extras.EXTRA_RECORD_ID, id);
        startActivity(intent);
        finish();
    }

    private void dispatchMapIntent() {
        //TODO: If previously set a marker, get last geolocation and make starting position to that
        Intent intent = new Intent(AddRecordActivity.this, MapActivity.class);
        startActivityForResult(intent, REQUEST_MAP_LOCATION);
    }

}