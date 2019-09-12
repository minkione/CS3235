package com.akta.android.util.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Comparator;

public class SimpleSectionedListAdapter extends BaseAdapter {
    protected ListAdapter mBaseAdapter;
    private LayoutInflater mLayoutInflater;
    private int mSectionResourceId;
    protected SparseArray<Section> mSections = new SparseArray<>();
    /* access modifiers changed from: private */
    public boolean mValid = true;

    public static class Section {
        int firstPosition;
        int sectionedPosition;
        CharSequence title;

        public Section(int i, CharSequence charSequence) {
            this.firstPosition = i;
            this.title = charSequence;
        }

        public CharSequence getTitle() {
            return this.title;
        }
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public SimpleSectionedListAdapter(Context context, int i, ListAdapter listAdapter) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mSectionResourceId = i;
        this.mBaseAdapter = listAdapter;
        this.mBaseAdapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                SimpleSectionedListAdapter simpleSectionedListAdapter = SimpleSectionedListAdapter.this;
                simpleSectionedListAdapter.mValid = !simpleSectionedListAdapter.mBaseAdapter.isEmpty();
                SimpleSectionedListAdapter.this.notifyDataSetChanged();
            }

            public void onInvalidated() {
                SimpleSectionedListAdapter.this.mValid = false;
                SimpleSectionedListAdapter.this.notifyDataSetInvalidated();
            }
        });
    }

    public void setSections(Section[] sectionArr) {
        this.mSections.clear();
        Arrays.sort(sectionArr, new Comparator<Section>() {
            public int compare(Section section, Section section2) {
                if (section.firstPosition == section2.firstPosition) {
                    return 0;
                }
                return section.firstPosition < section2.firstPosition ? -1 : 1;
            }
        });
        int i = 0;
        for (Section section : sectionArr) {
            section.sectionedPosition = section.firstPosition + i;
            this.mSections.append(section.sectionedPosition, section);
            i++;
        }
        notifyDataSetChanged();
    }

    public int positionToSectionedPosition(int i) {
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mSections.size() && ((Section) this.mSections.valueAt(i2)).firstPosition <= i) {
            i3++;
            i2++;
        }
        return i + i3;
    }

    public int sectionedPositionToPosition(int i) {
        if (isSectionHeaderPosition(i)) {
            return -1;
        }
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mSections.size() && ((Section) this.mSections.valueAt(i2)).sectionedPosition <= i) {
            i3--;
            i2++;
        }
        return i + i3;
    }

    public boolean isSectionHeaderPosition(int i) {
        return this.mSections.get(i) != null;
    }

    public int getCount() {
        if (this.mValid) {
            return this.mBaseAdapter.getCount() + this.mSections.size();
        }
        return 0;
    }

    public Object getItem(int i) {
        return isSectionHeaderPosition(i) ? this.mSections.get(i) : this.mBaseAdapter.getItem(sectionedPositionToPosition(i));
    }

    public long getItemId(int i) {
        return isSectionHeaderPosition(i) ? (long) (Integer.MAX_VALUE - this.mSections.indexOfKey(i)) : this.mBaseAdapter.getItemId(sectionedPositionToPosition(i));
    }

    public int getItemViewType(int i) {
        return isSectionHeaderPosition(i) ? getViewTypeCount() - 1 : this.mBaseAdapter.getItemViewType(i);
    }

    public boolean isEnabled(int i) {
        if (isSectionHeaderPosition(i)) {
            return false;
        }
        return this.mBaseAdapter.isEnabled(sectionedPositionToPosition(i));
    }

    public int getViewTypeCount() {
        return this.mBaseAdapter.getViewTypeCount() + 1;
    }

    public boolean hasStableIds() {
        return this.mBaseAdapter.hasStableIds();
    }

    public boolean isEmpty() {
        return this.mBaseAdapter.isEmpty();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        if (!isSectionHeaderPosition(i)) {
            return this.mBaseAdapter.getView(sectionedPositionToPosition(i), view, viewGroup);
        }
        TextView textView = (TextView) view;
        if (textView == null) {
            textView = (TextView) this.mLayoutInflater.inflate(this.mSectionResourceId, viewGroup, false);
        }
        textView.setText(((Section) this.mSections.get(i)).title);
        return textView;
    }
}
