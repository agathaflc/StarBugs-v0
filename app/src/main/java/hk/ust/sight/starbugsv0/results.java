package hk.ust.sight.starbugsv0;

/**
 * Created by user on 11/03/2016.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import hk.ust.sight.starbugsv0.PatientInfo;

public class results extends AppCompatActivity {

    private PatientInfo s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
//        findViewById(R.id.horizontalScrollView2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(tutorial_2.this, tutorial_3.class));
//            }
//        });
        TextView title = (TextView) findViewById(R.id.result_title);

        title.setText(s.patientName);

        findViewById(R.id.regradeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(results.this, ImagesOverview.class));
            }
        });
        findViewById(R.id.newPButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(results.this, PatientInfo.class));
            }
        });
    }
}