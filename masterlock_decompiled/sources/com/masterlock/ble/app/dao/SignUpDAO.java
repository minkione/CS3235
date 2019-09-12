package com.masterlock.ble.app.dao;

import android.database.Cursor;
import com.masterlock.ble.app.MasterLockApp;
import com.masterlock.ble.app.provider.MasterlockContract.WordDictionary;
import java.util.ArrayList;
import java.util.List;

public class SignUpDAO {
    public List<String> getMatchingDictionaryWords(String str) {
        ArrayList arrayList = new ArrayList();
        String[] strArr = {"DISTINCT *"};
        StringBuilder sb = new StringBuilder();
        sb.append("instr('");
        sb.append(str);
        sb.append("',");
        sb.append(WordDictionaryColumns.WORD_DICTIONARY_WORD);
        sb.append(")!=0");
        Cursor query = MasterLockApp.get().getContentResolver().query(WordDictionary.CONTENT_URI, strArr, sb.toString(), null, null);
        if (query.moveToFirst()) {
            do {
                arrayList.add(query.getString(2));
            } while (query.moveToNext());
        }
        if (query != null && !query.isClosed()) {
            query.close();
        }
        return arrayList;
    }
}
