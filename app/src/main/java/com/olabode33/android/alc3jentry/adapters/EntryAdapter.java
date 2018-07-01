package com.olabode33.android.alc3jentry.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.olabode33.android.alc3jentry.Model.JournalEntry;
import com.olabode33.android.alc3jentry.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by obello004 on 6/30/2018.
 */

public class EntryAdapter extends RecyclerView.Adapter<EntryAdapter.EntryViewHolder> {
    private final Context mContext;
    final private EntryAdapterOnClickHandler mClickHandler;
    private Cursor mCursor;
    private List<JournalEntry> mJournalEntries;

    private static final String DATE_FORMATE = "dd/MM/yyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMATE, Locale.getDefault());

    public EntryAdapter(@NonNull Context context, EntryAdapterOnClickHandler handler){
        mContext = context;
        mClickHandler = handler;
        mJournalEntries = new ArrayList<JournalEntry>();
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.entry_list_items;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean attachToParentImmediately = false;

        View view = inflater.inflate(layoutId, parent, attachToParentImmediately);

        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        JournalEntry journalEntry = mJournalEntries.get(position);
        String entry = journalEntry.getEntry();
        String username = journalEntry.getUsername();
        String dateCreated = dateFormat.format(journalEntry.getCreatedAt());


    }

    @Override
    public int getItemCount() {
        if(mJournalEntries == null){
            return 0;
        }
        else{
            return mJournalEntries.size();
        }
    }

    public void setEntries(JournalEntry journalEntry){
        mJournalEntries.add(journalEntry);
        notifyDataSetChanged();
    }

    public interface EntryAdapterOnClickHandler {
        void onItemClickListener(String itemKey);
    }

    class EntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView mEntryDate;
        TextView mEntryText;

        public EntryViewHolder(View itemView) {
            super(itemView);

            mEntryDate = itemView.findViewById(R.id.tv_entry_summary_date);
            mEntryText = itemView.findViewById(R.id.tv_entry_summary_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String entryKey = mJournalEntries.get(getAdapterPosition()).getKey();
            mClickHandler.onItemClickListener(entryKey);
        }
    }
}
