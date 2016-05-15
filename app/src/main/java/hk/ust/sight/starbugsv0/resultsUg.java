package hk.ust.sight.starbugsv0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class resultsUg extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_ug);
        TextView gradingTitle = (TextView) findViewById(R.id.result_title);
        gradingTitle.setText("Grading result for " + PatientInfo.patientName);

        findViewById(R.id.regradeButtonUG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(resultsUg.this, imageprocessing.class));
            }
        });
        findViewById(R.id.newPButtonUG).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(resultsUg.this, PatientInfo.class));
            }
        });
    }
}
