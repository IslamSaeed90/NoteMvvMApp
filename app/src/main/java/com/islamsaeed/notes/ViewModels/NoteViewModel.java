package com.islamsaeed.notes.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.islamsaeed.notes.DataBase.Note;
import com.islamsaeed.notes.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    /**
     * 1
     */
    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    /**
     * 2
     */
    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    /**
     * 3
     * create a repo methods for our db operations method for our repository
     */
    public void insert(Note note) {
        repository.insert(note);
    }

    public void update(Note note) {
        repository.update(note);
    }

    public void delete(Note note) {
        repository.delete(note);
    }

    public void deleteAllNotes() {
        repository.deleteAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }
}
