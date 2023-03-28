package com.example.qrhunter.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.R;
import com.example.qrhunter.data.model.Comment;

import java.util.ArrayList;

/**
 * Adapter for recycler view for QR Codes. Used in profile pages
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private ArrayList<Comment> comments;
    private onClickListeners listeners;

    /**
     * Constructor for CommentAdapter
     *
     * @param comments The data for this should be retrieved from a ViewModel class
     */
    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);

        holder.commentAuthor.setText(comment.getAuthor());
        holder.commentContent.setText(comment.getContent());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void setOnClickListeners(onClickListeners listeners) {
        this.listeners = listeners;
    }

    /**
     * Listener interface for parent fragment / activity to implement
     */
    public interface onClickListeners {
        void onDeleteButtonClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView commentAuthor;
        public TextView commentContent;

        public ViewHolder(View itemView) {
            super(itemView);

            commentAuthor = itemView.findViewById(R.id.comment_author);
            commentContent = itemView.findViewById(R.id.comment_content);

        }


    }

}
