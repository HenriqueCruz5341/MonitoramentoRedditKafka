package ufes.kafka.apis.dtos.search;

import com.google.gson.annotations.SerializedName;

import ufes.kafka.apis.dtos.common.DataDto;

public class SearchDto {
    @SerializedName("data")
    public DataDto data;

    public DataDto getData() {
        return data;
    }

    public void setData(DataDto data) {
        this.data = data;
    }
}
