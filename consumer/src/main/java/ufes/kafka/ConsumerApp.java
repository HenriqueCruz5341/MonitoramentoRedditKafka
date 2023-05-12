package ufes.kafka;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import ufes.kafka.dto.messaging.ChildrenDto;
import ufes.kafka.helpers.KafkaJsonDeserializer;
import ufes.kafka.producers.HotPostsProducer;

public class ConsumerApp {
    public static void main(String[] args) {
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

        KafkaConsumer<String, String> consumerJson = new KafkaConsumer<>(props);
        Consumer<String, ChildrenDto> consumerMessages = new KafkaConsumer<String, ChildrenDto>(props2,
                new StringDeserializer(), new KafkaJsonDeserializer<ChildrenDto>(ChildrenDto.class));

        consumerJson.subscribe(Arrays.asList("num-subscribers", "blocked-users", "posts", "overview"));
        consumerMessages.subscribe(Arrays.asList("messaging"));

        Duration timeout = Duration.ofMillis(200);

        while (true) {
            ConsumerRecords<String, String> recordsJson = consumerJson.poll(timeout);
            for (ConsumerRecord<String, String> record : recordsJson) {
                System.out.printf(" --> topic = %s, offset = %d, key = %s, value = %s%n", record.topic(),
                        record.offset(), record.key(), record.value());
            }

            ConsumerRecords<String, ChildrenDto> records = consumerMessages.poll(timeout);
            for (ConsumerRecord<String, ChildrenDto> record : records) {
                System.out.printf(" --> topic = %s, offset = %d, key = %s, value = %s%n", record.topic(),
                        record.offset(), record.key(), record.value().toString());

                ChildrenDto value = record.value();
                String parentId = value.getData().getParentId();

                if (actualTimestamps.containsKey(parentId) == false) {
                    actualTimestamps.put(parentId, longToLocalDateTime(record.timestamp()));
                    hotPostCounts.put(parentId, 1);
                } else {
                    LocalDateTime actualTimestamp = actualTimestamps.get(parentId);
                    LocalDateTime recordTimestamp = longToLocalDateTime(record.timestamp());
                    Integer hotPostCount = hotPostCounts.get(parentId);

                    if (actualTimestamp.plusMinutes(1).isAfter(recordTimestamp)) {
                        hotPostCounts.put(parentId, hotPostCount + 1);
                    } else {
                        actualTimestamps.remove(parentId);
                        continue;
                    }

                    if (hotPostCount + 1 == 3) {
                        hotPostsProducer.publish(value);
                        actualTimestamps.remove(parentId);
                    }
                }
            }
        }

    }

    public static LocalDateTime longToLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault());
    }
}
