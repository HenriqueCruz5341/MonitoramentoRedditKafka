package ufes.kafka;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;

import ufes.kafka.dto.messaging.ChildrenDto;
import ufes.kafka.dto.post.PostDto;
import ufes.kafka.helpers.KafkaJsonDeserializer;
import ufes.kafka.helpers.KafkaJsonSerializer;
import ufes.kafka.producers.HotPostsProducer;

public class ConsumerApp {
    private static final int List = 0;

    public static void main(String[] args) throws InterruptedException {
        Map<String, LocalDateTime> actualTimestamps = new HashMap<>();
        Map<String, Integer> hotPostCounts = new HashMap<>();
        HotPostsProducer hotPostsProducer = new HotPostsProducer();

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Properties props2 = new Properties();
        props2.put("bootstrap.servers", "localhost:9092");
        props2.put("group.id", "test2");
        props2.put("enable.auto.commit", "true");
        props2.put("auto.commit.interval.ms", "1000");
        props2.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props2.put("value.deserializer", "org.apache.kafka.common.serialization.Deserializer");

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "continuous-kafka-streams-app");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Criação da topologia do Kafka Streams
        // StreamsBuilder builder = new StreamsBuilder();
        // builder.stream("posts", Consumed.with(Serdes.String(), Serdes.String()))
        // .foreach((key, value) -> System.out.println("Received message: key=" + key +
        // ", value=" + value));

        // // Criação e inicialização do Kafka Streams
        // KafkaStreams streams = new KafkaStreams(builder.build(), config);
        CountDownLatch latch = new CountDownLatch(1);

        final Serde<String> stringSerde = Serdes.String();
        final Serde<PostDto> jsonSerde = Serdes.serdeFrom(new KafkaJsonSerializer<PostDto>(),
                new KafkaJsonDeserializer<PostDto>(PostDto.class));

        StreamsBuilder builderJson = new StreamsBuilder();
        KStream<String, PostDto> postsKStream = builderJson.stream("posts",
                Consumed.with(stringSerde, jsonSerde));

        postsKStream.foreach((key, value) -> System.out.println("Received message: key=" + key +
                ", value=" + value));

        KafkaStreams streams = new KafkaStreams(builderJson.build(), config);

        // Shutdown hook para encerrar o Kafka Streams corretamente ao encerrar a
        // aplicação
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await(); // Espera indefinidamente até que a aplicação seja encerrada
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);

        // KafkaConsumer<String, String> consumerJson = new KafkaConsumer<>(props);
        // Consumer<String, ChildrenDto> consumerMessages = new KafkaConsumer<String,
        // ChildrenDto>(props2,
        // new StringDeserializer(), new
        // KafkaJsonDeserializer<ChildrenDto>(ChildrenDto.class));

        // consumerJson.subscribe(Arrays.asList("num-subscribers", "blocked-users",
        // "posts", "overview"));
        // consumerMessages.subscribe(Arrays.asList("messaging"));

        // Duration timeout = Duration.ofMillis(200);

        // CountDownLatch latch = new CountDownLatch(1);

        // Properties propsStream = new Properties();
        // propsStream.put(StreamsConfig.APPLICATION_ID_CONFIG,
        // "wordcount-application");
        // propsStream.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        // propsStream.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
        // Serdes.String().getClass());
        // propsStream.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
        // Serdes.String().getClass());

        // final Serde<String> stringSerde = Serdes.String();
        // final Serde<PostDto> jsonSerde = Serdes.serdeFrom(new
        // KafkaJsonSerializer<PostDto>(),
        // new KafkaJsonDeserializer<PostDto>(PostDto.class));

        // StreamsBuilder builder = new StreamsBuilder();
        // KStream<String, PostDto> postsKStream = builder.stream("posts",
        // Consumed.with(stringSerde,
        // jsonSerde));

        // postsKStream.peek((key, value) -> {
        // System.out.println(" --> key = " + key + ", value = " + value.toString());
        // });

        // KafkaStreams streams = new KafkaStreams(builder.build(), propsStream);
        // streams.start();

        // Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

        // latch.await();

        // while (true) {

        // ConsumerRecords<String, String> recordsJson = consumerJson.poll(timeout);
        // for (ConsumerRecord<String, String> record : recordsJson) {
        // System.out.printf(" --> topic = %s, offset = %d, key = %s, value = %s%n",
        // record.topic(),
        // record.offset(), record.key(), record.value());
        // }

        // ConsumerRecords<String, ChildrenDto> records =
        // consumerMessages.poll(timeout);
        // for (ConsumerRecord<String, ChildrenDto> record : records) {
        // System.out.printf(" --> topic = %s, offset = %d, key = %s, value = %s%n",
        // record.topic(),
        // record.offset(), record.key(), record.value().toString());

        // ChildrenDto value = record.value();
        // String parentId = value.getData().getParentId();

        // if (actualTimestamps.containsKey(parentId) == false) {
        // actualTimestamps.put(parentId, longToLocalDateTime(record.timestamp()));
        // hotPostCounts.put(parentId, 1);
        // } else {
        // LocalDateTime actualTimestamp = actualTimestamps.get(parentId);
        // LocalDateTime recordTimestamp = longToLocalDateTime(record.timestamp());
        // Integer hotPostCount = hotPostCounts.get(parentId);

        // if (actualTimestamp.plusMinutes(1).isAfter(recordTimestamp)) {
        // hotPostCounts.put(parentId, hotPostCount + 1);
        // } else {
        // actualTimestamps.remove(parentId);
        // continue;
        // }

        // if (hotPostCount + 1 == 3) {
        // hotPostsProducer.publish(value);
        // actualTimestamps.remove(parentId);
        // }
        // }
        // }
        // }

    }

    public static LocalDateTime longToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault());
    }
}
