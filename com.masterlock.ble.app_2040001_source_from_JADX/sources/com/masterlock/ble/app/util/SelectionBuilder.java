package com.masterlock.ble.app.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.sqlcipher.database.SQLiteDatabase;

public class SelectionBuilder {
    private Map<String, String> mProjectionMap = new HashMap();
    private StringBuilder mSelection = new StringBuilder();
    private ArrayList<String> mSelectionArgs = new ArrayList<>();
    private String mTable = null;

    public SelectionBuilder reset() {
        this.mTable = null;
        this.mSelection.setLength(0);
        this.mSelectionArgs.clear();
        return this;
    }

    public SelectionBuilder where(String str, String... strArr) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mSelection.length() > 0) {
                this.mSelection.append(" AND ");
            }
            StringBuilder sb = this.mSelection;
            sb.append("(");
            sb.append(str);
            sb.append(")");
            if (strArr != null) {
                Collections.addAll(this.mSelectionArgs, strArr);
            }
            return this;
        } else if (strArr == null || strArr.length <= 0) {
            return this;
        } else {
            throw new IllegalArgumentException("Valid selection required when including arguments=");
        }
    }

    public SelectionBuilder table(String str) {
        this.mTable = str;
        return this;
    }

    private void assertTable() {
        if (this.mTable == null) {
            throw new IllegalStateException("Table not specified");
        }
    }

    public SelectionBuilder mapToTable(String str, String str2) {
        Map<String, String> map = this.mProjectionMap;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(".");
        sb.append(str);
        map.put(str, sb.toString());
        return this;
    }

    public SelectionBuilder map(String str, String str2) {
        Map<String, String> map = this.mProjectionMap;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(" AS ");
        sb.append(str);
        map.put(str, sb.toString());
        return this;
    }

    public String getSelection() {
        return this.mSelection.toString();
    }

    public String[] getSelectionArgs() {
        ArrayList<String> arrayList = this.mSelectionArgs;
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    private void mapColumns(String[] strArr) {
        for (int i = 0; i < strArr.length; i++) {
            String str = (String) this.mProjectionMap.get(strArr[i]);
            if (str != null) {
                strArr[i] = str;
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SelectionBuilder[table=");
        sb.append(this.mTable);
        sb.append(", selection=");
        sb.append(getSelection());
        sb.append(", selectionArgs=");
        sb.append(Arrays.toString(getSelectionArgs()));
        sb.append("]");
        return sb.toString();
    }

    public Cursor query(SQLiteDatabase sQLiteDatabase, String[] strArr, String str) {
        return query(sQLiteDatabase, false, strArr, null, null, str, null);
    }

    public Cursor query(SQLiteDatabase sQLiteDatabase, boolean z, String[] strArr, String str) {
        return query(sQLiteDatabase, z, strArr, null, null, str, null);
    }

    public Cursor query(SQLiteDatabase sQLiteDatabase, boolean z, String[] strArr, String str, String str2, String str3, String str4) {
        String[] strArr2 = strArr;
        assertTable();
        if (strArr2 != null) {
            mapColumns(strArr);
        }
        return sQLiteDatabase.query(z, this.mTable, strArr, getSelection(), getSelectionArgs(), str, str2, str3, str4);
    }

    public int update(SQLiteDatabase sQLiteDatabase, ContentValues contentValues) {
        assertTable();
        return sQLiteDatabase.update(this.mTable, contentValues, getSelection(), getSelectionArgs());
    }

    public int delete(SQLiteDatabase sQLiteDatabase) {
        assertTable();
        return sQLiteDatabase.delete(this.mTable, getSelection(), getSelectionArgs());
    }
}
