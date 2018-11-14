package mobtek.kel1.kamusflora;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Switch;
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

public class MainActivity extends AppCompatActivity{
    String [] flora;
    String [] latin;
    List<Flora> list;
    AppDatabase db;
    RecyclerView rvFlora;
    ListBahasaAdapter listBahasaAdapter;
    android.support.v7.widget.SearchView searchView;
    SwipyRefreshLayout swipyRefreshLayout;
    Switch aSwitch;
    ScrollView scrollView;
    RelativeLayout rlObservable;
    private int mParallaxImageHeight;
    boolean checked;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvFlora = (RecyclerView) findViewById(R.id.rvFlora);
        searchView = (android.support.v7.widget.SearchView) findViewById(R.id.searchView);
        rlObservable = (RelativeLayout) findViewById(R.id.rlObservable);
        EditText searchEditText = (EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.greenLight));
        searchEditText.setHintTextColor(getResources().getColor(R.color.greenLight));
        swipyRefreshLayout = (SwipyRefreshLayout) findViewById(R.id.swipyrefreshlayout);
        aSwitch = (Switch) findViewById(R.id.switch1);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        list = new ArrayList<>();

        checked = aSwitch.isChecked();

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "flora")
                .allowMainThreadQueries().build();

        if (db.dao().getAll().size() == 0){
            db.dao().insertAll((List<Flora>) getIntent().getSerializableExtra("list"));
            list = db.dao().getLimit50();
        } else {
            list = db.dao().getLimit50();
        }

        setAdapter();
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (checked == false){
                    listBahasaAdapter.getFilterFlora().filter(query);
                } else {
                    listBahasaAdapter.getFilterLatin().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (checked == false){
                    listBahasaAdapter.getFilterFlora().filter(query);
                } else {
                    listBahasaAdapter.getFilterLatin().filter(query);
                }
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

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checked = isChecked;
                if (checked == false){
                    Toast.makeText(MainActivity.this, "Pencarian berdasarkan nama tumbuhan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Pencarian berdasarkan nama latin", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}
