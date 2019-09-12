package com.masterlock.ble.app.provider;

import android.content.Context;
import com.masterlock.ble.app.util.DBUtil;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class MasterlockDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "masterlock23.db";
    private static final int DATABASE_VERSION = 2040001;
    public static final String OLD_DB_NAME = "masterlock.db";
    private static MasterlockDatabase mInstance;

    private interface References {
        public static final String GUEST_ID = "REFERENCES guests(guest_id)";
        public static final String KMS_ID = "REFERENCES locks(kms_id) ON DELETE CASCADE ON UPDATE CASCADE";
        public static final String LOCK_ID = "REFERENCES locks(lock_id) ON DELETE CASCADE ON UPDATE CASCADE";
    }

    public interface Tables {
        public static final String AVAILABLE_COMMANDS = "available_commands";
        public static final String AVAILABLE_SETTINGS = "available_settings";
        public static final String CALIBRATION = "calibration";
        public static final String DEVICE_INFO = "device_info";
        public static final String GUESTS = "guests";
        public static final String GUESTS_JOIN_INVITATIONS = "guests LEFT OUTER JOIN invitations ON invitations.invitation_guest_id = guests.guest_id";
        public static final String INVITATIONS = "invitations";
        public static final String INVITATIONS_JOIN_GUESTS = "invitations LEFT OUTER JOIN guests ON invitations.invitation_guest_id=guests.guest_id";
        public static final String KEYS = "keys";
        public static final String LOCKS = "locks";
        public static final String LOCKS_JOIN_AVAILABLE_COMMANDS = "locks LEFT OUTER JOIN device_info ON locks.lock_id = device_info.device_info_lock_id LEFT OUTER JOIN keys ON locks.kms_id=keys.kms_device_id INNER JOIN available_commands ON locks.kms_id=available_commands.kms_device_id";
        public static final String LOCKS_JOIN_AVAILABLE_SETTINGS = "locks LEFT OUTER JOIN device_info ON locks.lock_id = device_info.device_info_lock_id LEFT OUTER JOIN keys ON locks.kms_id=keys.kms_device_id INNER JOIN available_settings ON locks.kms_id=available_settings.kms_device_id";
        public static final String LOCKS_JOIN_CALIBRATION = "locks LEFT OUTER JOIN device_info ON locks.lock_id = device_info.device_info_lock_id LEFT OUTER JOIN keys ON locks.kms_id=keys.kms_device_id INNER JOIN calibration ON locks.kms_id=calibration.kms_device_id";
        public static final String LOCKS_JOIN_DEVICE_INFO = "locks LEFT OUTER JOIN device_info ON locks.lock_id = device_info.device_info_lock_id";
        public static final String LOCKS_JOIN_KEYS = "locks LEFT OUTER JOIN device_info ON locks.lock_id = device_info.device_info_lock_id LEFT OUTER JOIN keys ON locks.kms_id=keys.kms_device_id";
        public static final String LOCKS_JOIN_LOGS = "locks LEFT OUTER JOIN device_info ON locks.lock_id = device_info.device_info_lock_id LEFT OUTER JOIN keys ON locks.kms_id=keys.kms_device_id INNER JOIN logs ON locks.kms_id=logs.kms_device_id";
        public static final String LOCKS_JOIN_PRODUCT_CODES = "locks LEFT OUTER JOIN device_info ON locks.lock_id = device_info.device_info_lock_id LEFT OUTER JOIN keys ON locks.kms_id=keys.kms_device_id INNER JOIN product_codes ON locks.lock_id=product_codes.lockId";
        public static final String LOGS = "logs";
        public static final String PRODUCT_CODES = "product_codes";
        public static final String WORD_DICTIONARY = "word_dictionary";
    }

    private MasterlockDatabase(Context context) {
        super(context, DATABASE_NAME, null, 2040001);
    }

    public static synchronized MasterlockDatabase getInstance(Context context) {
        MasterlockDatabase masterlockDatabase;
        synchronized (MasterlockDatabase.class) {
            if (mInstance == null) {
                mInstance = new MasterlockDatabase(context.getApplicationContext());
            }
            masterlockDatabase = mInstance;
        }
        return masterlockDatabase;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL("CREATE TABLE locks (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,lock_id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,lock_name TEXT,lock_notes TEXT,lock_label TEXT,is_favorite INTEGER NOT NULL DEFAULT 0,user_type INTEGER NOT NULL DEFAULT 0,created_on TEXT,modified_on TEXT,model_id TEXT,model_interface_id INTEGER DEFAULT 0,model_number TEXT,model_sku TEXT,model_name TEXT,model_description TEXT,kms_id TEXT UNIQUE ON CONFLICT REPLACE,firmware_version INTEGER DEFAULT 0,firmware_counter INTEGER DEFAULT 0,primary_code TEXT,secondary_code_1 TEXT,secondary_code_2 TEXT,secondary_code_3 TEXT,secondary_code_4 TEXT,secondary_code_5 TEXT,battery_level INTEGER,temperature INTEGER,tx_power INTEGER,tx_interval INTEGER,lock_mode INTEGER NOT NULL DEFAULT 0,relock_time INTEGER,lock_status INTEGER NOT NULL DEFAULT 0,shackle_status INTEGER NOT NULL DEFAULT 0,locker_mode INTEGER NOT NULL DEFAULT 0,timezone TEXT,service_code TEXT,service_code_expiration TEXT,last_log_reference_id INTEGER,touch_mode_configuration TEXT,proximity_swipe_mode_configuration TEXT,kms_created_on TEXT,kms_modified_on TEXT,created_on_timezone_adjustedTEXT,modified_on_timezone_adjustedTEXT,public_config_counter INTEGER DEFAULT 0,primary_passcode_counter INTEGER DEFAULT 0,measurement_counter INTEGER DEFAULT 0,secondary_passcode_counter INTEGER DEFAULT 0,latitude TEXT NOT NULL DEFAULT '',longitude TEXT NOT NULL DEFAULT '',last_unlocked INTEGER DEFAULT 0,last_unlocked_shackle INTEGER DEFAULT 0,permission_start_at_date TEXT,permission_expires_at_date TEXT,permission_monday INTEGER,permission_tuesday INTEGER,permission_wednesday INTEGER,permission_thursday INTEGER,permission_friday INTEGER,permission_saturday INTEGER,permission_sunday INTEGER,permission_open_shackle_permission INTEGER,permission_view_temporary_code_permission INTEGER,permission_view_last_known_location_permission INTEGER,permission_schedule_type INTEGER,permission_view_type INTEGER,memory_map_version INTEGER,section_number INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE keys (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,key_id TEXT NOT NULL,kms_device_id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE REFERENCES locks(kms_id) ON DELETE CASCADE ON UPDATE CASCADE,device_id TEXT NOT NULL,key_value TEXT,profile_value TEXT,key_user_type INTEGER NOT NULL DEFAULT 0,user_id TEXT,alias INTEGER,product_invitation_id TEXT,key_expires_on TEXT,key_created_on TEXT,key_modified_on TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE device_info (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,device_info_lock_id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,rssi_threshold TEXT ,mac_address TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE guests (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,guest_id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,guest_first_name TEXT,guest_last_name TEXT,guest_organization TEXT,guest_email TEXT,guest_mobile_number TEXT,guest_country_code TEXT,guest_alpha_country_code TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE invitations (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,invitation_id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,invitation_status INTEGER NOT NULL DEFAULT 0,invitation_product_id TEXT NOT NULL REFERENCES locks(lock_id) ON DELETE CASCADE ON UPDATE CASCADE,invitation_guest_id TEXT NOT NULL REFERENCES guests(guest_id),invitation_message TEXT,invitation_user_type INTEGER NOT NULL DEFAULT 0,invitation_schedule_type INTEGER NOT NULL DEFAULT 0,invitation_accepted_on TEXT,invitation_created_on TEXT,invitation_modified_on TEXT,invitation_is_expired INTEGER NOT NULL DEFAULT 0,invitation_expires_on TEXT,invitation_guest_permissions_id TEXT,invitation_guest_interface_selection_mode INTEGER NOT NULL DEFAULT 1,invitation_full_owner_name TEXT,invitation_url TEXT,invitation_start_at_date TEXT,invitation_expires_at_date TEXT,invitation_monday INTEGER,invitation_tuesday INTEGER,invitation_wednesday INTEGER,invitation_thursday INTEGER,invitation_friday INTEGER,invitation_saturday INTEGER,invitation_sunday INTEGER,invitation_open_shackle_permission INTEGER,invitation_view_temporary_code_permission INTEGER,invitation_view_last_known_location_permission INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE logs (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,log_id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,kms_device_id TEXT NOT NULL REFERENCES locks(kms_id) ON DELETE CASCADE ON UPDATE CASCADE,kms_device_key_alias INTEGER,reference_id INTEGER,firmware_counter INTEGER,event_code TEXT,event_source TEXT,event_value TEXT,alias TEXT,message TEXT,created_on TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE word_dictionary (_id INTEGER PRIMARY KEY AUTOINCREMENT,word_dictionary_id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,word_dictionary_word TEXT NOT NULL)");
        sQLiteDatabase.execSQL("CREATE TABLE available_commands (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,available_command_uuid TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,kms_device_id TEXT NOT NULL REFERENCES locks(kms_id) ON DELETE CASCADE ON UPDATE CASCADE,available_command_id INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE available_settings (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,available_setting_uuid TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,kms_device_id TEXT NOT NULL REFERENCES locks(kms_id) ON DELETE CASCADE ON UPDATE CASCADE,available_setting_id INTEGER,available_setting_address INTEGER,available_setting_size INTEGER)");
        sQLiteDatabase.execSQL("CREATE TABLE calibration (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,id TEXT NOT NULL UNIQUE ON CONFLICT REPLACE,kms_device_id TEXT NOT NULL REFERENCES locks(kms_id) ON DELETE CASCADE ON UPDATE CASCADE,skip INTEGER DEFAULT 0,value TEXT)");
        sQLiteDatabase.execSQL("CREATE TABLE product_codes (_id INTEGER PRIMARY KEY AUTOINCREMENT,updated INTEGER NOT NULL DEFAULT -1,lockId TEXT NOT NULL REFERENCES locks(lock_id) ON DELETE CASCADE ON UPDATE CASCADE,id TEXT NOT NULL,name TEXT NOT NULL,value TEXT,display_order INTEGER)");
        DBUtil.populateWordDictionary(sQLiteDatabase);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        if (i != 2040001) {
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS locks");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS logs");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS keys");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS device_info");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS invitations");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS guests");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS word_dictionary");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS available_commands");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS available_settings");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS calibration");
            sQLiteDatabase.execSQL("DROP TABLE IF EXISTS product_codes");
            onCreate(sQLiteDatabase);
        }
    }
}
