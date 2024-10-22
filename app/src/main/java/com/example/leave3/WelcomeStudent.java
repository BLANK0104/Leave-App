package com.example.leave3;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class WelcomeStudent extends AppCompatActivity
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
        setContentView(R.layout.activity_welcome_student);
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
        super.onActivityResult(requestCode, resultCode, data); // Call to superclass implementation
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
        Intent i=new Intent(this,StudentProfileUpdate.class);
        i.putExtra("kemail",profileName);
        startActivity(i);
    }

    public void updateProfile(View v)
    {
        Intent i=new Intent(this,StudentProfileUpdate.class);
        i.putExtra("kemail",profileName);
        startActivity(i);
    }

    public void takeLeave(View view) {
        // Implement the logic for taking leave here
        // For example, you can start a new activity or show a dialog
        Intent intent = new Intent(this, TakeLeaveActivity.class);
        startActivity(intent);
    }

    public void leaveStatus(View v)
    {
        String Faculty_pending="dummy",HOD_pending="dummy";
        String columns[]={ld.STATUS_Faculty_COL,ld.STATUS_HOD_COL};
        String selection=ld.EMAIL_COL+" = ? ";
        String where[]={profileName};
        Cursor c=sd.query(ld.LEAVE_TABLE,columns,selection,where,null,null,null);
        if(c.moveToFirst())
        {
            Faculty_pending=c.getString(0);
            HOD_pending=c.getString(1);
        }

        if(Faculty_pending.equals("pending"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is Pending from Faculty.");
            ad.show();

        }
        else if(Faculty_pending.equals("cancel"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is cancel from Faculty.");
            ad.show();
        }
        else if(HOD_pending.equals("pending"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is Pending from HOD.");
            ad.show();
        }
        else if(HOD_pending.equals("cancel"))
        {
            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Leave Status");
            ad.setMessage("Sorry !\n\nYour Leave application is cancel from HOD.");
            ad.show();
        }
        else if(Faculty_pending.equals("approved") && HOD_pending.equals("approved"))
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