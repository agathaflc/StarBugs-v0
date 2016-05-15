package hk.ust.sight.starbugsv0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class resultsY2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_y2);
        TextView gradingTitle = (TextView) findViewById(R.id.result_title);
        gradingTitle.setText("Grading result for " + PatientInfo.patientName);

        findViewById(R.id.regradeButtonY2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(resultsY2 .this, imageprocessing.class));
            }
        });
        findViewById(R.id.newPButtonY2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(resultsY2.this, PatientInfo.class));
            }
        });
    }
}
