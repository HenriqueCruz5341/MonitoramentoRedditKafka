package ufes.kafka.runnables;

import retrofit2.Response;
import ufes.kafka.adapters.AuthAdapter;
import ufes.kafka.adapters.BlockedUsersAdapter;
import ufes.kafka.adapters.ProducerAdapter;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.dtos.blocked.BlockedUsersDto;

public class BlockedUsersRunnable implements Runnable {

    private AuthAdapter authAdapter;
    private BlockedUsersAdapter blockedUsersAdapter;
    private ProducerAdapter<BlockedUsersDto> meProducer;
    private long sleepTime;

    public BlockedUsersRunnable(AuthAdapter authAdapter, BlockedUsersAdapter blockedUsersAdapter,
            ProducerAdapter<BlockedUsersDto> meProducer, long sleepTime) {
        this.authAdapter = authAdapter;
        this.blockedUsersAdapter = blockedUsersAdapter;
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

            Response<BlockedUsersDto> responseBlockedUsers = blockedUsersAdapter
                    .getBlockedUsers(authDto.getAccessToken());
            if (responseBlockedUsers.isSuccessful()) {
                BlockedUsersDto meDto = responseBlockedUsers.body();

                meProducer.send("blocked-users", authDto.getUsername(), meDto);
                meProducer.flush();
            } else if (responseBlockedUsers.code() == 401) {
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
