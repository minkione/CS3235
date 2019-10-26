package com.masterlock.core.comparator;

import com.masterlock.core.AccessType;
import com.masterlock.core.KmsLogEntry;
import com.masterlock.core.Lock;
import java.util.Comparator;

public enum LockComparator implements Comparator<Lock> {
    OWNER_FIRST_SORT {
        public int compare(Lock lock, Lock lock2) {
            if (lock.getAccessType() != AccessType.OWNER || lock2.getAccessType() == AccessType.OWNER) {
                return (lock.getAccessType() == AccessType.OWNER || lock2.getAccessType() != AccessType.OWNER) ? 0 : 1;
            }
            return -1;
        }
    },
    SECTION_SORT {
        public int compare(Lock lock, Lock lock2) {
            return lock.getSectionNumber() - lock2.getSectionNumber();
        }
    },
    LOCK_ID_SORT {
        public int compare(Lock lock, Lock lock2) {
            return lock.getLockId().compareTo(lock2.getLockId());
        }
    },
    NAME_SORT {
        public int compare(Lock lock, Lock lock2) {
            return lock.getName().compareToIgnoreCase(lock2.getName());
        }
    },
    USER_TYPE_SORT {
        public int compare(Lock lock, Lock lock2) {
            return lock.getAccessType().compareTo(lock2.getAccessType());
        }
    },
    LOCK_NAME_ASC_SORT {
        public int compare(Lock lock, Lock lock2) {
            return lock.getName().compareToIgnoreCase(lock2.getName());
        }
    },
    LOCK_NAME_DES_SORT {
        public int compare(Lock lock, Lock lock2) {
            return lock2.getName().compareToIgnoreCase(lock.getName());
        }
    },
    RECENTLY_ACCESSED_SORT {
        public int compare(Lock lock, Lock lock2) {
            if (lock.getLogs() == null || lock.getLogs().isEmpty()) {
                return 1;
            }
            if (lock2.getLogs() == null || lock2.getLogs().isEmpty()) {
                return -1;
            }
            return ((KmsLogEntry) lock2.getLogs().get(0)).getCreatedOn().compareToIgnoreCase(((KmsLogEntry) lock.getLogs().get(0)).getCreatedOn());
        }
    },
    PRODUCT_TYPE_SORT {
        public int compare(Lock lock, Lock lock2) {
            return lock.getModelNumber().compareToIgnoreCase(lock2.getModelNumber());
        }
    };

    public enum SortId {
        private static final /* synthetic */ SortId[] $VALUES = null;
        public static final SortId LOCK_ID_SORT = null;
        public static final SortId LOCK_NAME_ASC_SORT = null;
        public static final SortId LOCK_NAME_DES_SORT = null;
        public static final SortId NAME_SORT = null;
        public static final SortId OWNER_FIRST_SORT = null;
        public static final SortId PRODUCT_TYPE_SORT = null;
        public static final SortId RECENTLY_ACCESSED_SORT = null;
        public static final SortId USER_TYPE_SORT = null;
        private String sortId;

        public static SortId valueOf(String str) {
            return (SortId) Enum.valueOf(SortId.class, str);
        }

        public static SortId[] values() {
            return (SortId[]) $VALUES.clone();
        }

        static {
            OWNER_FIRST_SORT = new SortId("OWNER_FIRST_SORT", 0, "OWNER_FIRST_SORT");
            LOCK_ID_SORT = new SortId("LOCK_ID_SORT", 1, "LOCK_ID_SORT");
            NAME_SORT = new SortId("NAME_SORT", 2, "NAME_SORT");
            USER_TYPE_SORT = new SortId("USER_TYPE_SORT", 3, "USER_TYPE_SORT");
            LOCK_NAME_ASC_SORT = new SortId("LOCK_NAME_ASC_SORT", 4, "LOCK_NAME_ASC_SORT");
            LOCK_NAME_DES_SORT = new SortId("LOCK_NAME_DES_SORT", 5, "LOCK_NAME_DES_SORT");
            RECENTLY_ACCESSED_SORT = new SortId("RECENTLY_ACCESSED_SORT", 6, "RECENTLY_ACCESSED_SORT");
            PRODUCT_TYPE_SORT = new SortId("PRODUCT_TYPE_SORT", 7, "PRODUCT_TYPE_SORT");
            $VALUES = new SortId[]{OWNER_FIRST_SORT, LOCK_ID_SORT, NAME_SORT, USER_TYPE_SORT, LOCK_NAME_ASC_SORT, LOCK_NAME_DES_SORT, RECENTLY_ACCESSED_SORT, PRODUCT_TYPE_SORT};
        }

        private SortId(String str, int i, String str2) {
            this.sortId = str2;
        }

        public String getSortId() {
            return this.sortId;
        }
    }

    public static Comparator<Lock> descending(final Comparator<Lock> comparator) {
        return new Comparator<Lock>() {
            public int compare(Lock lock, Lock lock2) {
                return comparator.compare(lock, lock2) * -1;
            }
        };
    }

    public static Comparator<Lock> getComparator(final Comparator<Lock>... comparatorArr) {
        return new Comparator<Lock>() {
            public int compare(Lock lock, Lock lock2) {
                for (Comparator compare : comparatorArr) {
                    int compare2 = compare.compare(lock, lock2);
                    if (compare2 != 0) {
                        return compare2;
                    }
                }
                return 0;
            }
        };
    }
}
