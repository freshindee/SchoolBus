package com.fitscorp.apps.indika.schoolbus.login.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.model.City;

import java.util.ArrayList;
import java.util.List;


public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_FAMILY_MEMBER = 1;



    //instances
    // Declare Variables
    private List<City> familyMemberlist = null;
    private List<City> contactListFiltered = null;
    private List<City> reportsList = null;

    private Activity activity;
    private CityAdapter.OnItemClickListener listener;
    //  private PrefManager pref;


    private CityAdapter.OnClickAssignUserImage listener_delete;



    public void setOnSelectAssignUserListener(CityAdapter.OnClickAssignUserImage listener) {
        this.listener_delete = listener;
    }
    //    Interface For Events Handing ...........



    public interface OnClickAssignUserImage {
        void onSelectAssignUser(String obj, int pos);
    }

    public CityAdapter(Activity activity, List<City> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
        // pref=new PrefManager(activity);
        List<City> filteredList = new ArrayList<>();
        contactListFiltered = new ArrayList<>();
        reportsList = new ArrayList<>();
        reportsList=worldpopulationlist;
        contactListFiltered=worldpopulationlist;
    }


    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_child_address;
        ImageView img_call;

        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);

        }
    }

    public void setOnItemClickListener(CityAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        System.out.println("========================onCreateViewHolder");
        view = LayoutInflater.from(activity).inflate(R.layout.raw_city_layout, parent, false);
        return new FamilyMemberViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof FamilyMemberViewHolder) {

            City obj = contactListFiltered.get(position);

            ((FamilyMemberViewHolder) holder).txt_name.setText(obj.getName());
         //   ((FamilyMemberViewHolder) holder).txt_child_address.setText("No 234, Park Road, Colombo 5");

        }

        final int pos = holder.getAdapterPosition();
        ((FamilyMemberViewHolder) holder).txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(contactListFiltered.get(pos));
            }
        });

    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = reportsList;
                } else {
                    List<City> filteredList = new ArrayList<>();
                    for (City row : reportsList) {
                            if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                            }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                //   ArrayList<Object> songsLis = (ArrayList<Object>) filterResults.values;

                //  contactListFiltered=Utility_Report.getreportDataList2(firstReportID,songsLis);

                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        if (contactListFiltered == null)
            return 0;

        else
            return contactListFiltered.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (contactListFiltered == null)
            return VIEW_TYPE_FAMILY_MEMBER;
        else
            return VIEW_TYPE_FAMILY_MEMBER;
    }
    public interface OnItemClickListener {
        void onItemClick(Object object);
    }




}

