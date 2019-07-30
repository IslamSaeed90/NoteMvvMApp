package com.islamsaeed.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.islamsaeed.notes.Adapters.NoteAdapter;
import com.islamsaeed.notes.DataBase.Note;
import com.islamsaeed.notes.ViewModels.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Note> notes;

    public static final int ADD_NOTE_REQUEST_CODE = 1;
    public static final int EDIT_NOTE_REQUEST_CODE = 2;
    /**
     * 1
     */
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        initRecyclerView();

        /** 4
         * To add note*/
        FloatingActionButton buttonAddNote = findViewById(R.id.floating_addBtn);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST_CODE);
            }
        });


        /**3*/
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final NoteAdapter adapter = new NoteAdapter();

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note, int pos) {
                Intent i = new Intent(MainActivity.this, AddEditNoteActivity.class);
                i.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                i.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                i.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                i.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());

                startActivityForResult(i, EDIT_NOTE_REQUEST_CODE);

            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /** 2*/
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update our recyclerView
                adapter.submitList(notes);
            }
        });
        /**6
         * To swipe to delete*/
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
//            @Override
//            public void onChildDraw(Canvas c, RecyclerView recyclerView,
//                                    RecyclerView.ViewHolder viewHolder,
//                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
//                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//                final ColorDrawable background = new ColorDrawable(Color.RED);
//                background.setBounds(0, recyclerView.getTop(), (int) (recyclerView.getLeft() + dX), recyclerView.getBottom());
//                background.draw(c);
//
//            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
//                Intent data = new Intent();
//                String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
//                String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
//                int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);
//
//                final Note note = new Note(title, description, priority);
//
//                /* دي خطوة ال Undo  واني احطها في سناك بار */
//                Snackbar.make(viewHolder.itemView, "Note been deleted ", Snackbar.LENGTH_LONG)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                NoteDataBase.getInstance(MainActivity.this)
//                                        .noteDAO();
//                                noteViewModel.insert(note);
//                                notes=new ArrayList<>();
//
//                                notes.add(viewHolder.getAdapterPosition(),note);
//                                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
//
//                            }
//                        }).show();
//                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }


    /**
     * 7
     * To delete all notes
     * We want to confirm the input when we clicked the save menu icon
     * in the top right corner in the action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * To handel a clicks in our menu items و or in our save icon here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * 5
     * continue : To add note
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();

        }
        /** To handel edit note after added step num 10 in AddEditNoteActivity Class*/
        else if (requestCode == EDIT_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }


    }
}
