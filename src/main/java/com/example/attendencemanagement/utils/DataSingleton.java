package com.example.attendencemanagement.utils;

import com.example.attendencemanagement.StudentDataModel;
import javafx.scene.control.ProgressIndicator;

public class DataSingleton {
    private static final DataSingleton instance =  new DataSingleton();
    public static final DataSingleton getInstance(){
        return instance;
    }


    public StudentDataModel getStudent() {
        return student;
    }

    public void setStudent(StudentDataModel student) {
        this.student = student;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormattedSubjectName() {
        return formattedSubjectName;
    }

    public void setFormattedSubjectName(String formattedSubjectName) {
        this.formattedSubjectName = formattedSubjectName;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void setProgressIndicator(ProgressIndicator progressIndicator) {
        this.progressIndicator = progressIndicator;
    }

    private ProgressIndicator progressIndicator;
    private String formattedSubjectName;
    private String date;
    private StudentDataModel student;

}
