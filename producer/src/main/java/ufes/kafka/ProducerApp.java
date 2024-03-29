package ufes.kafka;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import ufes.kafka.adapters.AuthAdapter;
import ufes.kafka.adapters.BlockedUsersAdapter;
import ufes.kafka.adapters.CommentAdapter;
import ufes.kafka.adapters.MeAdapter;
import ufes.kafka.adapters.MessagingAdapter;
import ufes.kafka.adapters.OverviewAdapter;
import ufes.kafka.adapters.ProducerAdapter;
import ufes.kafka.adapters.SearchAdapter;
import ufes.kafka.apis.dtos.blocked.BlockedUsersDto;
import ufes.kafka.apis.dtos.comment.CommentDto;
import ufes.kafka.apis.dtos.common.DataPostDto;
import ufes.kafka.apis.dtos.me.MeDto;
import ufes.kafka.apis.dtos.messaging.ChildrenDto;
import ufes.kafka.apis.dtos.post.PostDto;
import ufes.kafka.runnables.BlockedUsersRunnable;
import ufes.kafka.runnables.MeRunnable;
import ufes.kafka.runnables.MessagingRunnable;
import ufes.kafka.runnables.OverviewRunnable;
import ufes.kafka.runnables.PostRunnable;

public class ProducerApp {

    public static void main(String[] args) throws InterruptedException {
        Set<String> usersToOverview = new HashSet<>();
        List<String> queryList = new ArrayList<>();
        queryList.add("atentado terrorista ufes");
        queryList.add("brasil ataque escola");

        AuthAdapter authAdapter = new AuthAdapter();
        MeAdapter meAdapter = new MeAdapter();
        BlockedUsersAdapter blockedUsersAdapter = new BlockedUsersAdapter();
        MessagingAdapter messagingAdapter = new MessagingAdapter();
        SearchAdapter searchAdapter = new SearchAdapter();
        CommentAdapter commentAdapter = new CommentAdapter();
        OverviewAdapter overviewAdapter = new OverviewAdapter();

        ProducerAdapter<MeDto> meProducer = new ProducerAdapter<>();
        ProducerAdapter<BlockedUsersDto> blockedUsersProducer = new ProducerAdapter<>();
        ProducerAdapter<ChildrenDto> messagingProducer = new ProducerAdapter<>();
        ProducerAdapter<PostDto> postProducer = new ProducerAdapter<>();
        ProducerAdapter<DataPostDto> overviewProducer = new ProducerAdapter<>();

        meProducer.start();
        blockedUsersProducer.start();
        messagingProducer.start();
        postProducer.start();
        overviewProducer.start();

        CountDownLatch latch = new CountDownLatch(1);

        MeRunnable meRunnable = new MeRunnable(authAdapter, meAdapter, meProducer, 10000);
        new Thread(meRunnable).start();

        BlockedUsersRunnable blockedUsersRunnable = new BlockedUsersRunnable(authAdapter, blockedUsersAdapter,
                blockedUsersProducer, 20000);
        new Thread(blockedUsersRunnable).start();

        MessagingRunnable messagingRunnable = new MessagingRunnable(authAdapter, messagingAdapter,
                messagingProducer,
                10000);
        new Thread(messagingRunnable).start();

        PostRunnable postRunnable = new PostRunnable(authAdapter, searchAdapter, commentAdapter,
                postProducer, 15000, queryList, usersToOverview);
        new Thread(postRunnable).start();

        OverviewRunnable overviewRunnable = new OverviewRunnable(authAdapter, overviewAdapter, overviewProducer,
                5000,
                usersToOverview);
        new Thread(overviewRunnable).start();

        latch.await();

        meProducer.close();
        blockedUsersProducer.close();
        messagingProducer.close();
        postProducer.close();
        overviewProducer.close();
    }
}
