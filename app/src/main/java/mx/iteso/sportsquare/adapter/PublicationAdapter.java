package mx.iteso.sportsquare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.sportsquare.ActivityComments;
import mx.iteso.sportsquare.R;
import mx.iteso.sportsquare.beans.Publication;

/**
 * Created by Desarrollo on 31/03/2018.
 */

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.ViewHolder> {
    private ArrayList<Publication> mDataSet;
    private Context context;

    public PublicationAdapter(Context context, ArrayList mDataSet) {
        this.context = context;
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_publication, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.publicationText.setText(mDataSet.get(position).getPublicationText());
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ActivityComments.class);
                intent.putExtra("idPublication", mDataSet.get(position).getId());
                intent.putExtra("publicationText", mDataSet.get(position).getPublicationText());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView publicationText;
        protected ImageView comment;

        public ViewHolder(View itemView) {
            super(itemView);
            publicationText = itemView.findViewById(R.id.item_card_publication_text);
            comment = itemView.findViewById(R.id.item_card_comment);
        }
    }
}
