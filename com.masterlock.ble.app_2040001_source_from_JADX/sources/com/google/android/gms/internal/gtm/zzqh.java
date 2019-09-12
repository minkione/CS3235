package com.google.android.gms.internal.gtm;

import java.io.IOException;
import java.util.List;

final class zzqh implements zzsy {
    private int tag;
    private final zzqe zzawp;
    private int zzawq;
    private int zzawr = 0;

    public static zzqh zza(zzqe zzqe) {
        if (zzqe.zzawi != null) {
            return zzqe.zzawi;
        }
        return new zzqh(zzqe);
    }

    private zzqh(zzqe zzqe) {
        this.zzawp = (zzqe) zzre.zza(zzqe, "input");
        this.zzawp.zzawi = this;
    }

    public final int zzog() throws IOException {
        int i = this.zzawr;
        if (i != 0) {
            this.tag = i;
            this.zzawr = 0;
        } else {
            this.tag = this.zzawp.zzni();
        }
        int i2 = this.tag;
        if (i2 == 0 || i2 == this.zzawq) {
            return Integer.MAX_VALUE;
        }
        return i2 >>> 3;
    }

    public final int getTag() {
        return this.tag;
    }

    public final boolean zzoh() throws IOException {
        if (!this.zzawp.zzny()) {
            int i = this.tag;
            if (i != this.zzawq) {
                return this.zzawp.zzao(i);
            }
        }
        return false;
    }

    private final void zzat(int i) throws IOException {
        if ((this.tag & 7) != i) {
            throw zzrk.zzpt();
        }
    }

    public final double readDouble() throws IOException {
        zzat(1);
        return this.zzawp.readDouble();
    }

    public final float readFloat() throws IOException {
        zzat(5);
        return this.zzawp.readFloat();
    }

    public final long zznj() throws IOException {
        zzat(0);
        return this.zzawp.zznj();
    }

    public final long zznk() throws IOException {
        zzat(0);
        return this.zzawp.zznk();
    }

    public final int zznl() throws IOException {
        zzat(0);
        return this.zzawp.zznl();
    }

    public final long zznm() throws IOException {
        zzat(1);
        return this.zzawp.zznm();
    }

    public final int zznn() throws IOException {
        zzat(5);
        return this.zzawp.zznn();
    }

    public final boolean zzno() throws IOException {
        zzat(0);
        return this.zzawp.zzno();
    }

    public final String readString() throws IOException {
        zzat(2);
        return this.zzawp.readString();
    }

    public final String zznp() throws IOException {
        zzat(2);
        return this.zzawp.zznp();
    }

    public final <T> T zza(zzsz<T> zzsz, zzqp zzqp) throws IOException {
        zzat(2);
        return zzc(zzsz, zzqp);
    }

    public final <T> T zzb(zzsz<T> zzsz, zzqp zzqp) throws IOException {
        zzat(3);
        return zzd(zzsz, zzqp);
    }

    private final <T> T zzc(zzsz<T> zzsz, zzqp zzqp) throws IOException {
        int zznr = this.zzawp.zznr();
        if (this.zzawp.zzawf < this.zzawp.zzawg) {
            int zzaq = this.zzawp.zzaq(zznr);
            T newInstance = zzsz.newInstance();
            this.zzawp.zzawf++;
            zzsz.zza(newInstance, this, zzqp);
            zzsz.zzt(newInstance);
            this.zzawp.zzan(0);
            this.zzawp.zzawf--;
            this.zzawp.zzar(zzaq);
            return newInstance;
        }
        throw zzrk.zzpu();
    }

    private final <T> T zzd(zzsz<T> zzsz, zzqp zzqp) throws IOException {
        int i = this.zzawq;
        this.zzawq = ((this.tag >>> 3) << 3) | 4;
        try {
            T newInstance = zzsz.newInstance();
            zzsz.zza(newInstance, this, zzqp);
            zzsz.zzt(newInstance);
            if (this.tag == this.zzawq) {
                return newInstance;
            }
            throw zzrk.zzpv();
        } finally {
            this.zzawq = i;
        }
    }

    public final zzps zznq() throws IOException {
        zzat(2);
        return this.zzawp.zznq();
    }

    public final int zznr() throws IOException {
        zzat(0);
        return this.zzawp.zznr();
    }

    public final int zzns() throws IOException {
        zzat(0);
        return this.zzawp.zzns();
    }

    public final int zznt() throws IOException {
        zzat(5);
        return this.zzawp.zznt();
    }

    public final long zznu() throws IOException {
        zzat(1);
        return this.zzawp.zznu();
    }

    public final int zznv() throws IOException {
        zzat(0);
        return this.zzawp.zznv();
    }

    public final long zznw() throws IOException {
        zzat(0);
        return this.zzawp.zznw();
    }

    public final void zzg(List<Double> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzqm) {
            zzqm zzqm = (zzqm) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    int zznr = this.zzawp.zznr();
                    zzau(zznr);
                    int zznz = this.zzawp.zznz() + zznr;
                    do {
                        zzqm.zzd(this.zzawp.readDouble());
                    } while (this.zzawp.zznz() < zznz);
                    return;
                default:
                    throw zzrk.zzpt();
            }
            do {
                zzqm.zzd(this.zzawp.readDouble());
                if (!this.zzawp.zzny()) {
                    zzni2 = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni2 == this.tag);
            this.zzawr = zzni2;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                int zznr2 = this.zzawp.zznr();
                zzau(zznr2);
                int zznz2 = this.zzawp.zznz() + zznr2;
                do {
                    list.add(Double.valueOf(this.zzawp.readDouble()));
                } while (this.zzawp.zznz() < zznz2);
                return;
            default:
                throw zzrk.zzpt();
        }
        do {
            list.add(Double.valueOf(this.zzawp.readDouble()));
            if (!this.zzawp.zzny()) {
                zzni = this.zzawp.zzni();
            } else {
                return;
            }
        } while (zzni == this.tag);
        this.zzawr = zzni;
    }

    public final void zzh(List<Float> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzqz) {
            zzqz zzqz = (zzqz) list;
            int i = this.tag & 7;
            if (i == 2) {
                int zznr = this.zzawp.zznr();
                zzav(zznr);
                int zznz = this.zzawp.zznz() + zznr;
                do {
                    zzqz.zzc(this.zzawp.readFloat());
                } while (this.zzawp.zznz() < zznz);
            } else if (i == 5) {
                do {
                    zzqz.zzc(this.zzawp.readFloat());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 2) {
                int zznr2 = this.zzawp.zznr();
                zzav(zznr2);
                int zznz2 = this.zzawp.zznz() + zznr2;
                do {
                    list.add(Float.valueOf(this.zzawp.readFloat()));
                } while (this.zzawp.zznz() < zznz2);
            } else if (i2 == 5) {
                do {
                    list.add(Float.valueOf(this.zzawp.readFloat()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzi(List<Long> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzry) {
            zzry zzry = (zzry) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzry.zzaa(this.zzawp.zznj());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzry.zzaa(this.zzawp.zznj());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Long.valueOf(this.zzawp.zznj()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Long.valueOf(this.zzawp.zznj()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzj(List<Long> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzry) {
            zzry zzry = (zzry) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzry.zzaa(this.zzawp.zznk());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzry.zzaa(this.zzawp.zznk());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Long.valueOf(this.zzawp.zznk()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Long.valueOf(this.zzawp.zznk()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzk(List<Integer> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzrd) {
            zzrd zzrd = (zzrd) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzrd.zzbm(this.zzawp.zznl());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzrd.zzbm(this.zzawp.zznl());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzawp.zznl()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Integer.valueOf(this.zzawp.zznl()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzl(List<Long> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzry) {
            zzry zzry = (zzry) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    int zznr = this.zzawp.zznr();
                    zzau(zznr);
                    int zznz = this.zzawp.zznz() + zznr;
                    do {
                        zzry.zzaa(this.zzawp.zznm());
                    } while (this.zzawp.zznz() < zznz);
                    return;
                default:
                    throw zzrk.zzpt();
            }
            do {
                zzry.zzaa(this.zzawp.zznm());
                if (!this.zzawp.zzny()) {
                    zzni2 = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni2 == this.tag);
            this.zzawr = zzni2;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                int zznr2 = this.zzawp.zznr();
                zzau(zznr2);
                int zznz2 = this.zzawp.zznz() + zznr2;
                do {
                    list.add(Long.valueOf(this.zzawp.zznm()));
                } while (this.zzawp.zznz() < zznz2);
                return;
            default:
                throw zzrk.zzpt();
        }
        do {
            list.add(Long.valueOf(this.zzawp.zznm()));
            if (!this.zzawp.zzny()) {
                zzni = this.zzawp.zzni();
            } else {
                return;
            }
        } while (zzni == this.tag);
        this.zzawr = zzni;
    }

    public final void zzm(List<Integer> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzrd) {
            zzrd zzrd = (zzrd) list;
            int i = this.tag & 7;
            if (i == 2) {
                int zznr = this.zzawp.zznr();
                zzav(zznr);
                int zznz = this.zzawp.zznz() + zznr;
                do {
                    zzrd.zzbm(this.zzawp.zznn());
                } while (this.zzawp.zznz() < zznz);
            } else if (i == 5) {
                do {
                    zzrd.zzbm(this.zzawp.zznn());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 2) {
                int zznr2 = this.zzawp.zznr();
                zzav(zznr2);
                int zznz2 = this.zzawp.zznz() + zznr2;
                do {
                    list.add(Integer.valueOf(this.zzawp.zznn()));
                } while (this.zzawp.zznz() < zznz2);
            } else if (i2 == 5) {
                do {
                    list.add(Integer.valueOf(this.zzawp.zznn()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzn(List<Boolean> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzpq) {
            zzpq zzpq = (zzpq) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzpq.addBoolean(this.zzawp.zzno());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzpq.addBoolean(this.zzawp.zzno());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Boolean.valueOf(this.zzawp.zzno()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Boolean.valueOf(this.zzawp.zzno()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void readStringList(List<String> list) throws IOException {
        zza(list, false);
    }

    public final void zzo(List<String> list) throws IOException {
        zza(list, true);
    }

    private final void zza(List<String> list, boolean z) throws IOException {
        int zzni;
        int zzni2;
        if ((this.tag & 7) != 2) {
            throw zzrk.zzpt();
        } else if (!(list instanceof zzrt) || z) {
            do {
                list.add(z ? zznp() : readString());
                if (!this.zzawp.zzny()) {
                    zzni = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni == this.tag);
            this.zzawr = zzni;
        } else {
            zzrt zzrt = (zzrt) list;
            do {
                zzrt.zzc(zznq());
                if (!this.zzawp.zzny()) {
                    zzni2 = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni2 == this.tag);
            this.zzawr = zzni2;
        }
    }

    public final <T> void zza(List<T> list, zzsz<T> zzsz, zzqp zzqp) throws IOException {
        int zzni;
        int i = this.tag;
        if ((i & 7) == 2) {
            do {
                list.add(zzc(zzsz, zzqp));
                if (!this.zzawp.zzny() && this.zzawr == 0) {
                    zzni = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni == i);
            this.zzawr = zzni;
            return;
        }
        throw zzrk.zzpt();
    }

    public final <T> void zzb(List<T> list, zzsz<T> zzsz, zzqp zzqp) throws IOException {
        int zzni;
        int i = this.tag;
        if ((i & 7) == 3) {
            do {
                list.add(zzd(zzsz, zzqp));
                if (!this.zzawp.zzny() && this.zzawr == 0) {
                    zzni = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni == i);
            this.zzawr = zzni;
            return;
        }
        throw zzrk.zzpt();
    }

    public final void zzp(List<zzps> list) throws IOException {
        int zzni;
        if ((this.tag & 7) == 2) {
            do {
                list.add(zznq());
                if (!this.zzawp.zzny()) {
                    zzni = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni == this.tag);
            this.zzawr = zzni;
            return;
        }
        throw zzrk.zzpt();
    }

    public final void zzq(List<Integer> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzrd) {
            zzrd zzrd = (zzrd) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzrd.zzbm(this.zzawp.zznr());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzrd.zzbm(this.zzawp.zznr());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzawp.zznr()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Integer.valueOf(this.zzawp.zznr()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzr(List<Integer> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzrd) {
            zzrd zzrd = (zzrd) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzrd.zzbm(this.zzawp.zzns());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzrd.zzbm(this.zzawp.zzns());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzawp.zzns()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Integer.valueOf(this.zzawp.zzns()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzs(List<Integer> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzrd) {
            zzrd zzrd = (zzrd) list;
            int i = this.tag & 7;
            if (i == 2) {
                int zznr = this.zzawp.zznr();
                zzav(zznr);
                int zznz = this.zzawp.zznz() + zznr;
                do {
                    zzrd.zzbm(this.zzawp.zznt());
                } while (this.zzawp.zznz() < zznz);
            } else if (i == 5) {
                do {
                    zzrd.zzbm(this.zzawp.zznt());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 2) {
                int zznr2 = this.zzawp.zznr();
                zzav(zznr2);
                int zznz2 = this.zzawp.zznz() + zznr2;
                do {
                    list.add(Integer.valueOf(this.zzawp.zznt()));
                } while (this.zzawp.zznz() < zznz2);
            } else if (i2 == 5) {
                do {
                    list.add(Integer.valueOf(this.zzawp.zznt()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzt(List<Long> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzry) {
            zzry zzry = (zzry) list;
            switch (this.tag & 7) {
                case 1:
                    break;
                case 2:
                    int zznr = this.zzawp.zznr();
                    zzau(zznr);
                    int zznz = this.zzawp.zznz() + zznr;
                    do {
                        zzry.zzaa(this.zzawp.zznu());
                    } while (this.zzawp.zznz() < zznz);
                    return;
                default:
                    throw zzrk.zzpt();
            }
            do {
                zzry.zzaa(this.zzawp.zznu());
                if (!this.zzawp.zzny()) {
                    zzni2 = this.zzawp.zzni();
                } else {
                    return;
                }
            } while (zzni2 == this.tag);
            this.zzawr = zzni2;
            return;
        }
        switch (this.tag & 7) {
            case 1:
                break;
            case 2:
                int zznr2 = this.zzawp.zznr();
                zzau(zznr2);
                int zznz2 = this.zzawp.zznz() + zznr2;
                do {
                    list.add(Long.valueOf(this.zzawp.zznu()));
                } while (this.zzawp.zznz() < zznz2);
                return;
            default:
                throw zzrk.zzpt();
        }
        do {
            list.add(Long.valueOf(this.zzawp.zznu()));
            if (!this.zzawp.zzny()) {
                zzni = this.zzawp.zzni();
            } else {
                return;
            }
        } while (zzni == this.tag);
        this.zzawr = zzni;
    }

    public final void zzu(List<Integer> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzrd) {
            zzrd zzrd = (zzrd) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzrd.zzbm(this.zzawp.zznv());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzrd.zzbm(this.zzawp.zznv());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Integer.valueOf(this.zzawp.zznv()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Integer.valueOf(this.zzawp.zznv()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    public final void zzv(List<Long> list) throws IOException {
        int zzni;
        int zzni2;
        if (list instanceof zzry) {
            zzry zzry = (zzry) list;
            int i = this.tag & 7;
            if (i == 0) {
                do {
                    zzry.zzaa(this.zzawp.zznw());
                    if (!this.zzawp.zzny()) {
                        zzni2 = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni2 == this.tag);
                this.zzawr = zzni2;
            } else if (i == 2) {
                int zznz = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    zzry.zzaa(this.zzawp.zznw());
                } while (this.zzawp.zznz() < zznz);
                zzaw(zznz);
            } else {
                throw zzrk.zzpt();
            }
        } else {
            int i2 = this.tag & 7;
            if (i2 == 0) {
                do {
                    list.add(Long.valueOf(this.zzawp.zznw()));
                    if (!this.zzawp.zzny()) {
                        zzni = this.zzawp.zzni();
                    } else {
                        return;
                    }
                } while (zzni == this.tag);
                this.zzawr = zzni;
            } else if (i2 == 2) {
                int zznz2 = this.zzawp.zznz() + this.zzawp.zznr();
                do {
                    list.add(Long.valueOf(this.zzawp.zznw()));
                } while (this.zzawp.zznz() < zznz2);
                zzaw(zznz2);
            } else {
                throw zzrk.zzpt();
            }
        }
    }

    private static void zzau(int i) throws IOException {
        if ((i & 7) != 0) {
            throw zzrk.zzpv();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0051, code lost:
        if (zzoh() != false) goto L_0x0053;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x005b, code lost:
        throw new com.google.android.gms.internal.gtm.zzrk("Unable to parse map entry.");
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004d */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final <K, V> void zza(java.util.Map<K, V> r6, com.google.android.gms.internal.gtm.zzsd<K, V> r7, com.google.android.gms.internal.gtm.zzqp r8) throws java.io.IOException {
        /*
            r5 = this;
            r0 = 2
            r5.zzat(r0)
            com.google.android.gms.internal.gtm.zzqe r0 = r5.zzawp
            int r0 = r0.zznr()
            com.google.android.gms.internal.gtm.zzqe r1 = r5.zzawp
            int r0 = r1.zzaq(r0)
            K r1 = r7.zzbcq
            V r2 = r7.zzbcs
        L_0x0014:
            int r3 = r5.zzog()     // Catch:{ all -> 0x0065 }
            r4 = 2147483647(0x7fffffff, float:NaN)
            if (r3 == r4) goto L_0x005c
            com.google.android.gms.internal.gtm.zzqe r4 = r5.zzawp     // Catch:{ all -> 0x0065 }
            boolean r4 = r4.zzny()     // Catch:{ all -> 0x0065 }
            if (r4 != 0) goto L_0x005c
            switch(r3) {
                case 1: goto L_0x003a;
                case 2: goto L_0x002d;
                default: goto L_0x0028;
            }
        L_0x0028:
            boolean r3 = r5.zzoh()     // Catch:{ zzrl -> 0x004d }
            goto L_0x0042
        L_0x002d:
            com.google.android.gms.internal.gtm.zzug r3 = r7.zzbcr     // Catch:{ zzrl -> 0x004d }
            V r4 = r7.zzbcs     // Catch:{ zzrl -> 0x004d }
            java.lang.Class r4 = r4.getClass()     // Catch:{ zzrl -> 0x004d }
            java.lang.Object r2 = r5.zza(r3, r4, r8)     // Catch:{ zzrl -> 0x004d }
            goto L_0x0014
        L_0x003a:
            com.google.android.gms.internal.gtm.zzug r3 = r7.zzbcp     // Catch:{ zzrl -> 0x004d }
            r4 = 0
            java.lang.Object r1 = r5.zza(r3, r4, r4)     // Catch:{ zzrl -> 0x004d }
            goto L_0x0014
        L_0x0042:
            if (r3 == 0) goto L_0x0045
            goto L_0x0014
        L_0x0045:
            com.google.android.gms.internal.gtm.zzrk r3 = new com.google.android.gms.internal.gtm.zzrk     // Catch:{ zzrl -> 0x004d }
            java.lang.String r4 = "Unable to parse map entry."
            r3.<init>(r4)     // Catch:{ zzrl -> 0x004d }
            throw r3     // Catch:{ zzrl -> 0x004d }
        L_0x004d:
            boolean r3 = r5.zzoh()     // Catch:{ all -> 0x0065 }
            if (r3 == 0) goto L_0x0054
            goto L_0x0014
        L_0x0054:
            com.google.android.gms.internal.gtm.zzrk r6 = new com.google.android.gms.internal.gtm.zzrk     // Catch:{ all -> 0x0065 }
            java.lang.String r7 = "Unable to parse map entry."
            r6.<init>(r7)     // Catch:{ all -> 0x0065 }
            throw r6     // Catch:{ all -> 0x0065 }
        L_0x005c:
            r6.put(r1, r2)     // Catch:{ all -> 0x0065 }
            com.google.android.gms.internal.gtm.zzqe r6 = r5.zzawp
            r6.zzar(r0)
            return
        L_0x0065:
            r6 = move-exception
            com.google.android.gms.internal.gtm.zzqe r7 = r5.zzawp
            r7.zzar(r0)
            throw r6
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.gtm.zzqh.zza(java.util.Map, com.google.android.gms.internal.gtm.zzsd, com.google.android.gms.internal.gtm.zzqp):void");
    }

    private final Object zza(zzug zzug, Class<?> cls, zzqp zzqp) throws IOException {
        switch (zzug) {
            case BOOL:
                return Boolean.valueOf(zzno());
            case BYTES:
                return zznq();
            case DOUBLE:
                return Double.valueOf(readDouble());
            case ENUM:
                return Integer.valueOf(zzns());
            case FIXED32:
                return Integer.valueOf(zznn());
            case FIXED64:
                return Long.valueOf(zznm());
            case FLOAT:
                return Float.valueOf(readFloat());
            case INT32:
                return Integer.valueOf(zznl());
            case INT64:
                return Long.valueOf(zznk());
            case MESSAGE:
                zzat(2);
                return zzc(zzsw.zzqs().zzi(cls), zzqp);
            case SFIXED32:
                return Integer.valueOf(zznt());
            case SFIXED64:
                return Long.valueOf(zznu());
            case SINT32:
                return Integer.valueOf(zznv());
            case SINT64:
                return Long.valueOf(zznw());
            case STRING:
                return zznp();
            case UINT32:
                return Integer.valueOf(zznr());
            case UINT64:
                return Long.valueOf(zznj());
            default:
                throw new RuntimeException("unsupported field type.");
        }
    }

    private static void zzav(int i) throws IOException {
        if ((i & 3) != 0) {
            throw zzrk.zzpv();
        }
    }

    private final void zzaw(int i) throws IOException {
        if (this.zzawp.zznz() != i) {
            throw zzrk.zzpp();
        }
    }
}
