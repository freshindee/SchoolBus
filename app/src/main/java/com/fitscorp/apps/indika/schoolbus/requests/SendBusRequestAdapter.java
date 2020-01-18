package com.fitscorp.apps.indika.schoolbus.requests;

import android.app.Activity;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.login.adapter.SelectDistrictAdapter;
import com.fitscorp.apps.indika.schoolbus.model.SchoolBusData;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class SendBusRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_FAMILY_MEMBER = 1;



    //instances
    // Declare Variables
    private List<SchoolBusData> familyMemberlist = null;
    private Activity activity;
    private SelectDistrictAdapter.OnItemClickListener listener;
    //  private PrefManager pref;


    private SelectDistrictAdapter.OnClickAssignUserImage listener_delete;



    public void setOnSelectAssignUserListener(SelectDistrictAdapter.OnClickAssignUserImage listener) {
        this.listener_delete = listener;
    }
    //    Interface For Events Handing ...........



    public interface OnClickAssignUserImage {
        void onSelectAssignUser(String obj, int pos);
    }

    public SendBusRequestAdapter(Activity activity, List<SchoolBusData> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
        // pref=new PrefManager(activity);
    }


    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        TextView txt_bus_number;
        TextView txt_from;
        TextView txt_to;
        TextView txt_time;

        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            txt_bus_number = itemView.findViewById(R.id.txt_school_name);
            txt_from = itemView.findViewById(R.id.txt_from);
            txt_to = itemView.findViewById(R.id.txt_to);
            txt_time = itemView.findViewById(R.id.txt_time);

        }
    }

    public void setOnItemClickListener(SelectDistrictAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        System.out.println("========================onCreateViewHolder");
        view = LayoutInflater.from(activity).inflate(R.layout.raw_select_school_bus_layout, parent, false);
        return new SendBusRequestAdapter.FamilyMemberViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof FamilyMemberViewHolder) {

            SchoolBusData obj = familyMemberlist.get(position);
            ((FamilyMemberViewHolder) holder).txt_bus_number.setText(obj.getBusNumber());

            String from="From : "+obj.getStartAddress();
            ((FamilyMemberViewHolder) holder).txt_from.setText(from);
            String roadDetails="To : "+obj.getCrossCities()+", "+obj.getEndAddress();
            ((FamilyMemberViewHolder) holder).txt_to.setText(roadDetails);
            String time="Time : "+obj.getTimeDesc();
            ((FamilyMemberViewHolder) holder).txt_time.setText(time);

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

