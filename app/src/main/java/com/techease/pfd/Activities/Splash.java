package com.techease.pfd.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techease.pfd.Activities.Intro.IntroActivity;
import com.techease.pfd.R;
import com.techease.pfd.Utils.CheckNetwork;

import java.security.MessageDigest;

public class Splash extends AppCompatActivity {

    ImageView ivSpalsh,ivWifi;
    TextView textView;
    Typeface typefaceBold;
    Button btnTryAgain;
    ProgressBar progressBar;
    int progressbarstatus = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ivSpalsh=(ImageView)findViewById(R.id.ivSplash);
        ivWifi=(ImageView)findViewById(R.id.ivNointernet);
        textView=(TextView)findViewById(R.id.tv);
        typefaceBold=Typeface.createFromAsset(this.getAssets(),"brandon_reg.otf");
        textView.setTypeface(typefaceBold);
        btnTryAgain=(Button)findViewById(R.id.btnTryAgain);
        progressBar=(ProgressBar)findViewById(R.id.progress);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
               setProgressValue(progressbarstatus);
                Check();
            }
        });

       // ImageView imageView=(ImageView)findViewById(R.id.ivGifSplashScreen);


        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                       Check();
                      // getHasKey();

                }
            }
        };
        timer.start();


    }

    public void Check()
    {
        if (CheckNetwork.isInternetAvailable(Splash.this))
        {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(Splash.this, IntroActivity.class));
                    finish();
                }
            });

        }else
        {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    ivSpalsh.setImageResource(R.drawable.loginbackpic);
                    ivWifi.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    btnTryAgain.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

        }
    }
    //Method for generating keyhash value
    void getHasKey()
    {
        //Get Has Key
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo("techease.com.postcard", PackageManager.GET_SIGNATURES);
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

    private void setProgressValue(final int progress) {

        // set the progress
        progressBar.setProgress(progress);
        // thread is used to change the progress value
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setProgressValue(progress + 10);
            }
        });
        thread.start();
    }
}
