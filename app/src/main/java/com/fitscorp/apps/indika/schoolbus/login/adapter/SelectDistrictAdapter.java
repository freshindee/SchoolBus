package com.fitscorp.apps.indika.schoolbus.login.adapter;

import android.app.Activity;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.fitscorp.apps.indika.schoolbus.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SelectDistrictAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_FAMILY_MEMBER = 1;



    //instances
    // Declare Variables
    private List<String> familyMemberlist = null;
    private Activity activity;
    private OnItemClickListener listener;
  //  private PrefManager pref;


    private OnClickAssignUserImage listener_delete;



    public void setOnSelectAssignUserListener(OnClickAssignUserImage listener) {
        this.listener_delete = listener;
    }
    //    Interface For Events Handing ...........



    public interface OnClickAssignUserImage {
        void onSelectAssignUser(String obj, int pos);
    }

    public SelectDistrictAdapter(Activity activity, List<String> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
       // pref=new PrefManager(activity);
    }


    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        TextView text_name;


        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.txt_name);

        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
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

            String cityName = familyMemberlist.get(position);

            ((FamilyMemberViewHolder) holder).text_name.setText(cityName);

        }

        final int pos = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null)
                    listener.onItemClick(familyMemberlist.get(pos));
            }
        });

    }


    @Override
    public int getItemCount() {
        if (familyMemberlist == null)
            return 0;

        else
            return familyMemberlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (familyMemberlist == null)
            return VIEW_TYPE_FAMILY_MEMBER;
        else
            return VIEW_TYPE_FAMILY_MEMBER;
    }
    public interface OnItemClickListener {
        void onItemClick(Object object);
    }




}
