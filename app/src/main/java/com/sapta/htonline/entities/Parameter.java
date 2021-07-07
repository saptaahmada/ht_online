package com.sapta.htonline.entities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Parameter {

    @SerializedName("TARGET_PERFORMA_ID")
    @Expose
    private String targetPerformaId;
    @SerializedName("TYPE_CODE")
    @Expose
    private String typeCode;
    @SerializedName("TYPE_NAME")
    @Expose
    private String typeName;
    @SerializedName("TVALUE")
    @Expose
    private String tvalue;
    @SerializedName("CREATED_DATE")
    @Expose
    private String createdDate;
    @SerializedName("CREATED_BY")
    @Expose
    private String createdBy;
    @SerializedName("UPDATED_DATE")
    @Expose
    private String updatedDate;
    @SerializedName("UPDATED_BY")
    @Expose
    private Object updatedBy;
    @SerializedName("TVALUE2")
    @Expose
    private String tvalue2;
    @SerializedName("TVALUE3")
    @Expose
    private String tvalue3;
    @SerializedName("TVALUE4")
    @Expose
    private String tvalue4;
    @SerializedName("TVALUE5")
    @Expose
    private Object tvalue5;

    public String getTargetPerformaId() {
        return targetPerformaId;
    }

    public void setTargetPerformaId(String targetPerformaId) {
        this.targetPerformaId = targetPerformaId;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTvalue() {
        return tvalue;
    }

    public void setTvalue(String tvalue) {
        this.tvalue = tvalue;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getTvalue2() {
        return tvalue2;
    }

    public void setTvalue2(String tvalue2) {
        this.tvalue2 = tvalue2;
    }

    public String getTvalue3() {
        return tvalue3;
    }

    public void setTvalue3(String tvalue3) {
        this.tvalue3 = tvalue3;
    }

    public String getTvalue4() {
        return tvalue4;
    }

    public void setTvalue4(String tvalue4) {
        this.tvalue4 = tvalue4;
    }

    public Object getTvalue5() {
        return tvalue5;
    }

    public void setTvalue5(Object tvalue5) {
        this.tvalue5 = tvalue5;
    }

}