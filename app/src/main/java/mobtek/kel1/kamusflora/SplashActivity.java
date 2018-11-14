package mobtek.kel1.kamusflora;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import db.Flora;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;
    TextToSpeech speech;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                playSound();
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                mainIntent.putExtra("list", preLoadRaw());
                startActivity(mainIntent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void playSound(){
         speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.US);
                }
            }
        });
        speech.speak("Selamat Datang", TextToSpeech.QUEUE_FLUSH, null);
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
                String[] splitStr;
                if (line.contains("=")){
                    splitStr = line.split("=");
                } else {
                    splitStr = line.split("\t");
                }

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
