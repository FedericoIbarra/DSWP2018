package mx.iteso.sportsquare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mx.iteso.sportsquare.R;
import mx.iteso.sportsquare.beans.Comment;

/**
 * Created by Desarrollo on 05/05/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> mDataSet;
    private Context context;

    public CommentAdapter(Context context, ArrayList mDataSet){
        this.context = context;
        this.mDataSet = mDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.commentText.setText(mDataSet.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView commentText;

        public ViewHolder(View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.item_card_comment_text);
        }
    }
}
