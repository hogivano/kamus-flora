package mobtek.kel1.kamusflora;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.omadahealth.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import adapter.ListBahasaAdapter;
import db.AppDatabase;
import db.Flora;
import util.ItemClickSupport;

public class MainActivity extends AppCompatActivity implements ObservableScrollViewCallbacks{
    String [] flora;
    String [] latin;
    List<Flora> list;
    AppDatabase db;
    RecyclerView rvFlora;
    ListBahasaAdapter listBahasaAdapter;
    SearchView searchView;
    SwipyRefreshLayout swipyRefreshLayout;
    ObservableScrollView observableScrollView;
    RelativeLayout rlObservable;

    private int mParallaxImageHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvFlora = (RecyclerView) findViewById(R.id.rvFlora);
        searchView = (SearchView) findViewById(R.id.searchView);
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        observableScrollView = (ObservableScrollView) findViewById(R.id.observebleScrollView);
        rlObservable = (RelativeLayout) findViewById(R.id.rlObservable);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        list = new ArrayList<>();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "flora")
                .allowMainThreadQueries().build();

        if (db.dao().getAll().size() == 0){
            db.dao().insertAll((List<Flora>) getIntent().getSerializableExtra("list"));
            list = db.dao().getLimit50();
        } else {
            list = db.dao().getLimit50();
            Toast.makeText(this, "Tidak Kosong", Toast.LENGTH_SHORT).show();
        }

        setAdapter();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listBahasaAdapter.getFilterFlora().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listBahasaAdapter.getFilterFlora().filter(newText);
                return false;
            }
        });

        swipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                list.addAll(db.dao().getContinueLimit(list.get(list.size()-1).getId()));
                listBahasaAdapter.addReload(list);
                swipyRefreshLayout.setRefreshing(false);
            }
        });

        observableScrollView.setScrollViewCallbacks(this);
    }

    public void setAdapter(){
        rvFlora.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        listBahasaAdapter = new ListBahasaAdapter(this, list);
        rvFlora.setAdapter(listBahasaAdapter);

        ItemClickSupport.addTo(rvFlora).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("list", listBahasaAdapter.getListFlora().get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(observableScrollView.getCurrentScrollY(), false, false);
    }

    /**
     * Called when the scroll change events occurred.
     * This won't be called just after the view is laid out, so if you'd like to
     * initialize the position of your views with this method, you should call this manually
     * or invoke scroll as appropriate.
     *
     * @param scrollY     scroll position in Y axis
     * @param firstScroll true when this is called for the first time in the consecutive motion events
     * @param dragging    true when the view is dragged and false when the view is scrolled in the inertia
     */
    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.colorAccent);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        ViewHelper.setTranslationY(rlObservable, scrollY / 2);
    }

    /**
     * Called when the down motion event occurred.
     */
    @Override
    public void onDownMotionEvent() {

    }

    /**
     * Called when the dragging ended or canceled.
     *
     * @param scrollState state to indicate the scroll direction
     */
    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
