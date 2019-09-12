package com.masterlock.core.comparator;

import com.masterlock.core.KmsLogEntry;
import java.util.Comparator;

public enum LogComparator implements Comparator<KmsLogEntry> {
    CREATED_ON_SORT {
        public int compare(KmsLogEntry kmsLogEntry, KmsLogEntry kmsLogEntry2) {
            return kmsLogEntry2.getCreatedOn().compareToIgnoreCase(kmsLogEntry.getCreatedOn());
        }
    },
    REFERENCE_ID_SORT {
        public int compare(KmsLogEntry kmsLogEntry, KmsLogEntry kmsLogEntry2) {
            return kmsLogEntry.getEventIndex() < kmsLogEntry2.getEventIndex() ? 1 : -1;
        }
    };

    public static Comparator<KmsLogEntry> descending(Comparator<KmsLogEntry> comparator) {
        return new Comparator(comparator) {
            private final /* synthetic */ Comparator f$0;

            {
                this.f$0 = r1;
            }

            public final int compare(Object obj, Object obj2) {
                return LogComparator.lambda$descending$0(this.f$0, (KmsLogEntry) obj, (KmsLogEntry) obj2);
            }
        };
    }

    public static Comparator<KmsLogEntry> getComparator(Comparator<KmsLogEntry>... comparatorArr) {
        return new Comparator(comparatorArr) {
            private final /* synthetic */ Comparator[] f$0;

            {
                this.f$0 = r1;
            }

            public final int compare(Object obj, Object obj2) {
                return LogComparator.lambda$getComparator$1(this.f$0, (KmsLogEntry) obj, (KmsLogEntry) obj2);
            }
        };
    }
}
