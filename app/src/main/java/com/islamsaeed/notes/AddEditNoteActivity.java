package com.islamsaeed.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {

    /**
     * 7
     */
    public static final String EXTRA_TITLE = "com.islamsaeed.notes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.islamsaeed.notes.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.islamsaeed.notes.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.islamsaeed.notes.EXTRA_ID";

    /**
     * 1
     */
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        /**2*/
        editTextTitle = findViewById(R.id.edit_text_tite);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);

        /**4*/
        /**To get that little x in the top left corner*/
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        /**
         * 9
         * To edit note (catch putExtra from MainActivity )
         *
         * after we done here we will go to saveNote method  */

        Intent i = getIntent();
        if (i.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTextTitle.setText(i.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(i.getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(i.getIntExtra(EXTRA_TITLE, 1));
        } else {
            setTitle("Add Note");

        }

    }

    /**
     * 8
     * To save Note
     */
    private void saveNote() {
        /**first we have to get the inputs from the editText field and number picker */
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please Insert a Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        /**
         * 10
         * continuing edit note after step num 9*/
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        /**setResult to indicate if the input was successfully or not */
        setResult(RESULT_OK, data);
        finish();


    }

    /**
     * 5
     * We want to confirm the input when we clicked the save menu icon
     * in the top right corner in the action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    /**
     * 6
     * To handel a clicks in our menu items و or in our save icon here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
