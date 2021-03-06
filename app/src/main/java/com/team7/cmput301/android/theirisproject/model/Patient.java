/*
 * Copyright (c) Team 7, CMPUT301, University of Alberta - All Rights Reserved. You may use, distribute, or modify this code under terms and conditions of the Code of Students Behavior at University of Alberta
 */

package com.team7.cmput301.android.theirisproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Patient-type User
 *
 * @author itstc
 * @author jtfwong
 * @author anticobalt
 */
public class Patient extends User {

    transient private List<CareProvider> careProviders;
    transient private ProblemList problems = new ProblemList();
    private List<String> careProviderIds = new ArrayList<>();
    private List<String> problemIds = new ArrayList<>();

    /* Constructors */

    public Patient(String name, String email, String phoneNumber) {
        super(name, email, phoneNumber, UserType.PATIENT);
        careProviders = new ArrayList<>();
    }

    /* Basic setters */

    public void setProblems(ProblemList problems) {
        this.problems = problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = new ProblemList(problems);
    }

    /* Basic getters */

    public List<CareProvider> getCareProviders() {
        return careProviders;
    }

    public ProblemList getProblems() {
        return problems;
    }

    public void addCareProviderId(String careProviderId) {
        careProviderIds.add(careProviderId);
    }

    public List<String> getCareProviderIds() {
        return careProviderIds;
    }

    /* Basic list operations */

    public void addProblem(Problem problem){
        problems.add(problem);
    }
  
    /* Basic setters */

    public void setCareProviders(List<CareProvider> careProviders) {
        this.careProviders = careProviders;
    }

    public void addCareProvider(CareProvider careProvider) {
        careProviders.add(careProvider);
    }

    /* Searches */

    public Problem getProblemById(String id) {
        return problems.getProblemById(id);
    }

    public Record getRecordById(String id) {
        for (Problem problem: problems) {
            Record record = problem.getRecords().getRecordById(id);
            if (record != null) {
                return record;
            }
        }
        return null;
    }

}