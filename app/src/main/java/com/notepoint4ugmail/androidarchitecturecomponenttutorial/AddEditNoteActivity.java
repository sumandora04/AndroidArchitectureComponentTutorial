package com.notepoint4ugmail.androidarchitecturecomponenttutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Objects;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.notepoint4ugmail.androidarchitecturecomponenttutorial.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.notepoint4ugmail.androidarchitecturecomponenttutorial.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.notepoint4ugmail.androidarchitecturecomponenttutorial.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.notepoint4ugmail.androidarchitecturecomponenttutorial.EXTRA_ID";

    private EditText title, description;
    private NumberPicker priorityPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.edit_title_edt);
        description = findViewById(R.id.edit_description_edt);
        priorityPicker = findViewById(R.id.priority_picker);

        priorityPicker.setMinValue(1);
        priorityPicker.setMaxValue(10);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priorityPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));
        }else {
            setTitle("Add Note");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    private void saveNote() {
        String str_title = title.getText().toString().trim();
        String str_description = description.getText().toString().trim();
        int priority = priorityPicker.getValue();

        if (str_title.isEmpty() || str_description.isEmpty()){
            Toast.makeText(this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intentData = new Intent();
        intentData.putExtra(EXTRA_TITLE,str_title);
        intentData.putExtra(EXTRA_DESCRIPTION,str_description);
        intentData.putExtra(EXTRA_PRIORITY,priority);

        // For update only.
        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id!=-1){
            intentData.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK,intentData);

        finish();
    }
}
