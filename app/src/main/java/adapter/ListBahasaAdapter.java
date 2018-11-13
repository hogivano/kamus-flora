package adapter;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

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

    public ListBahasaAdapter(Context context, List<Flora> listFlora){
        this.context = context;
        this.listFlora = listFlora;
        listFloraClone = listFlora;
        cekSearch = true;
        db = Room.databaseBuilder(context, AppDatabase.class, "flora")
                .allowMainThreadQueries().build();
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

    public void addReload(List<Flora> list){
        listFloraClone = list;
        listFlora = list;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        TextView latin;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.nama);
            latin = (TextView) itemView.findViewById(R.id.latin);
        }
    }
}
