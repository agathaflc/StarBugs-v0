package hk.ust.sight.starbugsv0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;

import java.util.logging.LogRecord;

/**
 * Created by StarBugs on 26/4/16.
 */
public class loadingPage extends Activity {

    //Introduce an delay
    private final int WAIT_TIME = 2500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        System.out.println("LoadingScreenActivity  screen started");
        setContentView(R.layout.loading_page);
        findViewById(R.id.mainSpinner1).setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                //Simulating a long running task
                //this.Sleep(1000);
                System.out.println("Going to Profile Data");
	  /* Create an Intent that will start the ProfileData-Activity. */
                Intent mainIntent = new Intent(loadingPage.this,resultsY0.class);
                loadingPage.this.startActivity(mainIntent);
                loadingPage.this.finish();
            }
        }, WAIT_TIME);
    }
}