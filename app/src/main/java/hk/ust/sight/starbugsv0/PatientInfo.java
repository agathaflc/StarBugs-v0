package hk.ust.sight.starbugsv0;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class PatientInfo extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    public static String patientName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);



        findViewById(R.id.btnBack1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientInfo.this, MainActivity.class));
            }
        });
        findViewById(R.id.btnNext1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText lastName = (EditText) findViewById(R.id.editLastName);
                EditText firstName = (EditText) findViewById(R.id.editFirstName);
                EditText middleName = (EditText) findViewById(R.id.editMiddleName);
                EditText bpjsNumber = (EditText) findViewById(R.id.editBpjsNumber);
                EditText clinicName = (EditText) findViewById(R.id.editClinic);

                if  (firstName.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                }
                else if (bpjsNumber.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter BPJS Number", Toast.LENGTH_SHORT).show();
                }
                else if (clinicName.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Enter Clinic Name", Toast.LENGTH_SHORT).show();
                }
                else {
                    //// RETRIEVE THE REMAINING DATA ////

                    // Retrieve gender: gender will be String "Male" or "Female" in patientGender
                    String patientGender;
                    int selectedGenderId = ((RadioGroup)findViewById(R.id.radioGender)).getCheckedRadioButtonId();
                    RadioButton genderButton = (RadioButton) findViewById(selectedGenderId);
                    patientGender = genderButton.getText().toString();

                    // Date of Birth: all stored in variables year, month, day
                    // retrieve phone number
                    EditText phoneNum = (EditText) findViewById(R.id.editPhoneNumber);
                    String phoneNumString = phoneNum.toString().trim();

                    // Write into JSON file
                    JSONObject patient = new JSONObject();


                    try {
                        patient.put("lastName", lastName.getText().toString().trim());
                        patient.put("firstName", firstName.getText().toString().trim());
                        patient.put("middleName", middleName.getText().toString().trim());
                        patient.put("bpjsNumber", bpjsNumber.getText().toString().trim());
                        patient.put("gender", patientGender);
                        patient.put("birthYear", year);
                        patient.put("birthMonth", month);
                        patient.put("birthDay", day);
                        patient.put("phoneNumber", phoneNumString);
                        patient.put("clinicName", clinicName.getText().toString().trim());

                        String fileName = bpjsNumber.getText().toString().trim() + ".json";

                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        // path to /data/data/hk.ust,sight.starbugsv0/app_patient_data/
                        File directory = cw.getDir("patient_data", Context.MODE_PRIVATE);

                        File destination =new File(directory,fileName);
                        //File destination = new File(Environment.getExternalStorageDirectory(), fileName);
                        FileOutputStream fo;
                        try {
                            destination.createNewFile();
                            fo = new FileOutputStream(destination);
                            OutputStreamWriter osw = new OutputStreamWriter(fo);
                            osw.write(patient.toString());
                            osw.flush();
                            Toast.makeText(getApplicationContext(), patient.toString()+"Data",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Data saved",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), destination.toString(),Toast.LENGTH_SHORT).show();
                            osw.close();
                            fo.close();
                        }     catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Settings not saved",Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    // Pass the patient name to results page
                    String patientFirst = firstName.getText().toString().trim();
                    String patientLast = lastName.getText().toString().trim();
                    Intent i = new Intent(PatientInfo.this, dataPatient.class);
                    i.putExtra("patientName", (patientFirst + " " + patientLast));
                    startActivity(i);

                    patientName = (patientFirst + " " + patientLast);

                    // Go to the next activity
                    startActivity(new Intent(PatientInfo.this, StartTutorial.class));
                }
            }
        });
    }

    // Code to get date: http://www.tutorialspoint.com/android/android_datepicker_control.htm
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            int year = 1960;
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            year = arg1;
            month = arg2;
            day = arg3;
            showDate(arg1, arg2 + 1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        TextView birthDate = (TextView) findViewById(R.id.dateOfBirth);
        birthDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
}
