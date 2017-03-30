package com.example.koshal.atttendance;

/**
 * Created by KOSHAL on 2/4/2017.
 */

public class Courses {

    String course_name;
    String course_id;
    String course_dept;
    int course_sem;

    public int getCourse_sem() {
        return course_sem;
    }

    public void setCourse_sem(int course_sem) {
        this.course_sem = course_sem;
    }

    public String getCourse_dept() {
        return course_dept;
    }

    public void setCourse_dept(String course_dept) {
        this.course_dept = course_dept;
    }

    int id;

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
