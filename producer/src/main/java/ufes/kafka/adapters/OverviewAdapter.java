package ufes.kafka.adapters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ufes.kafka.apis.clients.ClientRedditOAuthApi;
import ufes.kafka.apis.dtos.overview.OverviewDto;
import ufes.kafka.apis.services.OverviewService;

public class OverviewAdapter {
    private static final Logger logger = LoggerFactory.getLogger(OverviewAdapter.class.getName());

    public Response<OverviewDto> getOverview(String accessToken, String username) {

        OverviewService service = ClientRedditOAuthApi.getInstance(accessToken).getOverviewService();
        Call<OverviewDto> callOverviewDto = service.getOverview(username);

        try {
            Response<OverviewDto> responseOverviewDto = callOverviewDto.execute();

            if (responseOverviewDto.code() == 401) {
                logger.error("Erro ao autenticar: " + responseOverviewDto.code());
            } else {
                logger.info("Overview pego com sucesso");
            }

            return responseOverviewDto;
        } catch (Exception e) {
            logger.error("Erro ao pegar overview: " + e.getMessage());
            return Response.error(500, ResponseBody.create(null, e.getMessage()));
        }
    }
}
