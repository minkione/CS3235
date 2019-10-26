package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadata;
import com.google.i18n.phonenumbers.Phonemetadata.PhoneMetadataCollection;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import p008io.fabric.sdk.android.services.events.EventsFilesManager;

final class MultiFileMetadataSourceImpl implements MetadataSource {
    private static final String META_DATA_FILE_PREFIX = "/com/google/i18n/phonenumbers/data/PhoneNumberMetadataProto";
    private static final Logger logger = Logger.getLogger(MultiFileMetadataSourceImpl.class.getName());
    private final Map<Integer, PhoneMetadata> countryCodeToNonGeographicalMetadataMap;
    private final String currentFilePrefix;
    private final MetadataLoader metadataLoader;
    private final Map<String, PhoneMetadata> regionToMetadataMap;

    public MultiFileMetadataSourceImpl(String str, MetadataLoader metadataLoader2) {
        this.regionToMetadataMap = Collections.synchronizedMap(new HashMap());
        this.countryCodeToNonGeographicalMetadataMap = Collections.synchronizedMap(new HashMap());
        this.currentFilePrefix = str;
        this.metadataLoader = metadataLoader2;
    }

    public MultiFileMetadataSourceImpl(MetadataLoader metadataLoader2) {
        this(META_DATA_FILE_PREFIX, metadataLoader2);
    }

    public PhoneMetadata getMetadataForRegion(String str) {
        synchronized (this.regionToMetadataMap) {
            if (!this.regionToMetadataMap.containsKey(str)) {
                loadMetadataFromFile(this.currentFilePrefix, str, 0, this.metadataLoader);
            }
        }
        return (PhoneMetadata) this.regionToMetadataMap.get(str);
    }

    public PhoneMetadata getMetadataForNonGeographicalRegion(int i) {
        synchronized (this.countryCodeToNonGeographicalMetadataMap) {
            if (!this.countryCodeToNonGeographicalMetadataMap.containsKey(Integer.valueOf(i))) {
                loadMetadataFromFile(this.currentFilePrefix, PhoneNumberUtil.REGION_CODE_FOR_NON_GEO_ENTITY, i, this.metadataLoader);
            }
        }
        return (PhoneMetadata) this.countryCodeToNonGeographicalMetadataMap.get(Integer.valueOf(i));
    }

    /* access modifiers changed from: 0000 */
    public void loadMetadataFromFile(String str, String str2, int i, MetadataLoader metadataLoader2) {
        boolean equals = PhoneNumberUtil.REGION_CODE_FOR_NON_GEO_ENTITY.equals(str2);
        String valueOf = String.valueOf(equals ? String.valueOf(i) : str2);
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 1 + String.valueOf(valueOf).length());
        sb.append(str);
        sb.append(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
        sb.append(valueOf);
        String sb2 = sb.toString();
        InputStream loadMetadata = metadataLoader2.loadMetadata(sb2);
        if (loadMetadata == null) {
            Logger logger2 = logger;
            Level level = Level.SEVERE;
            String str3 = "missing metadata: ";
            String valueOf2 = String.valueOf(sb2);
            logger2.log(level, valueOf2.length() != 0 ? str3.concat(valueOf2) : new String(str3));
            String str4 = "missing metadata: ";
            String valueOf3 = String.valueOf(sb2);
            throw new IllegalStateException(valueOf3.length() != 0 ? str4.concat(valueOf3) : new String(str4));
        }
        try {
            List metadataList = loadMetadataAndCloseInput(new ObjectInputStream(loadMetadata)).getMetadataList();
            if (metadataList.isEmpty()) {
                Logger logger3 = logger;
                Level level2 = Level.SEVERE;
                String str5 = "empty metadata: ";
                String valueOf4 = String.valueOf(sb2);
                logger3.log(level2, valueOf4.length() != 0 ? str5.concat(valueOf4) : new String(str5));
                String str6 = "empty metadata: ";
                String valueOf5 = String.valueOf(sb2);
                throw new IllegalStateException(valueOf5.length() != 0 ? str6.concat(valueOf5) : new String(str6));
            }
            if (metadataList.size() > 1) {
                Logger logger4 = logger;
                Level level3 = Level.WARNING;
                String str7 = "invalid metadata (too many entries): ";
                String valueOf6 = String.valueOf(sb2);
                logger4.log(level3, valueOf6.length() != 0 ? str7.concat(valueOf6) : new String(str7));
            }
            PhoneMetadata phoneMetadata = (PhoneMetadata) metadataList.get(0);
            if (equals) {
                this.countryCodeToNonGeographicalMetadataMap.put(Integer.valueOf(i), phoneMetadata);
            } else {
                this.regionToMetadataMap.put(str2, phoneMetadata);
            }
        } catch (IOException e) {
            Logger logger5 = logger;
            Level level4 = Level.SEVERE;
            String str8 = "cannot load/parse metadata: ";
            String valueOf7 = String.valueOf(sb2);
            logger5.log(level4, valueOf7.length() != 0 ? str8.concat(valueOf7) : new String(str8), e);
            String str9 = "cannot load/parse metadata: ";
            String valueOf8 = String.valueOf(sb2);
            throw new RuntimeException(valueOf8.length() != 0 ? str9.concat(valueOf8) : new String(str9), e);
        }
    }

    private static PhoneMetadataCollection loadMetadataAndCloseInput(ObjectInputStream objectInputStream) {
        PhoneMetadataCollection phoneMetadataCollection = new PhoneMetadataCollection();
        try {
            phoneMetadataCollection.readExternal(objectInputStream);
            try {
            } catch (IOException e) {
                logger.log(Level.WARNING, "error closing input stream (ignored)", e);
            }
        } catch (IOException e2) {
            logger.log(Level.WARNING, "error reading input (ignored)", e2);
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException e3) {
                logger.log(Level.WARNING, "error closing input stream (ignored)", e3);
            }
        }
        return phoneMetadataCollection;
    }
}
