package com.olabode33.android.alc3jentry;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.olabode33.android.alc3jentry.Model.JournalEntry;

public class NewEntryActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessageDatabaseReference;

    private EditText mNewEntry;

    private String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessageDatabaseReference = mFirebaseDatabase.getReference().child("messages");

        mNewEntry = findViewById(R.id.et_new_entry);
        mUsername = "Anonymous";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_save:
                saveEntry("");
                break;

            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveEntry(String userID){
        JournalEntry entry = new JournalEntry(mNewEntry.getText().toString(), mUsername, null, null);

        mMessageDatabaseReference.push().setValue(entry);
        Intent intent = new Intent(this, AllEntriesActivity.class);
        startActivity(intent);
    }
}
