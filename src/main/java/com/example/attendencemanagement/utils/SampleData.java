package com.example.attendencemanagement.utils;

public class SampleData {
    private static String[] departments = {"CSE","BBA","English","Math","Physics"};
    private static String[] sessionWithYear = {"FALL-22","SPRING-22","SUMMER-22","FALL-23"};
    private static String[] batch = {"46A","46B","49A","49B"};
    private static String[] subjects = {"MAT101","CSE215","CSE216","SE29","SE210"};
    private static String[] sessions = {"FALL","SPRING","SUMMER"};
    private static String[] years = {"22","23","24","25"};
    public static String[] getYears(){
        return years;
    }
    public static String[] getDepartments(){
     return departments;
    }
    public static String[] getSessionWithYear(){
        return sessionWithYear;
    }
    public static String[] getSubjects(){
        return subjects;
    }
    public static String[] getSessions(){
        return sessions;
    }


}
