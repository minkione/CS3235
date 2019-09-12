package com.masterlock.core;

import com.google.gson.annotations.SerializedName;

public class ComplementaryCode {
    @SerializedName("CodeType")
    public CodeType codeType;
    @SerializedName("DisplayOrder")
    public int displayOrder;
    @SerializedName("Id")

    /* renamed from: id */
    public String f187id;
    @SerializedName("ModifiedOn")
    public String modifiedOn;
    @SerializedName("Name")
    public String name;
    @SerializedName("OperationType")
    public OperationType operationType;
    @SerializedName("Value")
    public String value;
}
