package com.islamsaeed.notes.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.islamsaeed.notes.DataBase.Note;
import com.islamsaeed.notes.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter <Note,NoteAdapter.ViewHolder> {


    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback <Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {


        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {

            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getPriority()== newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final Note currentNote = getItem(position);
        viewHolder.titleTv.setText(currentNote.getTitle());
        viewHolder.descriptionTv.setText(currentNote.getDescription());
        viewHolder.priorityTv.setText(String.valueOf(currentNote.getPriority()));

        /**Use callBack*/
        if (onItemClickListener!=null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(currentNote, position);
                }
            });
        }



    }



//    @Override
//    public int getItemCount() {
//        return notes.size();
//    }

//    public void changeData (List<Note> notes){
//        this.notes=notes;
//        notifyDataSetChanged();
//    }

    /**To create a position to delete note in swipe to delete method in mainActivity
     * */
    public Note getNoteAt(int position){
        return getItem(position);
    }



    /**CallBack */
    public interface onItemClickListener {
        void onItemClick(Note note , int pos);
    }
    onItemClickListener onItemClickListener ;

    public void setOnItemClickListener(NoteAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /** ViewHolder*/
    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTv, descriptionTv, priorityTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.title);
            descriptionTv = itemView.findViewById(R.id.description);
            priorityTv = itemView.findViewById(R.id.priority);

        }
    }
}
