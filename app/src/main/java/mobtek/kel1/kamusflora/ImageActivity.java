package mobtek.kel1.kamusflora;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import db.Flora;

public class ImageActivity extends AppCompatActivity {

    PhotoView photoView;
    PhotoViewAttacher photoViewAttacher;
    String url;
    Flora listFlora;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        photoView = (PhotoView) findViewById(R.id.photoView);
        url = "https://raw.githubusercontent.com/Ayun1998/kamus-tumbuhan/master/";

        listFlora = (Flora) getIntent().getSerializableExtra("list");

        Glide.with(this)
                .load(url + listFlora.getId() + ".jpg")
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.no_img)
                        .error(R.drawable.no_img)
                        .dontAnimate())
                .into(photoView);
    }
}
