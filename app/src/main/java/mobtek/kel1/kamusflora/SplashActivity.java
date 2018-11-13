package mobtek.kel1.kamusflora;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import db.Flora;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                mainIntent.putExtra("list", preLoadRaw());
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public ArrayList<Flora> preLoadRaw(){
        ArrayList<Flora> dictionary = new ArrayList<>();
        String line = null;

        BufferedReader reader;

        try {
            InputStream rawDict = getApplication().getResources().openRawResource(R.raw.flora);

            reader = new BufferedReader(new InputStreamReader(rawDict));

            do {
                line = reader.readLine();
                String[] splitStr = line.split("=");

                Flora flora = new Flora();
                flora.setNamaFlora(splitStr[0]);
                flora.setNamaLatin(splitStr[1]);
                dictionary.add(flora);
            } while (line != null);
        } catch (Exception err) {
            err.printStackTrace();
        }
        return dictionary;
    }
}
