package ufes.kafka.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.search.SearchDto;
import ufes.kafka.apis.services.SearchService;

public class SearchAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SearchAdapter.class.getName());

    public Response<SearchDto> search(String accessToken, String query) {

        SearchService service = ClientRedditOAuthApi.getInstance(accessToken).getSearchService();
        Call<SearchDto> callSearchDto = service.search(query);

        try {
            Response<SearchDto> responseSearchDto = callSearchDto.execute();

            if (responseSearchDto.code() == 401) {
                logger.error("Erro ao autenticar: " + responseSearchDto.code());
            } else {
                logger.info("Search feito com sucesso");
            }

            return responseSearchDto;
        } catch (Exception e) {
            logger.error("Erro ao search: " + e.getMessage());
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
