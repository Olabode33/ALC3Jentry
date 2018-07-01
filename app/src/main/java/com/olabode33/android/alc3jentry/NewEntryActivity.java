package com.olabode33.android.alc3jentry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.olabode33.android.alc3jentry.Model.JournalEntry;
import com.olabode33.android.alc3jentry.utils.EntryDateUtils;

import java.util.Date;

public class NewEntryActivity extends AppCompatActivity {

    public static final String EXTRA_ENTRY_KEY = "EXTRA_ENTRY_KEY";
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserEntriesDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private EditText mNewEntry;
    private String mUsername;
    private boolean isEdit = false;
    private String mEntryKey = "";
    private JournalEntry mJournalEntry;
    private ValueEventListener mValueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(this, GoogleSigninActivity.class);
            startActivity(intent);
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserEntriesDatabaseReference = mFirebaseDatabase.getReference().child(getString(R.string.firebase_database_reference)).child(mFirebaseUser.getUid());

        mNewEntry = findViewById(R.id.et_new_entry);
        mUsername = mFirebaseUser.getDisplayName();

        Intent edit_intent = getIntent();
        if (edit_intent != null && edit_intent.hasExtra(EXTRA_ENTRY_KEY)) {
            isEdit = true;
            mEntryKey = edit_intent.getStringExtra(EXTRA_ENTRY_KEY);
            ActionBar supportActionBar = getSupportActionBar();
            supportActionBar.setTitle(R.string.edit_entry_title);
            attachValueEventListener();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_entry_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                saveEntry();
                break;

            default:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveEntry() {
        if (isEdit && mEntryKey != null) {
            mJournalEntry.setEntry(mNewEntry.getText().toString());
            mUserEntriesDatabaseReference.child(mEntryKey).setValue(mJournalEntry)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent editSuccessIntent = new Intent(NewEntryActivity.this, EntryDetailsActivity.class);
                            editSuccessIntent.putExtra(EntryDetailsActivity.EXTRA_ENTRY_KEY, mEntryKey);
                            startActivity(editSuccessIntent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String errToastMsg = "Error updating entry: " + e.getMessage();
                            Toast toast = Toast.makeText(NewEntryActivity.this, errToastMsg, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
        } else {
            String dateCreated = EntryDateUtils.getCurrentDateDefaultFormat();
            JournalEntry entry = new JournalEntry(mNewEntry.getText().toString(), mUsername, dateCreated, null);

            mUserEntriesDatabaseReference.push().setValue(entry)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(NewEntryActivity.this, AllEntriesActivity.class);
                            startActivity(intent);
                        }


                    }).
                    addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String errToastMsg = "Error adding entry: " + e.getMessage();
                            Toast toast = Toast.makeText(NewEntryActivity.this, errToastMsg, Toast.LENGTH_LONG);
                            toast.show();
                        }
                    });
        }
    }

    private void attachValueEventListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mJournalEntry = dataSnapshot.child(mEntryKey).getValue(JournalEntry.class);
                    populateUI(mJournalEntry);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w("NewEntryActivity: ", "loadPost:onCancelled", databaseError.toException());
                }
            };
            mUserEntriesDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
        }
    }

    private void populateUI(JournalEntry mJournalEntry) {
        mNewEntry.setText(mJournalEntry.getEntry());
    }
}
