package ufes.kafka.adapters;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.ProducerApp;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.comment.CommentDto;
import ufes.kafka.apis.services.CommentService;

public class CommentAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ProducerApp.class.getName());

    public Optional<List<CommentDto>> getComment(String accessToken) {

        CommentService service = ClientRedditOAuthApi.getInstance(accessToken).getCommentService();
        Call<List<CommentDto>> callCommentDto = service.getComment("12hiwcb");

        try {
            Response<List<CommentDto>> responseCommentDto = callCommentDto.execute();

            if (responseCommentDto.code() != 200 || !responseCommentDto.isSuccessful()) {
                logger.error("Erro ao autenticar: " + responseCommentDto.code());
                return Optional.empty();
            }

            List<CommentDto> commentListDto = responseCommentDto.body();
            logger.info("Comment pegado com sucesso");

            return Optional.of(commentListDto);
        } catch (Exception e) {
            logger.error("Erro ao autenticar: " + e.getMessage());
            return Optional.empty();
        }
    }
}
