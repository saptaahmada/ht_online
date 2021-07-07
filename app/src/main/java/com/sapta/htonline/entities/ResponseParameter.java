package com.sapta.htonline.entities;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseParameter {

    @SerializedName("data")
    @Expose
    private List<Parameter> data = null;
    @SerializedName("success")
    @Expose
    private Boolean success;

    public List<Parameter> getData() {
        return data;
    }

    public void setData(List<Parameter> data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}