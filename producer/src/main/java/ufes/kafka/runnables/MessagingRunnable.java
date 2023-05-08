package ufes.kafka.runnables;

import retrofit2.Response;
import ufes.kafka.adapters.AuthAdapter;
import ufes.kafka.adapters.MessagingAdapter;
import ufes.kafka.adapters.ProducerAdapter;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.dtos.messaging.MessagingDto;

public class MessagingRunnable implements Runnable {

    private AuthAdapter authAdapter;
    private MessagingAdapter messagingAdapter;
    private ProducerAdapter<MessagingDto> messagingProducer;
    private long sleepTime;

    public MessagingRunnable(AuthAdapter authAdapter, MessagingAdapter messagingAdapter,
            ProducerAdapter<MessagingDto> messagingProducer, long sleepTime) {
        this.authAdapter = authAdapter;
        this.messagingAdapter = messagingAdapter;
        this.messagingProducer = messagingProducer;
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

            Response<MessagingDto> responseMessaging = messagingAdapter
                    .getMessaging(authDto.getAccessToken());
            if (responseMessaging.isSuccessful()) {
                MessagingDto meDto = responseMessaging.body();

                messagingProducer.send("messaging", authDto.getUsername(), meDto);
                messagingProducer.flush();
            } else if (responseMessaging.code() == 401) {
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
