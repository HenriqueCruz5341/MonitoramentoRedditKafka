package ufes.kafka;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.Joined;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.SlidingWindows;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.apache.kafka.streams.kstream.Windowed;

import ufes.kafka.dto.blocked.BlockedUsersDto;
import ufes.kafka.dto.common.DataPostDto;
import ufes.kafka.dto.me.MeDto;
import ufes.kafka.dto.messaging.ChildrenDto;
import ufes.kafka.dto.profile.ProfileDto;
import ufes.kafka.helpers.KafkaJsonDeserializer;
import ufes.kafka.helpers.KafkaJsonSerializer;

public class ConsumerApp {
    public static void main(String[] args) {
        String BootstrapServer = "localhost:9092";

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-processors");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder builder = new StreamsBuilder();

        final Serde<String> stringSerde = Serdes.String();

        // Stream 1:
        // Recebe os dados do tópico "messaging", verifica se dentro de um período de 15
        // segundos, houve 3 ou mais posts com o mesmo parentId, e envia para o tópico
        // "hot-posts" o parentId e a quantidade de mensagens
        final Serde<ChildrenDto> childrenDtoJsonSerde = Serdes.serdeFrom(new KafkaJsonSerializer<ChildrenDto>(),
                new KafkaJsonDeserializer<ChildrenDto>(ChildrenDto.class));
        KStream<String, ChildrenDto> messagingKStream = builder.stream("messaging",
                Consumed.with(stringSerde, childrenDtoJsonSerde));
        KGroupedStream<String, ChildrenDto> groupedStream = messagingKStream
                .groupByKey(Grouped.with(stringSerde, childrenDtoJsonSerde));
        SlidingWindows window = SlidingWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(15), Duration.ofSeconds(1));
        // TimeWindows window = TimeWindows.ofSizeAndGrace(Duration.ofSeconds(15),
        // Duration.ofSeconds(1));
        KTable<Windowed<String>, Long> windowedCounts = groupedStream
                .windowedBy(window).count();
        windowedCounts
                .filter((windowedKey, count) -> count >= 3).toStream()
                .map((windowedKey, count) -> new KeyValue<>(windowedKey.key(), count.toString()))
                .to("hot-posts", Produced.with(stringSerde, stringSerde));
        // =====================================================================================================

        // Stream 2:
        // Recebe os dados do tópico "overview", verifica se o id do post já foi
        // publicado no tópico "overview-filtered", se não foi, publica no tópico
        // "overview-filtered"
        final Serde<DataPostDto> dataPostDtoJsonSerde = Serdes.serdeFrom(new KafkaJsonSerializer<DataPostDto>(),
        new KafkaJsonDeserializer<DataPostDto>(DataPostDto.class));
        KStream<String, DataPostDto> overviewKStream = builder.stream("overview", Consumed.with(stringSerde, dataPostDtoJsonSerde))
                .selectKey((key, value) -> value.getId());
        KTable<String, DataPostDto> filteredTable = builder.table("overview-filtered", Consumed.with(stringSerde, dataPostDtoJsonSerde));
        ValueJoiner<DataPostDto, DataPostDto, DataPostDto> overviewJoiner = (overview, filtered) -> {
            if (filtered == null) {
                overview.setId(null);
            }
            return overview;
        };
        KStream<String, DataPostDto> filteredKStream = overviewKStream
                .leftJoin(filteredTable, overviewJoiner, Joined.with(stringSerde, dataPostDtoJsonSerde, dataPostDtoJsonSerde))
                .filter((key, value) -> value.getId() == null).mapValues((key, value) -> {value.setId(key); return value;})
                .selectKey((key, value) -> value.getId());
        filteredKStream.to("overview-filtered", Produced.with(stringSerde, dataPostDtoJsonSerde));
        // ======================================================================================================

        // Stream 3:
        // Recebe os dados to tópicos "num-subscribers" e "blocked-users", verifica se
        // a key é igual, se for, é criado um evento composto pelos dados dos dois
        // e publica no tópico "profile"
        final Serde<MeDto> meDtoJsonSerde = Serdes.serdeFrom(new KafkaJsonSerializer<MeDto>(),
                new KafkaJsonDeserializer<MeDto>(MeDto.class));
        final Serde<BlockedUsersDto> blockedUsersDtoJsonSerde = Serdes.serdeFrom(
                new KafkaJsonSerializer<BlockedUsersDto>(),
                new KafkaJsonDeserializer<BlockedUsersDto>(BlockedUsersDto.class));
        final Serde<ProfileDto> profileDtoJsonSerde = Serdes.serdeFrom(new KafkaJsonSerializer<ProfileDto>(),
                new KafkaJsonDeserializer<ProfileDto>(ProfileDto.class));
        KStream<String, MeDto> numSubscribersKStream = builder.stream("num-subscribers",
                Consumed.with(stringSerde, meDtoJsonSerde));
        KStream<String, BlockedUsersDto> blockedUsersKStream = builder.stream("blocked-users",
                Consumed.with(stringSerde, blockedUsersDtoJsonSerde));
        ValueJoiner<MeDto, BlockedUsersDto, ProfileDto> valueJoiner = (numSubscribers, blockedUsers) -> {
            ProfileDto profile = new ProfileDto();
            profile.setBlockedUsers(blockedUsers.getData().getChildren());
            profile.setNumSubscribers(numSubscribers.getUserInfo().getSubscribers());
            return profile;
        };
        KStream<String, ProfileDto> profileKStream = numSubscribersKStream.join(blockedUsersKStream, valueJoiner,
                JoinWindows.ofTimeDifferenceAndGrace(Duration.ofSeconds(30), Duration.ofSeconds(1)),
                StreamJoined.with(stringSerde, meDtoJsonSerde, blockedUsersDtoJsonSerde));
        profileKStream.to("profile", Produced.with(stringSerde, profileDtoJsonSerde));
        // ======================================================================================================

        // Stream 4:
        // Recebe os dados do tópico "posts", verifica se o post tem mais de 3
        // comentários, se tiver, publica no tópico "danger-posts"

        // ======================================================================================================

        KafkaStreams streams = new KafkaStreams(builder.build(), config);

        streams.setUncaughtExceptionHandler(ex -> {
            System.out.println(
                    "Kafka-Streams uncaught exception occurred. Stream will be replaced with new thread"
                            + ex);
            return StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD;
        });

        // only do this in dev - not in prod
        streams.cleanUp();
        streams.start();
        // print the topology
        // streams.localThreadsMetadata().forEach(data -> System.out.println(data));
        streams.metadataForLocalThreads().forEach(data -> System.out.println(data));

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

// para mostrar as saídas no console
/*
 * kafka-console-consumer --topic example --bootstrap-server broker:9092 \
 * --from-beginning \
 * --property print.key=true \
 * --property key.separator=" : " \
 * --key-deserializer "org.apache.kafka.common.serialization.LongDeserializer" \
 * --value-deserializer
 * "org.apache.kafka.common.serialization.DoubleDeserializer"
 *
 *
 */
