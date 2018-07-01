package com.olabode33.android.alc3jentry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.olabode33.android.alc3jentry.Model.JournalEntry;
import com.olabode33.android.alc3jentry.adapters.JournalEntryViewHolder;
import com.olabode33.android.alc3jentry.utils.EntryDateUtils;

public class AllEntriesActivity extends AppCompatActivity {

    private static Boolean persistenceEnabled = false;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserEntriesDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private RecyclerView mAllEntriesRecyclerView;
    private ProgressBar mLoadingProgressBar;
    private FirebaseRecyclerAdapter<JournalEntry, JournalEntryViewHolder> mFirebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_entries);

        if (!persistenceEnabled) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFirebaseUser == null) {
            Intent intent = new Intent(this, GoogleSigninActivity.class);
            startActivity(intent);
            finish();
        }

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserEntriesDatabaseReference = mFirebaseDatabase.getReference().child(getString(R.string.firebase_database_reference)).child(mFirebaseUser.getUid());
        mUserEntriesDatabaseReference.keepSynced(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = getApplicationContext();
                Intent intent = new Intent(context, NewEntryActivity.class);
                startActivity(intent);
            }
        });

        mLoadingProgressBar = findViewById(R.id.pb_loading_entries);

        mAllEntriesRecyclerView = findViewById(R.id.rv_all_entries);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mAllEntriesRecyclerView.setLayoutManager(layoutManager);

        //mAdapter = new EntryAdapter(this, this);
        //mAllEntriesRecyclerView.setAdapter(mAdapter);

        FirebaseRecyclerOptions<JournalEntry> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<JournalEntry>()
                .setQuery(mUserEntriesDatabaseReference, JournalEntry.class)
                .setLifecycleOwner(this)
                .build();

        mFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<JournalEntry, JournalEntryViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull JournalEntryViewHolder holder, int position, @NonNull JournalEntry model) {
                String modelKey = getRef(position).getKey();
                populateUI(holder, model, modelKey);
            }

            @NonNull
            @Override
            public JournalEntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                int layoutId = R.layout.entry_list_items;
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                boolean attachToParentImmediately = false;

                View view = inflater.inflate(layoutId, parent, attachToParentImmediately);

                return new JournalEntryViewHolder(view);
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                if (mLoadingProgressBar != null && mLoadingProgressBar.isShown()) {
                    mLoadingProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        };

        mAllEntriesRecyclerView.setAdapter(mFirebaseRecyclerAdapter);
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        mAllEntriesRecyclerView.addItemDecoration(decoration);
        //handleChildEventListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.all_entries_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //handleChildEventListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //detachChildEventListener();
    }

    private void populateUI(JournalEntryViewHolder holder, JournalEntry model, String modelKey) {
        final String key = modelKey;
        model.setKey(modelKey);

        String fullDate = model.getCreatedAt();

        EntryDateUtils dateUtils = new EntryDateUtils(fullDate);

        String slimDate = dateUtils.getSlimDateFormat();
        String dayTime = dateUtils.getDayTimeDateFormat();

        holder.setEntryTime(dayTime);
        holder.setEntryDate(slimDate);
        holder.setEntryText(model.getEntry());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryDetailsActivity.class);
                intent.putExtra(EntryDetailsActivity.EXTRA_ENTRY_KEY, key);
                startActivity(intent);
            }
        });
    }
}
