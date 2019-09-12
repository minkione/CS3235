package com.masterlock.ble.app.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class MasterlockContract {
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://com.masterlock.ble");
    public static final String CONTENT_AUTHORITY = "com.masterlock.ble";
    static final String PATH_AVAILABLE_COMMANDS = "available_commands";
    static final String PATH_AVAILABLE_SETTINGS = "available_settings";
    static final String PATH_CALIBRATION = "calibration";
    static final String PATH_DEVICE_INFO = "device_info";
    static final String PATH_GUESTS = "guests";
    static final String PATH_INVITATIONS = "invitations";
    static final String PATH_KEYS = "keys";
    static final String PATH_LOCKS = "locks";
    static final String PATH_LOGS = "logs";
    static final String PATH_PERMISSIONS = "permissions";
    static final String PATH_PRODUCT_CODES = "product_codes";
    static final String PATH_WORD_DICTIONARY = "word_dictionary";
    public static final String QUERY_PARAMETER_DISTINCT = "DISTINCT";
    public static final long UPDATED_UNKNOWN = -1;

    public static class AvailableCommands implements AvailableCommandsColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.available_commands";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.available_commands";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("available_commands").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildAvailableCommandUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getAvailableCommandId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface AvailableCommandsColumns {
        public static final String AVAILABLE_COMMAND_ID = "available_command_id";
        public static final String AVAILABLE_COMMAND_KMS_DEVICE_ID = "kms_device_id";
        public static final String AVAILABLE_COMMAND_UUID = "available_command_uuid";
    }

    public static class AvailableSettings implements AvailableSettingsColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.available_settings";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.available_settings";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("available_settings").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildAvailableSettingUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getAvailableSettingId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface AvailableSettingsColumns {
        public static final String AVAILABLE_SETTING_ADDRESS = "available_setting_address";
        public static final String AVAILABLE_SETTING_ID = "available_setting_id";
        public static final String AVAILABLE_SETTING_KMS_DEVICE_ID = "kms_device_id";
        public static final String AVAILABLE_SETTING_SIZE = "available_setting_size";
        public static final String AVAILABLE_SETTING_UUID = "available_setting_uuid";
    }

    public static class Calibration implements CalibrationColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.calibration";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.calibration";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("calibration").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildCalibrationUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getCalibrationId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface CalibrationColumns {
        public static final String CALIBRATION_ID = "id";
        public static final String CALIBRATION_KMS_DEVICE_ID = "kms_device_id";
        public static final String CALIBRATION_SKIP = "skip";
        public static final String CALIBRATION_VALUE = "value";
    }

    public static class DeviceInfo implements DeviceInfoColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.device_info";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.device_info";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("device_info").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildDeviceInfoUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getDeviceInfoId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface DeviceInfoColumns {
        public static final String DEVICE_INFO_LOCK_ID = "device_info_lock_id";
        public static final String MAC_ADDRESS = "mac_address";
        public static final String RSSI_THRESHOLD = "rssi_threshold";
    }

    public static class Guests implements GuestsColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.guest";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.guest";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("guests").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildGuestUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getGuestId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }

        public static Uri buildInvitationsDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("invitations").build();
        }
    }

    interface GuestsColumns {
        public static final String GUEST_ALPHA_COUNTRY_CODE = "guest_alpha_country_code";
        public static final String GUEST_COUNTRY_CODE = "guest_country_code";
        public static final String GUEST_EMAIL = "guest_email";
        public static final String GUEST_FIRST_NAME = "guest_first_name";
        public static final String GUEST_ID = "guest_id";
        public static final String GUEST_LAST_NAME = "guest_last_name";
        public static final String GUEST_MOBILE_NUMBER = "guest_mobile_number";
        public static final String GUEST_ORGANIZATION = "guest_organization";
    }

    public static class Invitations implements InvitationsColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.invitation";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.invitation";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("invitations").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildInvitationUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getInvitationId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface InvitationsColumns {
        public static final String INVITATION_ACCEPTED_ON = "invitation_accepted_on";
        public static final String INVITATION_CREATED_ON = "invitation_created_on";
        public static final String INVITATION_EXPIRES_AT_DATE = "invitation_expires_at_date";
        public static final String INVITATION_EXPIRES_ON = "invitation_expires_on";
        public static final String INVITATION_FRIDAY = "invitation_friday";
        public static final String INVITATION_FULL_OWNER_NAME = "invitation_full_owner_name";
        public static final String INVITATION_GUEST_ID = "invitation_guest_id";
        public static final String INVITATION_GUEST_INTERFACE_SELECTION_MODE = "invitation_guest_interface_selection_mode";
        public static final String INVITATION_GUEST_PERMISSIONS_ID = "invitation_guest_permissions_id";
        public static final String INVITATION_ID = "invitation_id";
        public static final String INVITATION_IS_EXPIRED = "invitation_is_expired";
        public static final String INVITATION_MESSAGE = "invitation_message";
        public static final String INVITATION_MODIFIED_ON = "invitation_modified_on";
        public static final String INVITATION_MONDAY = "invitation_monday";
        public static final String INVITATION_OPEN_SHACKLE_PERMISSION = "invitation_open_shackle_permission";
        public static final String INVITATION_PRODUCT_ID = "invitation_product_id";
        public static final String INVITATION_SATURDAY = "invitation_saturday";
        public static final String INVITATION_SCHEDULE_TYPE = "invitation_schedule_type";
        public static final String INVITATION_START_AT_DATE = "invitation_start_at_date";
        public static final String INVITATION_STATUS = "invitation_status";
        public static final String INVITATION_SUNDAY = "invitation_sunday";
        public static final String INVITATION_THURSDAY = "invitation_thursday";
        public static final String INVITATION_TUESDAY = "invitation_tuesday";
        public static final String INVITATION_URL = "invitation_url";
        public static final String INVITATION_USER_TYPE = "invitation_user_type";
        public static final String INVITATION_VIEW_LAST_KNOWN_LOCATION_PERMISSION = "invitation_view_last_known_location_permission";
        public static final String INVITATION_VIEW_TEMPORARY_PERMISSION = "invitation_view_temporary_code_permission";
        public static final String INVITATION_WEDNESDAY = "invitation_wednesday";
    }

    public static class Keys implements KeysColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.key";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.key";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("keys").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildKeyUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getKeyId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface KeysColumns {
        public static final String ALIAS = "alias";
        public static final String DEVICE_ID = "device_id";
        public static final String KEY_CREATED_ON = "key_created_on";
        public static final String KEY_EXPIRES_ON = "key_expires_on";
        public static final String KEY_ID = "key_id";
        public static final String KEY_MODIFIED_ON = "key_modified_on";
        public static final String KEY_USER_TYPE = "key_user_type";
        public static final String KEY_VALUE = "key_value";
        public static final String KMS_DEVICE_ID = "kms_device_id";
        public static final String PRODUCT_INVITATION_ID = "product_invitation_id";
        public static final String PROFILE_VALUE = "profile_value";
        public static final String USER_ID = "user_id";
    }

    public static class Locks implements LocksColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.lock";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.lock";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("locks").build();
        public static final String DEFAULT_SORT = "locks.lock_id ASC";

        public static Uri buildLockUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getLockId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }

        public static Uri buildKeysDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("keys").build();
        }

        public static Uri buildInvitationsDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("invitations").build();
        }

        public static Uri buildLogsDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("logs").build();
        }

        public static Uri buildAvailableCommandsDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("available_commands").build();
        }

        public static Uri buildAvailableSettingsDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("available_settings").build();
        }

        public static Uri buildCalibrationDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("calibration").build();
        }

        public static Uri buildProductCodesDirUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).appendPath("product_codes").build();
        }
    }

    interface LocksColumns {
        public static final String BATTERY_LEVEL = "battery_level";
        public static final String CREATED_ON = "created_on";
        public static final String CREATED_ON_TIMEZONE_ADJUSTED = "created_on_timezone_adjusted";
        public static final String FIRMWARE_COUNTER = "firmware_counter";
        public static final String FIRMWARE_VERSION = "firmware_version";
        public static final String IS_FAVORITE = "is_favorite";
        public static final String KMS_CREATED_ON = "kms_created_on";
        public static final String KMS_ID = "kms_id";
        public static final String KMS_MODIFIED_ON = "kms_modified_on";
        public static final String LAST_LOG_REFERENCE_ID = "last_log_reference_id";
        public static final String LAST_UNLOCKED = "last_unlocked";
        public static final String LAST_UNLOCKED_SHACKLE = "last_unlocked_shackle";
        public static final String LATITUDE = "latitude";
        public static final String LOCKER_MODE = "locker_mode";
        public static final String LOCK_ID = "lock_id";
        public static final String LOCK_LABEL = "lock_label";
        public static final String LOCK_MODE = "lock_mode";
        public static final String LOCK_NAME = "lock_name";
        public static final String LOCK_NOTES = "lock_notes";
        public static final String LOCK_STATUS = "lock_status";
        public static final String LONGITUDE = "longitude";
        public static final String MEASUREMENT_COUNTER = "measurement_counter";
        public static final String MEMORY_MAP_VERSION = "memory_map_version";
        public static final String MODEL_DESCRIPTION = "model_description";
        public static final String MODEL_ID = "model_id";
        public static final String MODEL_INTERFACE_ID = "model_interface_id";
        public static final String MODEL_NAME = "model_name";
        public static final String MODEL_NUMBER = "model_number";
        public static final String MODEL_SKU = "model_sku";
        public static final String MODIFIED_ON = "modified_on";
        public static final String MODIFIED_ON_TIMEZONE_ADJUSTED = "modified_on_timezone_adjusted";
        public static final String PERMISSION_EXPIRES_AT_DATE = "permission_expires_at_date";
        public static final String PERMISSION_FRIDAY = "permission_friday";
        public static final String PERMISSION_MONDAY = "permission_monday";
        public static final String PERMISSION_OPEN_SHACKLE_PERMISSION = "permission_open_shackle_permission";
        public static final String PERMISSION_SATURDAY = "permission_saturday";
        public static final String PERMISSION_SCHEDULE_TYPE = "permission_schedule_type";
        public static final String PERMISSION_START_AT_DATE = "permission_start_at_date";
        public static final String PERMISSION_SUNDAY = "permission_sunday";
        public static final String PERMISSION_THURSDAY = "permission_thursday";
        public static final String PERMISSION_TUESDAY = "permission_tuesday";
        public static final String PERMISSION_VIEW_LAST_KNOWN_LOCATION_PERMISSION = "permission_view_last_known_location_permission";
        public static final String PERMISSION_VIEW_MODE = "permission_view_type";
        public static final String PERMISSION_VIEW_TEMPORARY_PERMISSION = "permission_view_temporary_code_permission";
        public static final String PERMISSION_WEDNESDAY = "permission_wednesday";
        public static final String PRIMARY_CODE = "primary_code";
        public static final String PRIMARY_PASSCODE_COUNTER = "primary_passcode_counter";
        public static final String PROXIMITY_SWIPE_MODE_CONFIGURATION = "proximity_swipe_mode_configuration";
        public static final String PUBLIC_CONFIG_COUNTER = "public_config_counter";
        public static final String RELOCK_TIME = "relock_time";
        public static final String SECONDARY_CODE_1 = "secondary_code_1";
        public static final String SECONDARY_CODE_2 = "secondary_code_2";
        public static final String SECONDARY_CODE_3 = "secondary_code_3";
        public static final String SECONDARY_CODE_4 = "secondary_code_4";
        public static final String SECONDARY_CODE_5 = "secondary_code_5";
        public static final String SECONDARY_PASSCODE_COUNTER = "secondary_passcode_counter";
        public static final String SECTION_NUMBER = "section_number";
        public static final String SERVICE_CODE = "service_code";
        public static final String SERVICE_CODE_EXPIRATION = "service_code_expiration";
        public static final String SHACKLE_STATUS = "shackle_status";
        public static final String TEMPERATURE = "temperature";
        public static final String TIMEZONE = "timezone";
        public static final String TOUCH_MODE_CONFIGURATION = "touch_mode_configuration";
        public static final String TX_INTERVAL = "tx_interval";
        public static final String TX_POWER = "tx_power";
        public static final String USER_TYPE = "user_type";
    }

    interface LogColumns {
        public static final String ALIAS = "alias";
        public static final String CREATED_ON = "created_on";
        public static final String EVENT_CODE = "event_code";
        public static final String EVENT_SOURCE = "event_source";
        public static final String EVENT_VALUE = "event_value";
        public static final String FIRMWARE_COUNTER = "firmware_counter";
        public static final String KMS_DEVICE_KEY_ALIAS = "kms_device_key_alias";
        public static final String LOG_ID = "log_id";
        public static final String LOG_KMS_DEVICE_ID = "kms_device_id";
        public static final String MESSAGE = "message";
        public static final String REFERENCE_ID = "reference_id";
    }

    public static class Logs implements LogColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.log";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.log";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("logs").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildLogUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getLogId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    public static class ProductCodes implements ProductCodesColumns, BaseColumns, SyncColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.product_codes";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.product_codes";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("product_codes").build();
        public static final String DEFAULT_SORT = "display_order ASC";

        public static Uri buildProductCodesUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getProductCodeId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface ProductCodesColumns {
        public static final String CODE_ID = "id";
        public static final String DISPLAY_ORDER = "display_order";
        public static final String LOCK_ID = "lockId";
        public static final String NAME = "name";
        public static final String VALUE = "value";
    }

    public interface SyncColumns {
        public static final String UPDATED = "updated";
    }

    public static class WordDictionary implements WordDictionaryColumns, BaseColumns {
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.masterlock.ble.word_dictionary";
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.masterlock.ble.word_dictionary";
        public static final Uri CONTENT_URI = MasterlockContract.BASE_CONTENT_URI.buildUpon().appendPath("word_dictionary").build();
        public static final String DEFAULT_SORT = "_id ASC";

        public static Uri buildWordDictionaryUri(String str) {
            return CONTENT_URI.buildUpon().appendPath(str).build();
        }

        public static String getWordId(Uri uri) {
            return (String) uri.getPathSegments().get(1);
        }
    }

    interface WordDictionaryColumns {
        public static final String WORD_DICTIONARY_ID = "word_dictionary_id";
        public static final String WORD_DICTIONARY_WORD = "word_dictionary_word";
    }

    private MasterlockContract() {
    }
}
