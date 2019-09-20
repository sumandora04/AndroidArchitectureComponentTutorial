package com.notepoint4ugmail.androidarchitecturecomponenttutorial;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        // Instantiate the NoteDatabase object.
        NoteDatabase database = NoteDatabase.getDatabaseInstance(application);
        // Accessing the NoteDao interface through NoteDatabase abstract class
        noteDao = database.noteDao();
        // Accessing interface methods here.
        allNotes = noteDao.getAllNotes();

    }

    //Creating all database operations:
    public void insert(Note note){
        new InsertAsyncTask(noteDao).execute(note);
    }

    public void update(Note note){
        new UpdateAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note){
        new DeleteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>>getAllNotes(){
        return allNotes;
    }


    public static class InsertAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;

        public InsertAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }


    public static class UpdateAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;

        public UpdateAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDao noteDao;

        public DeleteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    public static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void>{

        private NoteDao noteDao;

        public DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
