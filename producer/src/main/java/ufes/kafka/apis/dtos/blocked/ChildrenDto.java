package ufes.kafka.apis.dtos.blocked;

import com.google.gson.annotations.SerializedName;

public class ChildrenDto {
    @SerializedName("date")
    public float date;
    @SerializedName("rel_id")
    public String relId;
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;

    public float getDate() {
        return date;
    }

    public void setDate(float date) {
        this.date = date;
    }

    public String getRelId() {
        return relId;
    }

    public void setRelId(String relId) {
        this.relId = relId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}