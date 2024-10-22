package com.example.leave3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class LeaveManagementDatabase extends SQLiteOpenHelper
{
    Context context;
    public  static final  String DATABASE_NAME="LMS";
    public  static final  int  VERSION=1;

    public  static final  String HOD_TABLE="HOD_DETAILS";
    public  static final  String Faculty_TABLE="Faculty_DETAILS";
    public  static final  String Student_TABLE="Student_DETAILS";
    public  static final  String LEAVE_TABLE="LEAVE_DETAILS";

    public  static final  String NAME_COL="NAME";
    public  static final  String EMAIL_COL="EMAIL";
    public  static final  String PASSWORD_COL="PASSWORD";
    public  static final  String GENDER_COL="GENDER";
    public  static final  String contactNO_COL="contact_NO";
    public  static final  String QUALIFICATION_COL="QULIFICATION";
    public  static final  String EXPERIENCE_COL="EXPERIENCE";
    public  static final  String STATUS_COL="STATUS";
    public  static final  String PHOTO_COL="PHOTO";

    public  static final  String DEPARTMENT_COL="DEPARTMENT";
    public  static final  String LEAVE_REASON_COL="LEAVE_REASON";
    public  static final  String DATE_FROM_COL="DATE_FROM";
    public  static final  String DATE_TO_COL="DATE_TO";
    public  static final  String STATUS_Faculty_COL="Faculty_LEAVE_STATUS";
    public  static final  String STATUS_HOD_COL="HOD_LEAVE_STATUS";




    public LeaveManagementDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query="create table "+HOD_TABLE+" ( "+NAME_COL+" text,"+EMAIL_COL+" text Primary Key,"+PASSWORD_COL+" text,"+GENDER_COL+" text,"+contactNO_COL+" integer,"+QUALIFICATION_COL+" text,"+EXPERIENCE_COL+" text,"+STATUS_COL+" text,"+PHOTO_COL+" BLOB)";
        db.execSQL(query);

        String query1="create table "+Faculty_TABLE+" ( "+NAME_COL+" text,"+EMAIL_COL+" text Primary Key,"+PASSWORD_COL+" text,"+GENDER_COL+" text,"+contactNO_COL+" integer,"+QUALIFICATION_COL+" text,"+EXPERIENCE_COL+" text,"+STATUS_COL+" text,"+PHOTO_COL+" BLOB)";
        db.execSQL(query1);

        String query2="create table "+Student_TABLE+" ( "+NAME_COL+" text,"+EMAIL_COL+" text Primary Key,"+PASSWORD_COL+" text,"+GENDER_COL+" text,"+contactNO_COL+" integer,"+QUALIFICATION_COL+" text,"+EXPERIENCE_COL+" text,"+STATUS_COL+" text,"+PHOTO_COL+" BLOB)";
        db.execSQL(query2);

        String query3="create table "+LEAVE_TABLE+" ( "+EMAIL_COL+" text Primary Key  ,"+DEPARTMENT_COL+" text,"+LEAVE_REASON_COL+" text,"+DATE_FROM_COL+" text,"+DATE_TO_COL+" text,"+STATUS_Faculty_COL+" text,"+STATUS_HOD_COL+" text)";
        db.execSQL(query3);
/*

        String department="";
        String leave_reason="";
        String status_HOD="pending";
        String status_Faculty="pending";
        String to_date="";
        String from_date="";
        ContentValues cv=new ContentValues();
        cv.put(EMAIL_COL,"");
        cv.put(DEPARTMENT_COL,department);
        cv.put(LEAVE_REASON_COL,leave_reason);
        cv.put(DATE_FROM_COL,to_date);
        cv.put(DATE_TO_COL,from_date);
        cv.put(STATUS_Faculty_COL,status_Faculty);
        cv.put(STATUS_HOD_COL,status_HOD);
        long res=db.insert(LEAVE_TABLE,null,cv);
        if(res!=-1)
        {
            Toast.makeText(context, "Table Create Leave ", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Cancel ", Toast.LENGTH_SHORT).show();
        }
*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + HOD_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Student_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Faculty_TABLE);

        // create new table
        onCreate(db);
    }
}
