package ufes.kafka.dto.messaging;

import java.util.List;

public class DataDto {
    public List<ChildrenDto> children;

    public List<ChildrenDto> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenDto> children) {
        this.children = children;
    }
}
