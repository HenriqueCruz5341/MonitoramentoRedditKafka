package ufes.kafka.adapters;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.comment.CommentDto;
import ufes.kafka.apis.services.CommentService;

public class CommentAdapter {
    private static final Logger logger = LoggerFactory.getLogger(CommentAdapter.class.getName());

    public Response<List<CommentDto>> getComment(String accessToken, String id) {

        CommentService service = ClientRedditOAuthApi.getInstance(accessToken).getCommentService();
        Call<List<CommentDto>> callCommentDto = service.getComment(id);

        try {
            Response<List<CommentDto>> responseCommentDto = callCommentDto.execute();

            if (responseCommentDto.code() == 401) {
                logger.error("Erro ao autenticar: " + responseCommentDto.code());
            } else {
                logger.info("Comment pego com sucesso");
            }

            return responseCommentDto;
        } catch (Exception e) {
            logger.error("Erro ao pegar comment: " + e.getMessage());
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
