package ufes.kafka.apis.dtos.post;

import java.util.List;

import ufes.kafka.apis.dtos.comment.CommentDto;
import ufes.kafka.apis.dtos.common.DataDto;

public class PostDto {
    public DataDto post;

    public List<DataDto> comments;

    public PostDto(List<CommentDto> comments) {
        this.post = comments.get(0).getData();
        comments.remove(0);
        this.comments = comments.stream().map(CommentDto::getData).collect(java.util.stream.Collectors.toList());
    }

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
