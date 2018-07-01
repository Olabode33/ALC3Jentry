package com.olabode33.android.alc3jentry.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.olabode33.android.alc3jentry.R;

/**
 * Created by obello004 on 6/30/2018.
 */

public class JournalEntryViewHolder extends RecyclerView.ViewHolder {
    public View mView;
    private TextView mEntryDate;
    private TextView mEntryText;
    private TextView mEntryTime;

    public JournalEntryViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mEntryDate = itemView.findViewById(R.id.tv_entry_summary_date);
        mEntryText = itemView.findViewById(R.id.tv_entry_summary_text);
        mEntryTime = itemView.findViewById(R.id.tv_entry_summary_time);
    }

    public void setEntryDate(String date) {
        mEntryDate.setText(date);
    }

    public void setEntryTime(String time) {
        mEntryTime.setText(time);
    }

    public void setEntryText(String entry) {
        mEntryText.setText(entry);
    }
}
