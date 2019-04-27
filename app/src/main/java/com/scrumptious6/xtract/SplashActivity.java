package com.scrumptious6.xtract;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity{
    private final int SPLASH_DURATION = 1500;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent startApplication = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(startApplication);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.fade_out_activity);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
