package ufes.kafka.dto.post;

import java.util.List;

import ufes.kafka.dto.common.DataDto;

public class PostDto {
    public DataDto post;

    public List<DataDto> comments;

    public List<DataDto> getComments() {
        return comments;
    }

    public void setComments(List<DataDto> comments) {
        this.comments = comments;
    }

    public DataDto getPost() {
        return post;
    }

    public void setPost(DataDto post) {
        this.post = post;
    }
}
