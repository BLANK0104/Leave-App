package com.example.leave3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewApplicationList extends Activity {

    ListView pendingAppList;
    String keys[]={"name","contactno"};
    int ids[]={R.id.viewtv1,R.id.viewtv2};
    ArrayList pendingAppArrayList;
    LeaveManagementDatabase ld;
    SQLiteDatabase sd;
    String HOD_profile="dummy";
    String HOD_alert="dummy";
    String Faculty_profie="dummy";
    String Faculty_alert="dummy";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_application_list);
        pendingAppList = (ListView) findViewById(R.id.leaveAppList);
        pendingAppArrayList = new ArrayList();

        ld = new LeaveManagementDatabase(this);
        sd = ld.getWritableDatabase();

        try {
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            Faculty_profie = b.getString("FacultyAlert1");
            Faculty_alert=b.getString("FacultyAlert2");
            HOD_profile = b.getString("HODAlert1");
            HOD_alert = b.getString("HODAlert2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(HOD_alert==null)
        {
            HOD_alert="dummy";
        }

        if(HOD_profile==null)
        {
            HOD_profile="dummy";
        }
        if(Faculty_alert==null)
        {
            Faculty_alert="dummy";
        }

        if(Faculty_profie==null)
        {
            Faculty_profie="dummy";
        }

        if (HOD_alert.equals("pAlert"))
        {

            final ArrayList parrayList = new ArrayList();
            String cols[] = {ld.EMAIL_COL, ld.DEPARTMENT_COL};
            final String where = ld.STATUS_HOD_COL + " = ? ";
            String where_args[] = {"pending"};
            Cursor c = sd.query(ld.LEAVE_TABLE, cols, where, where_args, null, null, null);
            int count = 0;
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(0);
                    String department = c.getString(1);

                    HashMap hm = new HashMap();
                    hm.put(keys[0], name);
                    hm.put(keys[1], department);
                    parrayList.add(hm);

                } while (c.moveToNext());
            }
            SimpleAdapter sa = new SimpleAdapter(this, parrayList, R.layout.view_list_style, keys, ids);
            pendingAppList.setAdapter(sa);

            pendingAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap hm1 = (HashMap) parrayList.get(position);
                    final String text = (String) hm1.get(keys[0]);
                    //Toast.makeText(ViewApplicationList.this, "" + text, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder ad = new AlertDialog.Builder(ViewApplicationList.this);
                    ad.setTitle("! Confirm Leave Application ");
                    ad.setMessage("Do You want to confirm this Leave Application");
                    ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues cv = new ContentValues();
                            cv.put(ld.STATUS_HOD_COL, "approved");
                            String where = ld.EMAIL_COL + " = ?";
                            String where_args[] = {text};
                            int res = sd.update(ld.LEAVE_TABLE, cv, where, where_args);

                            if (res != 0)
                            {
                                Toast.makeText(ViewApplicationList.this, "Leave Application is approved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ViewApplicationList.this, WelcomeHOD.class);
                                i.putExtra("RESULT", HOD_profile );
                                setResult(Activity.RESULT_OK, i);
                                finish();
                                dialog.dismiss();
                            }
                        }
                    });

                    ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues cv = new ContentValues();
                            cv.put(ld.STATUS_HOD_COL, "cancel");
                            String where = ld.EMAIL_COL + " = ?";
                            String where_args[] = {text};
                            int res = sd.update(ld.LEAVE_TABLE, cv, where, where_args);
                            if (res != 0)
                            {
                                Toast.makeText(ViewApplicationList.this, "Leave Application is Cancel", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ViewApplicationList.this, WelcomeHOD.class);
                                i.putExtra("RESULT", HOD_profile);
                                setResult(Activity.RESULT_OK, i);
                                finish();
                                dialog.dismiss();
                            }
                        }
                    });
                    ad.show();
                    ad.setCancelable(false);
                }
            });







        } else if (Faculty_alert.equals("hAlert"))
        {

            String cols[] = {ld.EMAIL_COL, ld.DEPARTMENT_COL};
            final String where = ld.STATUS_Faculty_COL + " = ? ";
            String where_args[] = {"pending"};
            Cursor c = sd.query(ld.LEAVE_TABLE, cols, where, where_args, null, null, null);
            int count = 0;
            if (c.moveToFirst()) {
                do {
                    String name = c.getString(0);
                    String department = c.getString(1);

                    HashMap hm = new HashMap();
                    hm.put(keys[0], name);
                    hm.put(keys[1], department);
                    pendingAppArrayList.add(hm);

                } while (c.moveToNext());
            }
            SimpleAdapter sa = new SimpleAdapter(this, pendingAppArrayList, R.layout.view_list_style, keys, ids);
            pendingAppList.setAdapter(sa);

            pendingAppList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap hm1 = (HashMap) pendingAppArrayList.get(position);
                    final String text = (String) hm1.get(keys[0]);
                    Toast.makeText(ViewApplicationList.this, "" + text, Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder ad = new AlertDialog.Builder(ViewApplicationList.this);
                    ad.setTitle("! Confirm Leave Application ");
                    ad.setMessage("Do You want to confirm this Leave Application");
                    ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues cv = new ContentValues();
                            cv.put(ld.STATUS_Faculty_COL, "approved");
                            String where = ld.EMAIL_COL + " = ?";
                            String where_args[] = {text};
                            int res = sd.update(ld.LEAVE_TABLE, cv, where, where_args);
                            if (res != 0) {
                                Toast.makeText(ViewApplicationList.this, "Leave Application is approved", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ViewApplicationList.this, WelcomeFaculty.class);
                                i.putExtra("RESULT", Faculty_profie);
                                setResult(Activity.RESULT_OK, i);
                                finish();
                                dialog.dismiss();
                            }
                        }
                    });

                    ad.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues cv = new ContentValues();
                            cv.put(ld.STATUS_Faculty_COL, "cancel");
                            String where = ld.EMAIL_COL + " = ?";
                            String where_args[] = {text};
                            int res = sd.update(ld.LEAVE_TABLE, cv, where, where_args);
                            if (res != 0) {
                                Toast.makeText(ViewApplicationList.this, "Leave Application is Cancle", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ViewApplicationList.this, WelcomeFaculty.class);
                                i.putExtra("RESULT", Faculty_profie);
                                setResult(Activity.RESULT_OK, i);
                                finish();
                                dialog.dismiss();
                            }
                        }
                    });
                    ad.show();
                    ad.setCancelable(false);
                }
            });
        }
    }
}