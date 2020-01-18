
package com.fitscorp.apps.indika.schoolbus.login.adapter;

        import android.app.Activity;


        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import com.fitscorp.apps.indika.schoolbus.R;
        import com.fitscorp.apps.indika.schoolbus.model.ChildMainData;
        import com.fitscorp.apps.indika.schoolbus.model.SchoolListData;
        import com.fitscorp.apps.indika.schoolbus.requests.SendBusRequestAdapter;

        import java.util.List;

        import androidx.recyclerview.widget.RecyclerView;

public class ChildListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //constants
    private static final int VIEW_TYPE_FAMILY_MEMBER = 1;



    //instances
    // Declare Variables
    private List<ChildMainData> familyMemberlist = null;
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

    public ChildListAdapter(Activity activity, List<ChildMainData> worldpopulationlist) {
        this.familyMemberlist = worldpopulationlist;
        this.activity = activity;
        // pref=new PrefManager(activity);
    }


    class FamilyMemberViewHolder extends RecyclerView.ViewHolder {
        TextView txt_school_name;
        TextView txt_child_name;

        FamilyMemberViewHolder(View itemView) {
            super(itemView);
            txt_child_name = itemView.findViewById(R.id.txt_child_name);
            txt_school_name = itemView.findViewById(R.id.txt_school_name);
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
        view = LayoutInflater.from(activity).inflate(R.layout.raw_child_cell, parent, false);
        return new FamilyMemberViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,final int position) {
        if (holder instanceof FamilyMemberViewHolder) {

            ChildMainData obj = familyMemberlist.get(position);


            ((FamilyMemberViewHolder) holder).txt_child_name.setText(obj.getName());
            String grade="Garde : "+obj.getGrade();
            ((FamilyMemberViewHolder) holder).txt_school_name.setText(grade);

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

