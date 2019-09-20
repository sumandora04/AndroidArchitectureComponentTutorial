package com.notepoint4ugmail.androidarchitecturecomponenttutorial;

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

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private static final int ADD_NOTE_REQ_VALUE = 1;
    private static final int EDIT_NOTE_REQ_VALUE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton addNoteFab = findViewById(R.id.add_note_btn);

        addNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);

                startActivityForResult(intent, ADD_NOTE_REQ_VALUE);
            }
        });

        recyclerView = findViewById(R.id.note_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);// List is empty here; list will be updated in Observer().

        // Getting a reference to the NoteViewModel.
        // Don't use new key word for instance creation.
        // Use ViewModelProviders and pass the activity/fragment and the ViewModel class
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        // To populate the recycler view and keep observing the data change use the observe() method.
        // observe() method will have two parameter:
        // 1. owner of the Lifecycle, which will be the fragment or activity.
        // 2. Observer
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // update the recyclerView here.
                // updating the list here.
               noteAdapter.submitList(notes); // submit list is a method of list adapter.
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

              noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //Implement the onClickListener interface
        noteAdapter.setOnClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());

                startActivityForResult(intent,EDIT_NOTE_REQ_VALUE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== ADD_NOTE_REQ_VALUE && resultCode== RESULT_OK){
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            noteViewModel.insert(note);

            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        }else if (requestCode== EDIT_NOTE_REQ_VALUE && resultCode== RESULT_OK){
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if (id==-1){
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            note.setId(id); // with out this step the update operation will not happen.
            noteViewModel.update(note);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_all_notes:
               noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }

}
