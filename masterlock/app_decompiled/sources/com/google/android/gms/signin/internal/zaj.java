package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.internal.ResolveAccountResponse;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Class;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Field;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Param;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.VersionField;

@Class(creator = "SignInResponseCreator")
public final class zaj extends AbstractSafeParcelable {
    public static final Creator<zaj> CREATOR = new zak();
    @Field(getter = "getConnectionResult", mo9239id = 2)
    private final ConnectionResult zadh;
    @VersionField(mo9245id = 1)
    private final int zale;
    @Field(getter = "getResolveAccountResponse", mo9239id = 3)
    private final ResolveAccountResponse zasb;

    @Constructor
    zaj(@Param(mo9242id = 1) int i, @Param(mo9242id = 2) ConnectionResult connectionResult, @Param(mo9242id = 3) ResolveAccountResponse resolveAccountResponse) {
        this.zale = i;
        this.zadh = connectionResult;
        this.zasb = resolveAccountResponse;
    }

    public zaj(int i) {
        this(new ConnectionResult(8, null), null);
    }

    private zaj(ConnectionResult connectionResult, ResolveAccountResponse resolveAccountResponse) {
        this(1, connectionResult, null);
    }

    public final ConnectionResult getConnectionResult() {
        return this.zadh;
    }

    public final ResolveAccountResponse zacw() {
        return this.zasb;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zale);
        SafeParcelWriter.writeParcelable(parcel, 2, this.zadh, i, false);
        SafeParcelWriter.writeParcelable(parcel, 3, this.zasb, i, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
