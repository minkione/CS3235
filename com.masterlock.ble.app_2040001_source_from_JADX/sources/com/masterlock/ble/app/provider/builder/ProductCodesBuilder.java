package com.masterlock.ble.app.provider.builder;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.database.Cursor;
import com.masterlock.ble.app.provider.MasterlockContract.ProductCodes;
import com.masterlock.ble.app.provider.MasterlockContract.SyncColumns;
import com.masterlock.core.ProductCode;
import com.masterlock.core.ProductCode.Builder;
import java.util.ArrayList;

public class ProductCodesBuilder {
    public static ContentValues buildContentValues(ProductCode productCode) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SyncColumns.UPDATED, Long.valueOf(System.currentTimeMillis()));
        contentValues.put("id", productCode.getId());
        contentValues.put(ProductCodesColumns.LOCK_ID, productCode.getLockId());
        contentValues.put(ProductCodesColumns.NAME, productCode.getName());
        contentValues.put("value", productCode.getValue());
        contentValues.put(ProductCodesColumns.DISPLAY_ORDER, Integer.valueOf(productCode.getDisplayOrder()));
        return contentValues;
    }

    public static ContentProviderOperation buildContentProviderOperation(ProductCode productCode) {
        return ContentProviderOperation.newInsert(ProductCodes.CONTENT_URI).withValue(ProductCodesColumns.NAME, productCode.getName()).withValue(ProductCodesColumns.LOCK_ID, productCode.getLockId()).withValue("id", productCode.getId()).withValue("value", productCode.getValue()).withValue(ProductCodesColumns.DISPLAY_ORDER, Integer.valueOf(productCode.getDisplayOrder())).build();
    }

    public static ArrayList<ProductCode> buildProductCodes(Cursor cursor) {
        ArrayList<ProductCode> arrayList = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            arrayList.add(buildProductCode(cursor));
            cursor.moveToNext();
        }
        return arrayList;
    }

    public static ProductCode buildProductCode(Cursor cursor) {
        return new Builder().setId(cursor.getString(cursor.getColumnIndexOrThrow("id"))).setName(cursor.getString(cursor.getColumnIndexOrThrow(ProductCodesColumns.NAME))).setLockId(cursor.getString(cursor.getColumnIndexOrThrow(ProductCodesColumns.LOCK_ID))).setDisplayOrder(cursor.getInt(cursor.getColumnIndexOrThrow(ProductCodesColumns.DISPLAY_ORDER))).setValue(cursor.getString(cursor.getColumnIndexOrThrow("value"))).build();
    }
}
