package ufes.kafka.runnables;

import java.util.List;
import java.util.Set;

import retrofit2.Response;
import ufes.kafka.adapters.AuthAdapter;
import ufes.kafka.adapters.CommentAdapter;
import ufes.kafka.adapters.ProducerAdapter;
import ufes.kafka.adapters.SearchAdapter;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.dtos.comment.CommentDto;
import ufes.kafka.apis.dtos.common.ChildrenDto;
import ufes.kafka.apis.dtos.post.PostDto;
import ufes.kafka.apis.dtos.search.SearchDto;

public class PostRunnable implements Runnable {

    private AuthAdapter authAdapter;
    private SearchAdapter searchAdapter;
    private CommentAdapter commentAdapter;
    private ProducerAdapter<PostDto> postProducer;
    private long sleepTime;
    private List<String> queryList;
    private Set<String> usersToOverview;

    public PostRunnable(AuthAdapter authAdapter, SearchAdapter searchAdapter, CommentAdapter commentAdapter,
            ProducerAdapter<PostDto> postProducer,
            long sleepTime, List<String> queryList, Set<String> usersToOverview) {
        this.authAdapter = authAdapter;
        this.searchAdapter = searchAdapter;
        this.commentAdapter = commentAdapter;
        this.postProducer = postProducer;
        this.sleepTime = sleepTime;
        this.queryList = queryList;
        this.usersToOverview = usersToOverview;
    }

    public void run() {
        Response<AuthDto> responseAuth = this.authAdapter.doAuth();

        if (!responseAuth.isSuccessful()) {
            return;
        }
        AuthDto authDto = responseAuth.body();

        while (true) {
            this.sleep();

            for (String query : this.queryList) {
                Response<SearchDto> responseSearch = this.searchAdapter.search(authDto.getAccessToken(), query);

                if (!responseSearch.isSuccessful()) {
                    if (responseSearch.code() == 401) {
                        responseAuth = this.authAdapter.doAuth();
                        if (!responseAuth.isSuccessful()) {
                            continue;
                        }
                        authDto = responseAuth.body();
                    }
                    continue;
                }

                SearchDto searchDto = responseSearch.body();
                List<ChildrenDto> topThree = searchDto.getData().getChildren().subList(0, 3);

                // Envio de mensagens para o tópico posts das 3 primeiras threads
                for (ChildrenDto childrenDto : topThree) {
                    synchronized (this.usersToOverview) {
                        this.usersToOverview.add(childrenDto.getDataPost().getAuthor());
                    }

                    Response<List<CommentDto>> responseComment = this.commentAdapter.getComment(
                            authDto.getAccessToken(),
                            childrenDto.getDataPost().getId());

                    if (!responseComment.isSuccessful()) {
                        if (responseComment.code() == 401) {
                            responseAuth = this.authAdapter.doAuth();
                            if (!responseAuth.isSuccessful()) {
                                continue;
                            }
                            authDto = responseAuth.body();
                        }
                        continue;
                    }

                    List<CommentDto> commentDtoList = responseComment.body();
                    PostDto postDto = new PostDto(commentDtoList);

                    this.postProducer.send("posts", childrenDto.getDataPost().getId(), postDto);
                    this.postProducer.flush();
                }
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(this.sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
