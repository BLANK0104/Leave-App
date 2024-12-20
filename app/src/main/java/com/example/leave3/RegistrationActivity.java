package com.example.leave3;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class RegistrationActivity extends AppCompatActivity {

    String check="dummy";
    Bitmap bit=null;
    boolean flag=false;
    EditText et_ame,et_pass,et_email,et_contact,et_qualification,et_experience;
    Spinner user_type;
    String name,email ,password,contact_no,qualification,exp,gender="";
    LeaveManagementDatabase ld;
    SQLiteDatabase sd;

    String type;
    RadioButton rb_male,rb_female;
    RadioGroup gender_id;
    private static final int CAMARA_REQUEST_CODE=1888;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        profile_image=(ImageView)findViewById(R.id.profile_image);
        et_ame = (EditText) findViewById(R.id.input_name);
        et_email = (EditText) findViewById(R.id.input_email);
        et_pass = (EditText) findViewById(R.id.input_password);
        et_contact= (EditText) findViewById(R.id.input_contact_number);
        et_qualification = (EditText) findViewById(R.id.input_qualification);
        et_experience = (EditText) findViewById(R.id.input_experience);
        gender_id=(RadioGroup)findViewById(R.id.gender);
        user_type=(Spinner)findViewById(R.id.spType_user);

        ld=new LeaveManagementDatabase(this);
        sd=ld.getWritableDatabase();

        try {
            Intent intent = getIntent();
            Bundle b = intent.getExtras();
            check = b.getString("k1");
        } catch(Exception e) {
            e.printStackTrace();
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.gender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                gender = checkedRadioButton.getText().toString();
            }
        });
    }

    public void openCamra(View v) {
        Intent camara_intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camara_intent,CAMARA_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMARA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bitmap bit = (Bitmap) data.getExtras().get("data");
            profile_image.setImageBitmap(bit);
            flag=true;
        }
    }

    public void registration(View v) {
        name = et_ame.getText().toString().trim();
        password = et_pass.getText().toString().trim();
        email = et_email.getText().toString().trim();
        contact_no = et_contact.getText().toString().trim();
        qualification = et_qualification.getText().toString().trim();
        exp= et_experience.getText().toString().trim();
        type=user_type.getSelectedItem().toString();

        if(validateName(name)) {
            if(validateEmail(email)) {
                if(validatePassword(password)) {
                    if(validateGender(gender)) {
                        if(validatecontactNo(contact_no)) {
                            if(validateQualification(qualification)) {
                                if(validateExp(exp)) {
                                    double cno=Double.parseDouble(contact_no);
                                    String status="pending";
                                    ContentValues cv=new ContentValues();
                                    cv.put(ld.NAME_COL,name);
                                    cv.put(ld.EMAIL_COL,email);
                                    cv.put(ld.PASSWORD_COL,password);
                                    cv.put(ld.GENDER_COL,gender);
                                    cv.put(ld.contactNO_COL,cno);
                                    cv.put(ld.QUALIFICATION_COL,qualification);
                                    cv.put(ld.EXPERIENCE_COL,exp);
                                    cv.put(ld.STATUS_COL,status);

                                    if(type.equals("HOD")) {
                                        if(check.equals("ok")) {
                                            long res = sd.insert(ld.HOD_TABLE, null, cv);
                                            if (res != -1) {
                                                Snackbar.make(v,"HOD registered ",Snackbar.LENGTH_LONG).show();
                                                et_ame.setText("");
                                                et_email.setText("");
                                                et_pass.setText("");
                                                et_contact.setText("");
                                                et_experience.setText("");
                                                et_qualification.setText("");
                                                profile_image.setImageResource(R.drawable.photo_camera);
                                                bit = null;
                                                et_ame.requestFocus();
                                                finish();
                                                Intent i = new Intent(this, MainActivity.class);
                                                startActivity(i);
                                            } else {
                                                Toast.makeText(this, "Error not inserted HOD in database", Toast.LENGTH_SHORT).show();
                                                finish();
                                                Intent i = new Intent(this, AdminActivity.class);
                                                startActivity(i);
                                            }
                                        } else {
                                            Toast.makeText(this, "Not Allowed to Register the HOD", Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent i = new Intent(this, MainActivity.class);
                                            startActivity(i);
                                        }
                                    } else if(type.equals("Faculty")) {
                                        long res = sd.insert(ld.Faculty_TABLE, null, cv);
                                        if (res != -1) {
                                            et_ame.setText("");
                                            et_email.setText("");
                                            et_pass.setText("");
                                            et_contact.setText("");
                                            et_experience.setText("");
                                            et_qualification.setText("");
                                            profile_image.setImageResource(R.drawable.photo_camera);
                                            bit = null;
                                            et_ame.requestFocus();
                                            Toast.makeText(this, "Successfully Registration of Faculty ", Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent i=new Intent(this,MainActivity.class);
                                            startActivity(i);
                                        } else {
                                            finish();
                                            Intent i=new Intent(this,MainActivity.class);
                                            startActivity(i);
                                            Toast.makeText(this, "Error not inserted Faculty", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if(type.equals("Student")) {
                                        long res = sd.insert(ld.Student_TABLE, null, cv);
                                        if (res != -1) {
                                            Toast.makeText(this, "Successfully Registration of Student ", Toast.LENGTH_SHORT).show();
                                            et_ame.setText("");
                                            et_email.setText("");
                                            et_pass.setText("");
                                            et_contact.setText("");
                                            et_experience.setText("");
                                            et_qualification.setText("");
                                            profile_image.setImageResource(R.drawable.photo_camera);
                                            bit = null;
                                            et_ame.requestFocus();
                                            finish();
                                            Intent i=new Intent(this,MainActivity.class);
                                            startActivity(i);
                                        } else {
                                            Toast.makeText(this, "Error not inserted Student", Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent i=new Intent(this,MainActivity.class);
                                            startActivity(i);
                                        }
                                    } else {
                                        Snackbar.make(v,"Please select type of the User",Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean validateName(String name) {
        if(name.isEmpty()) {
            et_ame.setError("Name should be filled");
            et_ame.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePassword(String password) {
        if(password.isEmpty()) {
            et_pass.setError("Password should be filled");
            et_pass.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateEmail(String email) {
        if(email.isEmpty()) {
            et_email.setError("Email should be filled");
            et_email.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validatecontactNo(String contact_no) {
        if(contact_no.isEmpty()) {
            et_contact.setError("contact no should be filled");
            et_contact.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateQualification(String qualification) {
        if(qualification.isEmpty()) {
            et_qualification.setError("Qualification should be filled");
            et_qualification.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateExp(String exp) {
        if(exp.isEmpty()) {
            et_experience.setError("Experience should be filled");
            et_experience.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateGender(String gender) {
        if (gender.isEmpty()) {
            Toast.makeText(this,"please select Gender",Toast.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }
}