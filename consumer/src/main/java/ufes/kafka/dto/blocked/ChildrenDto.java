package ufes.kafka.dto.blocked;

public class ChildrenDto {
    public float date;
    public String relId;
    public String name;
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

    @Override
    public String toString() {
        return "ChildrenDto [date=" + date + ", relId=" + relId + ", name=" + name + ", id=" + id + "]";
    }
}