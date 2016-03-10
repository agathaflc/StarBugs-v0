package hk.ust.sight.starbugsv0;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.w3c.dom.Text;

public class PatientInfo extends AppCompatActivity {

    public String patientName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        // Commented this part out because it's causing error and idk how to fix
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientInfo.this, MainActivity.class));
            }
        });
        findViewById(R.id.btnNext2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PatientInfo.this, StartTutorial.class));
            }
        });


    }

    @Override
    protected void onPause()
    {
        super.onPause();
        EditText textobj = (EditText) findViewById(R.id.textName);

        if(!isEmpty(textobj)) {
            updatepatientname();
            int i=1;
        }
        else
        {
            String TAG =  PatientInfo.class.getSimpleName();

            Log.d(TAG,"helo");

        }

    }

    private boolean isEmpty(EditText text)
    {
             if(text.getText().toString().trim().length()>0) {
                 return false;
             }
             else
             {
                 return true;
             }


    }




    public void updatepatientname()
    {
        EditText obj = (EditText) findViewById(R.id.textName);

       patientName=  obj.getText().toString();
    }

}
