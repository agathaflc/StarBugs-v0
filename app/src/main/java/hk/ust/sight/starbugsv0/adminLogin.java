package hk.ust.sight.starbugsv0;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class adminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) findViewById(R.id.editAdminName);
                EditText password = (EditText) findViewById(R.id.editPw);
                if (username.getText().toString().equals("starbugs") && password.getText().toString().equals("123")) {
                    startActivity(new Intent(adminLogin.this, MainActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(), "Access Denied", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

