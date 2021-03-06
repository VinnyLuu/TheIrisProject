/*
 * Copyright (c) Team 7, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.team7.cmput301.android.theirisproject.controller;

import android.content.Intent;
import android.os.Bundle;

import com.team7.cmput301.android.theirisproject.Extras;
import com.team7.cmput301.android.theirisproject.IrisProjectApplication;
import com.team7.cmput301.android.theirisproject.model.Record;
import com.team7.cmput301.android.theirisproject.model.RecordPhoto;
import com.team7.cmput301.android.theirisproject.task.Callback;
import com.team7.cmput301.android.theirisproject.task.GetRecordPhotoTask;
import com.team7.cmput301.android.theirisproject.task.GetRecordTask;

import java.util.List;

/**
 * RecordController allows communication between the Record Model and
 * the Record Activity
 *
 * @author itstc
 * */
public class RecordController extends IrisController<Record> {

    private String recordId;

    public RecordController(Intent intent) {
        super(intent);
        recordId = intent.getStringExtra(Extras.EXTRA_RECORD_ID);
        model = new Record();
    }

    public Record getRecordModel() {
        return model;
    }

    /**
     * getRecordData executes GetRecordTask and updates
     * the model in controller and also invoking the callback
     * given from activity
     * @param cb
     * */
    public void getRecordData(Callback cb) {
        new GetRecordTask(new Callback<Record>() {
            @Override
            public void onComplete(Record res) {
                model.asyncSetFields(res);
                cb.onComplete(model);
            }
        }).execute(recordId);

        new GetRecordPhotoTask(new Callback<List<RecordPhoto>>() {
            @Override
            public void onComplete(List<RecordPhoto> res) {
                model.asyncSetRecordPhotos(res);
                cb.onComplete(model);
            }
        }).execute(recordId);
    }

    public String getRecordId() {
        return recordId;
    }

    public List<RecordPhoto> getPhotos() { return model.getRecordPhotos(); }

    @Override
    Record getModel(Bundle data) {
        Record cachedRecord = IrisProjectApplication.getRecordById(recordId);
        if (cachedRecord != null) return cachedRecord;
        return new Record();
    }
}
