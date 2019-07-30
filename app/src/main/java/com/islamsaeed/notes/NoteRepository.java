package com.islamsaeed.notes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.islamsaeed.notes.DataBase.Note;
import com.islamsaeed.notes.DataBase.NoteDAO;
import com.islamsaeed.notes.DataBase.NoteDataBase;

import java.util.List;

public class NoteRepository {

    /**
     * 1
     */
    private NoteDAO noteDAO;
    private LiveData<List<Note>> allNotes;

    /**
     * 2 / create constructor
     */
    public NoteRepository(Application application) {
        NoteDataBase dataBase = NoteDataBase.getInstance(application);
        noteDAO = dataBase.noteDAO();
        allNotes = noteDAO.getAllNotes();
    }

    /**
     * 3 / create a methods for all our different database operations
     */

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDAO).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDAO).execute(note);

    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDAO).execute(note);

    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDAO).execute();

    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    /**
     * Then we have to execute the code in
     * background thread for those methods , because Room doesn't allow
     * db operations in the main thread
     */

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO noteDAO;

        public InsertNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.insert(notes[0]);
            return null;
        }
    }


    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO noteDAO;

        public UpdateNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.update(notes[0]);
            return null;
        }

    }


    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDAO noteDAO;

        public DeleteNoteAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.delete(notes[0]);
            return null;
        }

    }


    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDAO noteDAO;

        public DeleteAllNotesAsyncTask(NoteDAO noteDAO) {
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.deleteAllNotes();
            return null;
        }

    }


}





