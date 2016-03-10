package hk.ust.sight.starbugsv0;

/**
 * Created by user on 11/03/2016.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class confirmImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_image);
//        findViewById(R.id.horizontalScrollView2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(tutorial_2.this, tutorial_3.class));
//            }
//        });
        findViewById(R.id.regradeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(confirmImage.this, ImagesOverview.class));
            }
        });
        findViewById(R.id.confirmButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(confirmImage.this, results.class));
            }
        });
    }
}
