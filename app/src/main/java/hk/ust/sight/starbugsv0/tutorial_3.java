package hk.ust.sight.starbugsv0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class tutorial_3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_3);
        findViewById(R.id.btnBack4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tutorial_3.this, tutorial_2.class));
            }
        });
        findViewById(R.id.btnNext3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tutorial_3.this, ImagesOverview.class));
            }
        });
        findViewById(R.id.imageFundusview).setOnTouchListener(new OnSwipeTouchListener(tutorial_3.this) {
            public void onSwipeLeft() {
                startActivity(new Intent(tutorial_3.this, ImagesOverview.class));
            }

            public void onSwipeRight() {
                startActivity(new Intent(tutorial_3.this, tutorial_2.class));
            }
        });

//        findViewById(R.id.horizontalScrollView3).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(tutorial_3.this, ImagesOverview.class));
//            }
    }
}