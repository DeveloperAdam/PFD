package com.techease.pfd.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.techease.pfd.Activities.Intro.IntroActivity;
import com.techease.pfd.Configuration.Links;
import com.techease.pfd.R;

import java.security.MessageDigest;

public class Splash extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
       // getHasKey();



        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    sharedPreferences = getSharedPreferences(Links.MyPrefs, Context.MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    String token=sharedPreferences.getString("api_token","");
                    Log.d("token",token);
                    if (!token.equals(""))
                    {
                        startActivity(new Intent(Splash.this,Dashboard.class));
                        finish();
                    }
                    else{

                        startActivity(new Intent(Splash.this,IntroActivity.class));
                        finish();
                    }

                }
            }
        };
        timer.start();


    }

    //Method for generating keyhash value
    void getHasKey()
    {
        //Get Has Key
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("com.techease.pfd", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
