package mobtek.kel1.kamusflora;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import db.Flora;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{
    TextToSpeech speech;
    TextView namaFlora, namaLatin;
    ImageView speakerFlora, img;
    Flora flora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        namaFlora = (TextView) findViewById(R.id.namaFlora);
        namaLatin = (TextView) findViewById(R.id.namaLatin);
        speakerFlora = (ImageView) findViewById(R.id.speakerFlora);
        img = (ImageView) findViewById(R.id.img);

        flora = (Flora) getIntent().getSerializableExtra("list");

        namaFlora.setText(flora.getNamaFlora());
        namaLatin.setText(flora.getNamaLatin());

        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    speech.setLanguage(Locale.US);
                }
            }
        });

        img.setImageBitmap(blur(this, BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.flower)));
        speakerFlora.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speakerFlora:
                speech.speak(flora.getNamaFlora(), TextToSpeech.QUEUE_FLUSH, null);
                break;
        }
    }

    public Bitmap blur(Context context, Bitmap image) {
        float BITMAP_SCALE = 0.3f;
        float BLUR_RADIUS = 15f;
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmpIn);
        intrinsicBlur.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        Paint paint = new Paint();
        ColorFilter filter = new LightingColorFilter(Color.GRAY, 0);
        paint.setColorFilter(filter);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawBitmap(outputBitmap, 0, 0, paint);
        return outputBitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
}
