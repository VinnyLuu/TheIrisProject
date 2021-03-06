/*
 * Copyright (c) Team 7, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 *
 *
 */

package com.team7.cmput301.android.theirisproject.controller;

import android.content.Intent;
import android.os.Bundle;


import com.team7.cmput301.android.theirisproject.Extras;
import com.team7.cmput301.android.theirisproject.IrisProjectApplication;
import com.team7.cmput301.android.theirisproject.task.Callback;
import com.team7.cmput301.android.theirisproject.model.Problem;
import com.team7.cmput301.android.theirisproject.task.EditProblemTask;


import java.text.ParseException;


/**
 * Controller that submits the edited problem to the database
 * Called from EditProblemActivity
 *
 * @author VinnyLuu
 * @see com.team7.cmput301.android.theirisproject.activity.EditProblemActivity
 */
public class EditProblemController extends IrisController {
    private String problemID;

    public EditProblemController(Intent intent) {
        super(intent);
        this.problemID = intent.getExtras().getString(Extras.EXTRA_PROBLEM_ID);
        this.model = getModel(intent.getExtras());
    }

    @Override
    Object getModel(Bundle data) {
        return null;
    }

    /**
     * submitProblem is a method to asynchronously submit our edited problem
     * once we receive a response from the database, we return a callback
     * with a boolean result either successful or not
     *
     * @param title edited Problem title
     * @param desc edited Problem description
     * @param date edited Problem date
     * @param cb callback method
     * */
    public void submitProblem(String title, String desc, String date, Callback cb) throws ParseException{

        Problem submitProblem = new Problem(title, desc, date, IrisProjectApplication.getCurrentUser().getId());
        new EditProblemTask(cb).execute(submitProblem, problemID);


    }

}
