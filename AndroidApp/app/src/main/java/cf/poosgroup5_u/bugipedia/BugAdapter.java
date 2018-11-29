package cf.poosgroup5_u.bugipedia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

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
        View view = inflater.inflate(R.layout.card_layout, null);
        return new BugViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BugViewHolder holder, int i) {
        BugCard bug = bugList.get(i);

        holder.textViewName.setText(bug.getName());
        holder.textViewSciName.setText(bug.getsci_Name());
        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(bug.getImage()));

    }

    @Override
    public int getItemCount() {
        return bugList.size();
    }

    class BugViewHolder extends RecyclerView.ViewHolder{

        //Pic of bug
        ImageView imageView;

        //name and scientific name of bug to be displayed on card
        TextView textViewName, textViewSciName;

        public BugViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSciName = itemView.findViewById(R.id.textViewSciName);

        }
    }
}
