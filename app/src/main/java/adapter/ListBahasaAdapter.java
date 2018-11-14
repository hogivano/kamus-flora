package adapter;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import db.AppDatabase;
import db.Flora;
import mobtek.kel1.kamusflora.R;

/**
 * Created by root on 08/11/18.
 */

public class ListBahasaAdapter extends RecyclerView.Adapter<ListBahasaAdapter.CategoryViewHolder>{
    private Context context;
    private List<Flora> listFlora;
    private List<Flora> listFloraClone;
    private boolean cekSearch;
    private AppDatabase db;
    private String url;
    private String linkStorage;

    public ListBahasaAdapter(Context context, List<Flora> listFlora){
        this.context = context;
        this.listFlora = listFlora;
        listFloraClone = listFlora;
        cekSearch = true;
        db = Room.databaseBuilder(context, AppDatabase.class, "flora")
                .allowMainThreadQueries().build();
        url = "https://raw.githubusercontent.com/Ayun1998/kamus-tumbuhan/master/";
        linkStorage = "";
    }

    public void setCekSearch(boolean cekSearch){
        this.cekSearch = cekSearch;
    }

    public boolean isCekSearch() {
        return cekSearch;
    }

    public List<Flora> getListFlora(){
        return listFlora;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.nama.setText(listFlora.get(position).getNamaFlora());
        holder.latin.setText(listFlora.get(position).getNamaLatin());
        Glide.with(context)
                .load(url + listFlora.get(position).getId() + ".jpg")
                .apply(new RequestOptions()
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img))
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return getListFlora().size();
    }

    public Filter getFilterFlora() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterResults.values = listFloraClone;
                }
//                else {
//                    List<Flora> filteredList = new ArrayList<>();
//                    for (Flora row : listFloraClone) {
//                        if (cekSearch){
//                            if (row.getNamaFlora().toLowerCase().contains(charString.toLowerCase()) || row.getNamaFlora().contains(charSequence)) {
//                                filteredList.add(row);
//                            }
//                        } else {
//                            if (row.getNamaLatin().toLowerCase().contains(charString.toLowerCase()) || row.getNamaLatin().contains(charSequence)) {
//                                filteredList.add(row);
//                            }
//                        }
//                    }
//                    filterResults.values = filteredList;
//                }
                else {
//                    List<Flora> filteredList = new ArrayList<>();
//                    for (Flora row : db.dao().getAll()) {
//                        if (row.getNamaFlora().toLowerCase().contains(charString.toLowerCase()) || row.getNamaFlora().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
//                    }
//                    filterResults.values = filteredList;
                    filterResults.values = db.dao().findByNamaFlora(charString);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFlora = (ArrayList<Flora>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public Filter getFilterLatin() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterResults.values = listFloraClone;
                }
//                else {
//                    List<Flora> filteredList = new ArrayList<>();
//                    for (Flora row : listFloraClone) {
//                        if (cekSearch){
//                            if (row.getNamaFlora().toLowerCase().contains(charString.toLowerCase()) || row.getNamaFlora().contains(charSequence)) {
//                                filteredList.add(row);
//                            }
//                        } else {
//                            if (row.getNamaLatin().toLowerCase().contains(charString.toLowerCase()) || row.getNamaLatin().contains(charSequence)) {
//                                filteredList.add(row);
//                            }
//                        }
//                    }
//                    filterResults.values = filteredList;
//                }
                else {
//                    List<Flora> filteredList = new ArrayList<>();
//                    for (Flora row : db.dao().getAll()) {
//                        if (row.getNamaFlora().toLowerCase().contains(charString.toLowerCase()) || row.getNamaFlora().contains(charSequence)) {
//                            filteredList.add(row);
//                        }
//                    }
//                    filterResults.values = filteredList;
                    filterResults.values = db.dao().findByNamaLatin(charString);
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFlora = (ArrayList<Flora>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public void addReload(List<Flora> list){
        listFloraClone = list;
        listFlora = list;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        TextView latin;
        ImageView img;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.nama);
            latin = (TextView) itemView.findViewById(R.id.latin);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
