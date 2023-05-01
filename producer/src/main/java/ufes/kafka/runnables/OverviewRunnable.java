package ufes.kafka.runnables;

import java.util.Set;

import retrofit2.Response;
import ufes.kafka.adapters.AuthAdapter;
import ufes.kafka.adapters.OverviewAdapter;
import ufes.kafka.adapters.ProducerAdapter;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.dtos.common.ChildrenDto;
import ufes.kafka.apis.dtos.common.DataPostDto;
import ufes.kafka.apis.dtos.overview.OverviewDto;

public class OverviewRunnable implements Runnable {

    private AuthAdapter authAdapter;
    private OverviewAdapter overviewAdapter;
    private ProducerAdapter<DataPostDto> overviewProducer;
    private long sleepTime;
    private Set<String> usersToOverview;

    public OverviewRunnable(AuthAdapter authAdapter, OverviewAdapter overviewAdapter,
            ProducerAdapter<DataPostDto> overviewProducer,
            long sleepTime, Set<String> usersToOverview) {
        this.authAdapter = authAdapter;
        this.overviewAdapter = overviewAdapter;
        this.overviewProducer = overviewProducer;
        this.sleepTime = sleepTime;
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

            synchronized (this.usersToOverview) {

                for (String user : this.usersToOverview) {
                    Response<OverviewDto> responseOverview = overviewAdapter.getOverview(authDto.getAccessToken(),
                            user);

                    if (!responseOverview.isSuccessful()) {
                        if (responseOverview.code() == 401) {
                            responseAuth = this.authAdapter.doAuth();
                            if (!responseAuth.isSuccessful()) {
                                continue;
                            }
                            authDto = responseAuth.body();
                        }
                        continue;
                    }

                    OverviewDto overviewDto = responseOverview.body();
                    ChildrenDto first = overviewDto.getData().getChildren().get(0);

                    overviewProducer.send("overview", user, first.getDataPost());
                    overviewProducer.flush();
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
