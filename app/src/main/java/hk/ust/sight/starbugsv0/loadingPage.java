//package hk.ust.sight.starbugsv0;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import android.os.Handler;
//import java.util.logging.LogRecord;
//
///**
// * Created by StarBugs on 26/4/16.
// */
//public class loadingPage extends AppCompatActivity {
//    private static int TIME_OUT = 2700; //Time to launch the another activity
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.loading_page);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(loadingPage.this, resultsY0.class);
//                startActivity(i);
//                finish();
//            }
//        }, TIME_OUT);
//    }
//}