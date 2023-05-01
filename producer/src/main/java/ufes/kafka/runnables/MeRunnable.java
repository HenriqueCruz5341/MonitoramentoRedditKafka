package ufes.kafka.runnables;

import retrofit2.Response;
import ufes.kafka.adapters.AuthAdapter;
import ufes.kafka.adapters.MeAdapter;
import ufes.kafka.adapters.ProducerAdapter;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.dtos.me.MeDto;

public class MeRunnable implements Runnable {

    private AuthAdapter authAdapter;
    private MeAdapter meAdapter;
    private ProducerAdapter<MeDto> meProducer;
    private long sleepTime;

    public MeRunnable(AuthAdapter authAdapter, MeAdapter meAdapter, ProducerAdapter<MeDto> meProducer, long sleepTime) {
        this.authAdapter = authAdapter;
        this.meAdapter = meAdapter;
        this.meProducer = meProducer;
        this.sleepTime = sleepTime;
    }

    public void run() {
        Response<AuthDto> responseAuth = authAdapter.doAuth();

        if (!responseAuth.isSuccessful()) {
            return;
        }
        AuthDto authDto = responseAuth.body();

        while (true) {
            this.sleep();

            Response<MeDto> responseMe = meAdapter.getMe(authDto.getAccessToken());
            if (responseMe.isSuccessful()) {
                MeDto meDto = responseMe.body();

                meProducer.send("num-subscribers", authDto.getUsername(), meDto);
                meProducer.flush();
            } else if (responseMe.code() == 401) {
                responseAuth = authAdapter.doAuth();
                if (!responseAuth.isSuccessful()) {
                    continue;
                }
                authDto = responseAuth.body();
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
