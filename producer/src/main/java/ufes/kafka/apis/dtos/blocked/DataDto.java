package ufes.kafka.apis.dtos.blocked;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class DataDto {
    @SerializedName("children")
    public List<ChildrenDto> children;

    public List<ChildrenDto> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenDto> children) {
        this.children = children;
    }
}
