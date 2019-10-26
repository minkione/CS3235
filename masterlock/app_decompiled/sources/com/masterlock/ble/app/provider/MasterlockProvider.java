package com.masterlock.ble.app.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.masterlock.ble.app.MasterLockSharedPreferences;
import com.masterlock.ble.app.provider.MasterlockContract.AvailableCommands;
import com.masterlock.ble.app.provider.MasterlockContract.AvailableSettings;
import com.masterlock.ble.app.provider.MasterlockContract.Calibration;
import com.masterlock.ble.app.provider.MasterlockContract.DeviceInfo;
import com.masterlock.ble.app.provider.MasterlockContract.Guests;
import com.masterlock.ble.app.provider.MasterlockContract.Invitations;
import com.masterlock.ble.app.provider.MasterlockContract.Keys;
import com.masterlock.ble.app.provider.MasterlockContract.Locks;
import com.masterlock.ble.app.provider.MasterlockContract.Logs;
import com.masterlock.ble.app.provider.MasterlockContract.ProductCodes;
import com.masterlock.ble.app.provider.MasterlockContract.WordDictionary;
import com.masterlock.ble.app.provider.MasterlockDatabase.Tables;
import com.masterlock.ble.app.util.SelectionBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import net.sqlcipher.database.SQLiteDatabase;

public class MasterlockProvider extends ContentProvider {
    public static final int AVAILABLE_COMMAND = 900;
    public static final int AVAILABLE_COMMAND_ID = 901;
    public static final int AVAILABLE_SETTING = 1000;
    public static final int AVAILABLE_SETTING_ID = 1001;
    public static final int CALIBRATION = 1100;
    public static final int CALIBRATION_ID = 1101;
    public static final int DEVICE_INFO = 600;
    public static final int DEVICE_INFO_LOCK_ID = 601;
    public static final int GUESTS = 400;
    public static final int GUESTS_ID = 401;
    public static final int GUESTS_ID_INVITATIONS = 402;
    public static final int INVITATIONS = 300;
    public static final int INVITATIONS_ID = 301;
    public static final int KEYS = 200;
    public static final int KEYS_ID = 201;
    public static final int LOCKS = 100;
    public static final int LOCKS_ID = 101;
    public static final int LOCKS_ID_AVAILABLE_COMMANDS = 106;
    public static final int LOCKS_ID_AVAILABLE_SETTINGS = 107;
    public static final int LOCKS_ID_CALIBRATION = 108;
    public static final int LOCKS_ID_INVITATIONS = 103;
    public static final int LOCKS_ID_KEYS = 102;
    public static final int LOCKS_ID_LOGS = 104;
    public static final int LOCKS_ID_PRODUCT_CODES = 109;
    public static final int LOGS = 500;
    public static final int LOGS_ID = 501;
    public static final int PRODUCT_CODE = 1102;
    public static final int PRODUCT_CODE_ID = 1103;
    public static final int WORD_DICTIONARY = 800;
    public static final int WORD_DICTIONARY_ID = 801;
    public static final UriMatcher sUriMatcher = buildUriMatcher();
    MasterlockDatabase mOpenHelper;
    private MasterLockSharedPreferences prefs;
    private boolean sendNotifications = true;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(-1);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.LOCKS, 100);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*", 101);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*/keys", 102);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*/invitations", 103);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*/logs", 104);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*/available_commands", 106);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*/available_settings", 107);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*/calibration", 108);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "locks/*/product_codes", 109);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.KEYS, 200);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "keys/*", KEYS_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.INVITATIONS, INVITATIONS);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "invitations/*", INVITATIONS_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "guests/*/guests", GUESTS_ID_INVITATIONS);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.GUESTS, GUESTS);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "guests/*", GUESTS_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.LOGS, LOGS);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "logs/*", LOGS_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.DEVICE_INFO, 600);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "device_info/*", DEVICE_INFO_LOCK_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.WORD_DICTIONARY, WORD_DICTIONARY);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "word_dictionary/*", WORD_DICTIONARY_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.AVAILABLE_COMMANDS, AVAILABLE_COMMAND);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "available_commands/*", AVAILABLE_COMMAND_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.AVAILABLE_SETTINGS, 1000);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "available_settings/*", 1001);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.CALIBRATION, CALIBRATION);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "calibration/*", CALIBRATION_ID);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, Tables.PRODUCT_CODES, PRODUCT_CODE);
        uriMatcher.addURI(MasterlockContract.CONTENT_AUTHORITY, "product_codes/*", PRODUCT_CODE_ID);
        return uriMatcher;
    }

    public boolean onCreate() {
        this.mOpenHelper = MasterlockDatabase.getInstance(getContext());
        this.prefs = MasterLockSharedPreferences.getInstance(getContext());
        if (this.prefs.getDbToken().isEmpty()) {
            this.prefs.putDbToken("kJK*3=u>K'6wF{H5");
        }
        SQLiteDatabase.loadLibs(getContext());
        return true;
    }

    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case 100:
                return Locks.CONTENT_TYPE;
            case 101:
                return Locks.CONTENT_ITEM_TYPE;
            case 102:
                return Keys.CONTENT_TYPE;
            case 103:
                return Invitations.CONTENT_TYPE;
            case 104:
                return Logs.CONTENT_TYPE;
            case 200:
                return Keys.CONTENT_TYPE;
            case KEYS_ID /*201*/:
                return Keys.CONTENT_ITEM_TYPE;
            case INVITATIONS /*300*/:
                return Invitations.CONTENT_TYPE;
            case INVITATIONS_ID /*301*/:
                return Invitations.CONTENT_ITEM_TYPE;
            case GUESTS /*400*/:
                return Guests.CONTENT_TYPE;
            case GUESTS_ID /*401*/:
                return Guests.CONTENT_ITEM_TYPE;
            case GUESTS_ID_INVITATIONS /*402*/:
                return Invitations.CONTENT_TYPE;
            case LOGS /*500*/:
                return Logs.CONTENT_TYPE;
            case LOGS_ID /*501*/:
                return Logs.CONTENT_ITEM_TYPE;
            case 600:
                return DeviceInfo.CONTENT_TYPE;
            case DEVICE_INFO_LOCK_ID /*601*/:
                return DeviceInfo.CONTENT_ITEM_TYPE;
            case WORD_DICTIONARY /*800*/:
                return WordDictionary.CONTENT_TYPE;
            case WORD_DICTIONARY_ID /*801*/:
                return WordDictionary.CONTENT_ITEM_TYPE;
            case AVAILABLE_COMMAND /*900*/:
                return AvailableCommands.CONTENT_TYPE;
            case AVAILABLE_COMMAND_ID /*901*/:
                return AvailableCommands.CONTENT_ITEM_TYPE;
            case 1000:
                return AvailableSettings.CONTENT_TYPE;
            case 1001:
                return AvailableSettings.CONTENT_ITEM_TYPE;
            case CALIBRATION /*1100*/:
                return Calibration.CONTENT_TYPE;
            case CALIBRATION_ID /*1101*/:
                return Calibration.CONTENT_ITEM_TYPE;
            case PRODUCT_CODE /*1102*/:
                return ProductCodes.CONTENT_TYPE;
            case PRODUCT_CODE_ID /*1103*/:
                return ProductCodes.CONTENT_ITEM_TYPE;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown uri: ");
                sb.append(uri);
                throw new UnsupportedOperationException(sb.toString());
        }
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        boolean z = !TextUtils.isEmpty(uri.getQueryParameter(MasterlockContract.QUERY_PARAMETER_DISTINCT));
        Cursor query = buildExpandedSelection(uri, sUriMatcher.match(uri)).where(str, strArr2).query(this.mOpenHelper.getReadableDatabase(this.prefs.getDbToken()), z, strArr, str2);
        if (query != null && !isTemporary() && this.sendNotifications) {
            query.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return query;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase(this.prefs.getDbToken());
        switch (sUriMatcher.match(uri)) {
            case 100:
                writableDatabase.insertOrThrow(Tables.LOCKS, null, contentValues);
                Uri buildLockUri = Locks.buildLockUri(contentValues.getAsString("lock_id"));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildLockUri, null);
                }
                return buildLockUri;
            case 101:
            case KEYS_ID /*201*/:
            case INVITATIONS_ID /*301*/:
            case GUESTS_ID /*401*/:
            case GUESTS_ID_INVITATIONS /*402*/:
            case DEVICE_INFO_LOCK_ID /*601*/:
            case AVAILABLE_COMMAND_ID /*901*/:
            case 1001:
            case CALIBRATION_ID /*1101*/:
            case PRODUCT_CODE_ID /*1103*/:
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to insert at provided id for ");
                sb.append(uri);
                sb.append("...Did you mean to update this record or insert a new record?");
                throw new UnsupportedOperationException(sb.toString());
            case 102:
                writableDatabase.insertOrThrow(Tables.KEYS, null, contentValues);
                Uri buildLockUri2 = Locks.buildLockUri(contentValues.getAsString("lock_id"));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return buildLockUri2;
            case 103:
                writableDatabase.insertOrThrow(Tables.INVITATIONS, null, contentValues);
                Uri buildInvitationUri = Invitations.buildInvitationUri(contentValues.getAsString(InvitationsColumns.INVITATION_ID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return buildInvitationUri;
            case 104:
                writableDatabase.insertOrThrow(Tables.LOGS, null, contentValues);
                Uri buildLogUri = Logs.buildLogUri(contentValues.getAsString(LogColumns.LOG_ID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return buildLogUri;
            case 200:
                writableDatabase.insertOrThrow(Tables.KEYS, null, contentValues);
                Uri buildKeyUri = Keys.buildKeyUri(contentValues.getAsString(KeysColumns.KEY_ID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildKeyUri, null);
                }
                return buildKeyUri;
            case INVITATIONS /*300*/:
                writableDatabase.insertOrThrow(Tables.INVITATIONS, null, contentValues);
                Uri buildInvitationUri2 = Invitations.buildInvitationUri(contentValues.getAsString(InvitationsColumns.INVITATION_ID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildInvitationUri2, null);
                }
                return buildInvitationUri2;
            case GUESTS /*400*/:
                writableDatabase.insertOrThrow(Tables.GUESTS, null, contentValues);
                Uri buildGuestUri = Guests.buildGuestUri(contentValues.getAsString(GuestsColumns.GUEST_ID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildGuestUri, null);
                }
                return buildGuestUri;
            case LOGS /*500*/:
                writableDatabase.insertOrThrow(Tables.LOGS, null, contentValues);
                Uri buildLogUri2 = Logs.buildLogUri(contentValues.getAsString(LogColumns.LOG_ID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildLogUri2, null);
                }
                return buildLogUri2;
            case 600:
                writableDatabase.insertOrThrow(Tables.DEVICE_INFO, null, contentValues);
                Uri buildDeviceInfoUri = DeviceInfo.buildDeviceInfoUri(contentValues.getAsString(DeviceInfoColumns.DEVICE_INFO_LOCK_ID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildDeviceInfoUri, null);
                }
                return buildDeviceInfoUri;
            case AVAILABLE_COMMAND /*900*/:
                writableDatabase.insertOrThrow(Tables.AVAILABLE_COMMANDS, null, contentValues);
                Uri buildAvailableCommandUri = AvailableCommands.buildAvailableCommandUri(contentValues.getAsString(AvailableCommandsColumns.AVAILABLE_COMMAND_UUID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildAvailableCommandUri, null);
                }
                return buildAvailableCommandUri;
            case 1000:
                writableDatabase.insertOrThrow(Tables.AVAILABLE_SETTINGS, null, contentValues);
                Uri buildAvailableSettingUri = AvailableSettings.buildAvailableSettingUri(contentValues.getAsString(AvailableSettingsColumns.AVAILABLE_SETTING_UUID));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildAvailableSettingUri, null);
                }
                return buildAvailableSettingUri;
            case CALIBRATION /*1100*/:
                writableDatabase.insertOrThrow(Tables.CALIBRATION, null, contentValues);
                Uri buildCalibrationUri = Calibration.buildCalibrationUri(contentValues.getAsString("id"));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildCalibrationUri, null);
                }
                return buildCalibrationUri;
            case PRODUCT_CODE /*1102*/:
                writableDatabase.insertOrThrow(Tables.PRODUCT_CODES, null, contentValues);
                Uri buildProductCodesUri = ProductCodes.buildProductCodesUri(contentValues.getAsString("id"));
                if (this.sendNotifications) {
                    getContext().getContentResolver().notifyChange(buildProductCodesUri, null);
                }
                return buildProductCodesUri;
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown uri: ");
                sb2.append(uri);
                throw new UnsupportedOperationException(sb2.toString());
        }
    }

    public int delete(Uri uri, String str, String[] strArr) {
        int delete = buildSelection(uri, sUriMatcher.match(uri)).where(str, strArr).delete(this.mOpenHelper.getWritableDatabase(this.prefs.getDbToken()));
        if (delete != 0 && this.sendNotifications) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delete;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int update = buildSelection(uri, sUriMatcher.match(uri)).where(str, strArr).update(this.mOpenHelper.getWritableDatabase(this.prefs.getDbToken()), contentValues);
        if (update != 0 && this.sendNotifications) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return update;
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> arrayList) {
        int i = 0;
        this.sendNotifications = false;
        SQLiteDatabase writableDatabase = this.mOpenHelper.getWritableDatabase(this.prefs.getDbToken());
        writableDatabase.beginTransaction();
        int size = arrayList.size();
        ContentProviderResult[] contentProviderResultArr = new ContentProviderResult[size];
        while (i < size) {
            try {
                contentProviderResultArr[i] = ((ContentProviderOperation) arrayList.get(i)).apply(this, contentProviderResultArr, i);
                i++;
            } catch (OperationApplicationException unused) {
            } catch (Throwable th) {
                writableDatabase.endTransaction();
                throw th;
            }
        }
        writableDatabase.setTransactionSuccessful();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ContentProviderOperation contentProviderOperation = (ContentProviderOperation) it.next();
            if (!arrayList2.contains(contentProviderOperation.getUri().getPath())) {
                getContext().getContentResolver().notifyChange(contentProviderOperation.getUri(), null);
                arrayList2.add(contentProviderOperation.getUri().getPath());
            }
        }
        writableDatabase.endTransaction();
        this.sendNotifications = true;
        return contentProviderResultArr;
    }

    private SelectionBuilder buildSelection(Uri uri, int i) {
        SelectionBuilder selectionBuilder = new SelectionBuilder();
        switch (i) {
            case 100:
                return selectionBuilder.table(Tables.LOCKS);
            case 101:
                return selectionBuilder.table(Tables.LOCKS).where("lock_id=?", Locks.getLockId(uri));
            case 102:
                return selectionBuilder.table(Tables.KEYS).where("device_id=?", Locks.getLockId(uri));
            case 103:
                Locks.getLockId(uri);
                return selectionBuilder.table(Tables.INVITATIONS);
            case 104:
                return selectionBuilder.table(Tables.LOGS).where("kms_device_id=?", Locks.getLockId(uri));
            case 200:
                return selectionBuilder.table(Tables.KEYS);
            case KEYS_ID /*201*/:
                return selectionBuilder.table(Tables.KEYS).where("key_id=?", Keys.getKeyId(uri));
            case INVITATIONS /*300*/:
                return selectionBuilder.table(Tables.INVITATIONS);
            case INVITATIONS_ID /*301*/:
                return selectionBuilder.table(Tables.INVITATIONS).where("invitation_id=?", Invitations.getInvitationId(uri));
            case GUESTS /*400*/:
                return selectionBuilder.table(Tables.GUESTS);
            case GUESTS_ID /*401*/:
                return selectionBuilder.table(Tables.GUESTS).where("guest_id=?", Guests.getGuestId(uri));
            case GUESTS_ID_INVITATIONS /*402*/:
                return selectionBuilder.table(Tables.INVITATIONS).where("invitation_product_id=?", Guests.getGuestId(uri));
            case LOGS /*500*/:
                return selectionBuilder.table(Tables.LOGS);
            case LOGS_ID /*501*/:
                return selectionBuilder.table(Tables.LOGS).where("log_id=?", Logs.getLogId(uri));
            case 600:
                return selectionBuilder.table(Tables.DEVICE_INFO);
            case DEVICE_INFO_LOCK_ID /*601*/:
                return selectionBuilder.table(Tables.DEVICE_INFO).where("device_info_lock_id=?", DeviceInfo.getDeviceInfoId(uri));
            case WORD_DICTIONARY /*800*/:
                return selectionBuilder.table(Tables.WORD_DICTIONARY);
            case AVAILABLE_COMMAND /*900*/:
                return selectionBuilder.table(Tables.AVAILABLE_COMMANDS);
            case AVAILABLE_COMMAND_ID /*901*/:
                return selectionBuilder.table(Tables.AVAILABLE_COMMANDS).where("available_command_uuid=?", AvailableCommands.getAvailableCommandId(uri));
            case 1000:
                return selectionBuilder.table(Tables.AVAILABLE_SETTINGS);
            case 1001:
                return selectionBuilder.table(Tables.AVAILABLE_SETTINGS).where("available_setting_uuid=?", AvailableCommands.getAvailableCommandId(uri));
            case CALIBRATION /*1100*/:
                return selectionBuilder.table(Tables.CALIBRATION);
            case CALIBRATION_ID /*1101*/:
                return selectionBuilder.table(Tables.CALIBRATION).where("id=?", Calibration.getCalibrationId(uri));
            case PRODUCT_CODE /*1102*/:
                return selectionBuilder.table(Tables.PRODUCT_CODES);
            case PRODUCT_CODE_ID /*1103*/:
                return selectionBuilder.table(Tables.PRODUCT_CODES).where("id=?", ProductCodes.getProductCodeId(uri));
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown uri: ");
                sb.append(uri);
                throw new UnsupportedOperationException(sb.toString());
        }
    }

    private SelectionBuilder buildExpandedSelection(Uri uri, int i) {
        SelectionBuilder selectionBuilder = new SelectionBuilder();
        switch (i) {
            case 100:
                return selectionBuilder.table(Tables.LOCKS_JOIN_KEYS).mapToTable("_id", Tables.LOCKS).mapToTable("lock_id", Tables.LOCKS);
            case 101:
                return selectionBuilder.table(Tables.LOCKS_JOIN_KEYS).mapToTable("_id", Tables.LOCKS).mapToTable("lock_id", Tables.LOCKS).where("locks.lock_id=?", Locks.getLockId(uri));
            case 103:
                return selectionBuilder.table(Tables.INVITATIONS_JOIN_GUESTS).mapToTable("lock_id", Tables.LOCKS).where("invitation_product_id=?", Locks.getLockId(uri));
            case 104:
                return selectionBuilder.table(Tables.LOCKS_JOIN_LOGS).mapToTable("_id", Tables.LOGS).mapToTable("lock_id", Tables.LOCKS).mapToTable("created_on", Tables.LOGS).where("locks.lock_id=?", Locks.getLockId(uri));
            case 106:
                return selectionBuilder.table(Tables.LOCKS_JOIN_AVAILABLE_COMMANDS).mapToTable("_id", Tables.AVAILABLE_COMMANDS).where("locks.lock_id=?", Locks.getLockId(uri));
            case 107:
                return selectionBuilder.table(Tables.LOCKS_JOIN_AVAILABLE_SETTINGS).mapToTable("_id", Tables.AVAILABLE_SETTINGS).where("locks.lock_id=?", Locks.getLockId(uri));
            case 108:
                return selectionBuilder.table(Tables.LOCKS_JOIN_CALIBRATION).mapToTable("_id", Tables.CALIBRATION).where("locks.lock_id=?", Locks.getLockId(uri));
            case 109:
                return selectionBuilder.table(Tables.LOCKS_JOIN_PRODUCT_CODES).mapToTable("_id", Tables.PRODUCT_CODES).where("locks.lock_id=?", Locks.getLockId(uri));
            case INVITATIONS /*300*/:
                return selectionBuilder.table(Tables.INVITATIONS_JOIN_GUESTS).mapToTable("_id", Tables.INVITATIONS);
            case INVITATIONS_ID /*301*/:
                return selectionBuilder.table(Tables.INVITATIONS_JOIN_GUESTS).mapToTable("_id", Tables.INVITATIONS).where("invitation_id=?", Invitations.getInvitationId(uri));
            case GUESTS_ID_INVITATIONS /*402*/:
                return selectionBuilder.table(Tables.GUESTS_JOIN_INVITATIONS).where("invitation_product_id=?", Guests.getGuestId(uri));
            default:
                return buildSelection(uri, i);
        }
    }
}
