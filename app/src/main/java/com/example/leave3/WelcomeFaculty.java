package com.example.leave3;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class WelcomeFaculty extends AppCompatActivity
{
    TextView profile;
    String profileName="";
    String gender = "";
    String to_date="";
    String from_date="";
    String department="",leave_reason="";

    final int REQUEST_CODE=123;

    LeaveManagementDatabase ld;
    SQLiteDatabase sd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_faculty);
        profile = (TextView) findViewById(R.id.textviewUser);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        try {
            profileName = b.getString("kname");
            profile.setText("Welcome " + profileName);
        }   catch (Exception e){
            e.printStackTrace();
        }

        ld=new LeaveManagementDatabase(this);
        sd=ld.getWritableDatabase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                profileName=data.getExtras().getString("RESULT");
            }

        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menuoption,menu);
        return true;
    }

    public void logout(MenuItem mi)
    {
        finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void viewProfile(MenuItem mi)
    {
        Intent i=new Intent(this,FacultyProfileUpdate.class);
        i.putExtra("kemail",profileName);
        startActivity(i);
    }

    public void updateProfile(View v)
    {
        Intent i=new Intent(this,FacultyProfileUpdate.class);
        i.putExtra("kemail",profileName);
        startActivity(i);
    }

    public void leaveStatus(View v)
    {
        String hod_pending="dummy",principal_pending="dummy";
        String columns[]={ld.STATUS_HOD_COL,ld.STATUS_PRINCIPAL_COL};
        String selection=ld.EMAIL_COL+" = ? ";
        String where[]={profileName};
        Cursor c=sd.query(ld.LEAVE_TABLE,columns,selection,where,null,null,null);
        if(c.moveToFirst())
        {
            hod_pending=c.getString(0);
            principal_pending=c.getString(1);
        }

        if(hod_pending.equals("pending"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is Pending from Head Of Department.");
            ad.show();

        }
        else if(hod_pending.equals("cancel"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is cancel from Head Of Department.");
            ad.show();
        }
        else if(principal_pending.equals("pending"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is Pending from Principal.");
            ad.show();
        }
        else if(principal_pending.equals("cancel"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is cancel from Principal.");
            ad.show();
        }
        else if(hod_pending.equals("approved") && principal_pending.equals("approved"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Congratulations !\n\nYour Leave application is approved.");
            ad.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}