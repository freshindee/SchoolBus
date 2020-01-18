package com.fitscorp.apps.indika.schoolbus.login.adapter;

import android.app.Activity;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.fitscorp.apps.indika.schoolbus.R;
import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class AcceptCustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_FAMILY_MEMBER = 1;



    //instances
    // Declare Variables
    private List<SchoolListData> familyMemberlist = null;
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

    public AcceptCustomerAdapter(Activity activity, List<SchoolListData> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
        // pref=new PrefManager(activity);
    }


    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        TextView txt_school_name;
        ImageView img_accept;

        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            txt_school_name = itemView.findViewById(R.id.txt_school_name);
            img_accept= itemView.findViewById(R.id.img_accept);

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
        view = LayoutInflater.from(activity).inflate(R.layout.raw_approve_child_details, parent, false);
        return new FamilyMemberViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof SchoolListAdapter.FamilyMemberViewHolder) {

            SchoolListData obj = familyMemberlist.get(position);

            ((FamilyMemberViewHolder) holder).txt_school_name.setText(obj.getName());




        }

        final int pos = holder.getAdapterPosition();
        ((FamilyMemberViewHolder) holder).img_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FamilyMemberViewHolder) holder).img_accept.setBackgroundResource(R.drawable.accepted_button);
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (listener != null)
//                    listener.onItemClick(familyMemberlist.get(pos));
//            }
//        });
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


