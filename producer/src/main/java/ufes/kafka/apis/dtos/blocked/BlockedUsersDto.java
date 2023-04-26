package ufes.kafka.apis.dtos.blocked;

import com.google.gson.annotations.SerializedName;

public class BlockedUsersDto {
    @SerializedName("data")
    public DataDto data;

    public DataDto getData() {
        return data;
    }

    public void setData(DataDto data) {
        this.data = data;
    }
}
