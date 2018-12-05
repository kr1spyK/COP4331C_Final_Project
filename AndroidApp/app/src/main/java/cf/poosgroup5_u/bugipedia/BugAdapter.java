package cf.poosgroup5_u.bugipedia;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cf.poosgroup5_u.bugipedia.api.BugIDWrapper;
import cf.poosgroup5_u.bugipedia.utils.AppUtils;

/*
    RecyclerView.Adapter
    RecyclerView.ViewHolder
 */

public class BugAdapter extends RecyclerView.Adapter<BugAdapter.BugViewHolder> {

    private Context mCtx;
    private List<BugCard> bugList;

    public BugAdapter(Context mCtx, List<BugCard> bugList) {
        this.bugList = bugList;
        this.mCtx = mCtx;
    }


    @NonNull
    @Override
    public BugViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.card_layout, viewGroup, false);
        return new BugViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BugViewHolder holder, int i) {
        BugCard bug = bugList.get(i);

        holder.id = bug.getId();
        holder.textViewName.setText(bug.getName());
        holder.textViewSciName.setText(bug.getsci_Name());
        holder.bugID.setText(bug.getId().toString());
        AppUtils.loadImageIntoView(bug.getImage(), holder.imageView, mCtx);
    }

    @Override
    public int getItemCount() {
        return bugList.size();
    }

    class BugViewHolder extends RecyclerView.ViewHolder{

        //Pic of bug
        ImageView imageView;
        Integer id;

        //name and scientific name of bug to be displayed on card
        TextView textViewName, textViewSciName, bugID;

        public BugViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mCtx, ViewDBEntryActivity.class);
                    intent.putExtra(AppUtils.BUG_INFO_KEY, id);
                    mCtx.startActivity(intent);
                }
            });
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            bugID = itemView.findViewById(R.id.bugID);
            textViewSciName = itemView.findViewById(R.id.textViewSciName);

        }
    }
}
