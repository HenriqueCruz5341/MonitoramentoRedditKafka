package ufes.kafka;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ufes.kafka.adapters.AuthAdapter;
import ufes.kafka.adapters.CommentAdapter;
import ufes.kafka.adapters.MeAdapter;
import ufes.kafka.adapters.ProducerAdapter;
import ufes.kafka.apis.dtos.auth.AuthDto;
import ufes.kafka.apis.dtos.comment.CommentDto;
import ufes.kafka.apis.dtos.me.MeDto;

public class ProducerApp {

    private static final Logger logger = LoggerFactory.getLogger(ProducerApp.class.getName());

    public static void main(String[] args) {

        Optional<AuthDto> authDtoOpt = authenticate();
        if (authDtoOpt.isEmpty()) {
            return;
        }

        AuthDto authDto = authDtoOpt.get();

        // ProducerAdapter<AuthDto> producer = new ProducerAdapter<>();
        // producer.start();

        // for (int i = 0; i < 10; i++) {
        // producer.send("my-topic", Integer.toString(i), authDto);
        // producer.flush();
        // }

        // producer.close();

        // ProducerAdapter<BlockedUsersDto> producer2 = new ProducerAdapter<>();
        // producer2.start();

        // BlockedUsersAdapter blockedUsersAdapter = new BlockedUsersAdapter();
        // Optional<BlockedUsersDto> blockedUsersDtoOpt =
        // blockedUsersAdapter.getBlockedUsers(authDto.getAccessToken());

        // if (blockedUsersDtoOpt.isEmpty()) {
        // return;
        // }

        // BlockedUsersDto blockedUsersDto = blockedUsersDtoOpt.get();

        // producer2.send("my-topic",
        // blockedUsersDto.getData().getChildren().get(0).getName(), blockedUsersDto);
        // producer2.flush();

        // producer2.close();

        // ProducerAdapter<MeDto> producer2 = new ProducerAdapter<>();
        // producer2.start();

        // MeAdapter meAdapter = new MeAdapter();
        // Optional<MeDto> meDtoOpt = meAdapter.getMe(authDto.getAccessToken());

        // if (meDtoOpt.isEmpty()) {
        // return;
        // }

        // MeDto meDto = meDtoOpt.get();

        // producer2.send("my-topic", "123", meDto);
        // producer2.flush();

        // producer2.close();

        ProducerAdapter<List<CommentDto>> producer2 = new ProducerAdapter<>();
        producer2.start();

        CommentAdapter commentAdapter = new CommentAdapter();
        Optional<List<CommentDto>> commentDtoOpt = commentAdapter.getComment(authDto.getAccessToken());

        if (commentDtoOpt.isEmpty()) {
            return;
        }

        List<CommentDto> commentDtos = commentDtoOpt.get();

        producer2.send("my-topic", "123", commentDtos);
        producer2.flush();

        producer2.close();

    }

    private static Optional<AuthDto> authenticate() {
        AuthAdapter authAdapter = new AuthAdapter();
        return authAdapter.doAuth();
    }

}
