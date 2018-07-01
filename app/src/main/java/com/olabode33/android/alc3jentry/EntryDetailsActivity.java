package com.olabode33.android.alc3jentry;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.olabode33.android.alc3jentry.Model.JournalEntry;
import com.olabode33.android.alc3jentry.utils.EntryDateUtils;

public class EntryDetailsActivity extends AppCompatActivity {

    public static String EXTRA_ENTRY_KEY = "ENTRY_KEY";
    public static String LOG_TAG = "LOG_TAG";

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserEntriesDatabaseReference;
    private FirebaseUser mFirebaseUser;

    private JournalEntry mJournalEntry;
    private String mEntryKeyExtra;

    private ValueEventListener mValueEventListener;

    private TextView mDateTextView;
    private TextView mEntryTextView;
    private TextView mTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ENTRY_KEY)) {
            mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            mEntryKeyExtra = intent.getStringExtra(EXTRA_ENTRY_KEY);
            if (mFirebaseUser != null) {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mUserEntriesDatabaseReference = mFirebaseDatabase.getReference().child(getString(R.string.firebase_database_reference)).child(mFirebaseUser.getUid());
                attachValueEventListener();
            }
        }

        mDateTextView = findViewById(R.id.tv_entry_date);
        mEntryTextView = findViewById(R.id.tv_entry_context);
        mTimeTextView = findViewById(R.id.tv_entry_time);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.entry_details_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //attachValueEventListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //detachValueEventListener();
    }

    private void populateUI(JournalEntry entry) {
        if (entry != null) {
            String fullDate = entry.getCreatedAt();

            EntryDateUtils dateUtils = new EntryDateUtils(fullDate);
            String slimDate = dateUtils.getSlimDateFormat();
            String dayTime = dateUtils.getDayTimeDateFormat();

            mDateTextView.setText(slimDate);
            mTimeTextView.setText(dayTime);
            mEntryTextView.setText(entry.getEntry());
        } else {
            return;
        }
    }

    private void detachValueEventListener() {
        if (mValueEventListener != null) {
            mUserEntriesDatabaseReference.removeEventListener(mValueEventListener);
            mValueEventListener = null;
        }
    }

    private void attachValueEventListener() {
        if (mValueEventListener == null) {
            mValueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mJournalEntry = dataSnapshot.child(mEntryKeyExtra).getValue(JournalEntry.class);
                    populateUI(mJournalEntry);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(LOG_TAG, "loadPost:onCancelled", databaseError.toException());
                }
            };
            mUserEntriesDatabaseReference.addListenerForSingleValueEvent(mValueEventListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_action:
                Intent intent = new Intent(this, NewEntryActivity.class);
                intent.putExtra(NewEntryActivity.EXTRA_ENTRY_KEY, mEntryKeyExtra);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
