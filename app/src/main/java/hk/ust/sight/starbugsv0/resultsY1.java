package hk.ust.sight.starbugsv0;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class resultsY1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_y1);
        TextView gradingTitle = (TextView) findViewById(R.id.result_title);
        gradingTitle.setText("Grading result for " + PatientInfo.patientName);
    }
}
