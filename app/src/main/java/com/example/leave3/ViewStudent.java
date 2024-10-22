package com.example.leave3;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewStudent extends AppCompatActivity {

    ListView approvedlv,pendinglv,canclelv;
    ArrayList al_can,al_ap,al_pen;
    LeaveManagementDatabase ld;
    SQLiteDatabase sd;
    String d_email,d_name,d_contact,d_gender,d_exp;
    String keys[]={"name","contactno"};
    int ids[]={R.id.viewtv1,R.id.viewtv2};
    String pass1="dummy",pass2="dummy",pass_Faculty="dummy";
    String text="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        approvedlv=(ListView)findViewById(R.id.lvApprovedStudent);
        pendinglv=(ListView)findViewById(R.id.lvPendingStudent);
        canclelv=(ListView)findViewById(R.id.lvCancleStudent);


        try
        {
            Intent i = getIntent();
            Bundle b = i.getExtras();
            pass1=b.getString("admin");
            pass2=b.getString("HOD");
            pass_Faculty=b.getString("Faculty");

        }catch(Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }
        if(pass2==null)
            pass2="dummy";
        if(pass1==null)
            pass1="dummy";
        if(pass_Faculty==null)
            pass_Faculty="dummy";


        ld=new LeaveManagementDatabase(this);
        sd=ld.getWritableDatabase();


        showPending();
        showApprove();
        showCancle();


        pendinglv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                HashMap hm = (HashMap) al_pen.get(position);
                text = (String) hm.get(keys[0]);

                String cols[] = {LeaveManagementDatabase.NAME_COL, LeaveManagementDatabase.contactNO_COL, LeaveManagementDatabase.EMAIL_COL, LeaveManagementDatabase.GENDER_COL, LeaveManagementDatabase.EXPERIENCE_COL};
                String sel_args[] = {text};
                String sel = LeaveManagementDatabase.EMAIL_COL + " = ? ";
                Cursor c = sd.query(LeaveManagementDatabase.Student_TABLE, cols, sel, sel_args, null, null, null);
                if (c.moveToFirst()) {
                    d_name = c.getString(0);
                    d_contact = c.getString(1);
                    d_email = c.getString(2);
                    d_gender = c.getString(3);
                    d_exp = c.getString(4);
                }


                if ((pass1.equals("adminAlert")))
                {

                    AlertDialog.Builder ad = new AlertDialog.Builder(ViewStudent.this);
                    ad.setTitle("Delete");
                    ad.setMessage("Do You Want to Delete this Student");
                    ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String whr = LeaveManagementDatabase.EMAIL_COL + "= ?";
                            String whr_args[] = {d_email};

                            int i = sd.delete(LeaveManagementDatabase.Student_TABLE, whr, whr_args);
                            if (i != 0) {
                                // show();
                                Toast.makeText(ViewStudent.this, "delete the Student", Toast.LENGTH_SHORT).show();


                            }
                            dialog.dismiss();
                        }
                    });
                    ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();

                }
                else if (pass2.equals("HODAlert"))
                {
                    final Dialog d = new Dialog(ViewStudent.this);
                    d.setContentView(R.layout.view_profile_dialoge);
                    TextView tv_name = (TextView) d.findViewById(R.id.d_name);
                    TextView tv_cno = (TextView) d.findViewById(R.id.d_con);
                    TextView tv_email = (TextView) d.findViewById(R.id.d_email);
                    TextView tv_gender = (TextView) d.findViewById(R.id.d_gender);
                    TextView tv_quali = (TextView) d.findViewById(R.id.d_quali);
                    ImageButton iv_approve = (ImageButton) d.findViewById(R.id.btn_right);
                    ImageButton iv_cancle = (ImageButton) d.findViewById(R.id.btn_wrong);

                    tv_name.setText(d_name);
                    tv_cno.setText(d_contact);
                    tv_email.setText(d_email);
                    tv_gender.setText(d_gender);
                    tv_quali.setText(d_exp);
                    iv_approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Toast.makeText(ViewStudent.this, "Approve ", Toast.LENGTH_SHORT).show();
                            ContentValues cv=new ContentValues();
                            cv.put(LeaveManagementDatabase.STATUS_COL,"approved");
                            String whr=LeaveManagementDatabase.EMAIL_COL+"= ?";
                            String whr_args[]={text};

                            int res=sd.update(LeaveManagementDatabase.Student_TABLE,cv,whr,whr_args);
                            Toast.makeText(ViewStudent.this, ""+res, Toast.LENGTH_SHORT).show();
                            if(res!=0)
                            {

                                showPending();
                                showApprove();
                                Toast.makeText(ViewStudent.this, "Student is approve", Toast.LENGTH_SHORT).show();
                            }
                            d.dismiss();

                        }
                    });

                    iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ViewStudent.this, "Rejected", Toast.LENGTH_SHORT).show();
                            d.dismiss();
                        }
                    });
                    d.show();
                }
                else if(pass_Faculty.equals("FacultyAlert"))
                {
                    final Dialog d = new Dialog(ViewStudent.this);
                    d.setContentView(R.layout.view_profile_dialoge);
                    TextView tv_name = (TextView) d.findViewById(R.id.d_name);
                    TextView tv_cno = (TextView) d.findViewById(R.id.d_con);
                    TextView tv_email = (TextView) d.findViewById(R.id.d_email);
                    TextView tv_gender = (TextView) d.findViewById(R.id.d_gender);
                    TextView tv_quali = (TextView) d.findViewById(R.id.d_quali);
                    ImageButton iv_approve = (ImageButton) d.findViewById(R.id.btn_right);
                    ImageButton iv_cancle = (ImageButton) d.findViewById(R.id.btn_wrong);

                    tv_name.setText(d_name);
                    tv_cno.setText(d_contact);
                    tv_email.setText(d_email);
                    tv_gender.setText(d_gender);
                    tv_quali.setText(d_exp);
                    iv_approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {
                            Toast.makeText(ViewStudent.this, "Approve ", Toast.LENGTH_SHORT).show();
                            ContentValues cv=new ContentValues();
                            cv.put(LeaveManagementDatabase.STATUS_COL,"approved");
                            String whr=LeaveManagementDatabase.EMAIL_COL+"= ?";
                            String whr_args[]={text};

                            int res=sd.update(LeaveManagementDatabase.Student_TABLE,cv,whr,whr_args);
                            Toast.makeText(ViewStudent.this, ""+res, Toast.LENGTH_SHORT).show();
                            if(res!=0)
                            {

                                showPending();
                                showApprove();
                                Toast.makeText(ViewStudent.this, "Student is approve", Toast.LENGTH_SHORT).show();
                            }
                            d.dismiss();
                        }

                    });
                    d.show();


                    iv_cancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v)
                        {

                            Toast.makeText(ViewStudent.this, "Rejected", Toast.LENGTH_SHORT).show();
                            ContentValues cv=new ContentValues();
                            cv.put(ld.STATUS_COL,"cancle");
                            String where=LeaveManagementDatabase.EMAIL_COL +" = ?";
                            String where_args[]={text};
                            int res=sd.update(LeaveManagementDatabase.Faculty_TABLE,cv,where,where_args);
                            if(res!=0){
                                Toast.makeText(ViewStudent.this, "Faculty is Cancle", Toast.LENGTH_SHORT).show();
                                showCancle();
                                showPending();
                                showApprove();
                            }

                            d.dismiss();
                        }
                    });
                    d.show();
                }
            }
        });



    }


    public void showPending()
    {
        al_pen = new ArrayList();
        String cols[] = {LeaveManagementDatabase.EMAIL_COL, LeaveManagementDatabase.contactNO_COL};
        String sel = ld.STATUS_COL + " = ?";
        String sel_ags[] = {"pending"};
        Cursor c = sd.query(ld.Student_TABLE, cols, sel, sel_ags, null, null, null);

        boolean res = c.moveToFirst();
        if (res)
        {
            do {
                String name   =c.getString(0);
                //profile_name.add(name);
                String  contactno=c.getString(1);
                //profile_cno.add(contactno);
                HashMap hm= new HashMap();
                hm.put(keys[0],name);
                hm.put(keys[1],contactno);
                al_pen.add(hm);
            } while (c.moveToNext());
        }
        SimpleAdapter sa=new SimpleAdapter(ViewStudent.this,al_pen,R.layout.view_list_style,keys,ids);
        pendinglv.setAdapter(sa);
    }

    public void showApprove()
    {
        al_ap = new ArrayList();
        String cols[] = {LeaveManagementDatabase.EMAIL_COL, LeaveManagementDatabase.contactNO_COL};
        String sel = ld.STATUS_COL + " = ?";
        String sel_ags[] = {"approved"};
        Cursor c = sd.query(ld.Student_TABLE, cols, sel, sel_ags, null, null, null);

        boolean res = c.moveToFirst();
        if (res)
        {
            do {
                String name   =c.getString(0);

                String  contactno=c.getString(1);

                HashMap hm= new HashMap();
                hm.put(keys[0],name);
                hm.put(keys[1],contactno);
                al_ap.add(hm);

            } while (c.moveToNext());
        }

        SimpleAdapter sa=new SimpleAdapter(ViewStudent.this,al_ap,R.layout.view_list_style,keys,ids);
        approvedlv.setAdapter(sa);
    }

    public void showCancle()
    {

        al_can = new ArrayList();
        String cols[] = {LeaveManagementDatabase.EMAIL_COL, LeaveManagementDatabase.contactNO_COL};
        String sel = ld.STATUS_COL + " = ?";
        String sel_ags[] = {"cancle"};
        Cursor c = sd.query(ld.Student_TABLE, cols, sel, sel_ags, null, null, null);
        al_can.clear();

        boolean res = c.moveToFirst();
        if (res) {
            do {
                String name   =c.getString(0);
                String  contactno=c.getString(1);
                HashMap hm= new HashMap();
                hm.put(keys[0],name);
                hm.put(keys[1],contactno);
                al_can.add(hm);
            } while (c.moveToNext());
        }
        SimpleAdapter sa=new SimpleAdapter(ViewStudent.this,al_can,R.layout.view_list_style,keys,ids);
        canclelv.setAdapter(sa);
    }
}