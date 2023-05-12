package ufes.kafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ufes.kafka.dto.hotPost.HotPost;
import ufes.kafka.dto.messaging.ChildrenDto;
import ufes.kafka.helpers.KafkaJsonSerializer;

public class HotPostsProducer {
    private static final Logger logger = LoggerFactory.getLogger(HotPostsProducer.class.getName());

    private Producer<String, HotPost> producer;

    public HotPostsProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serializer");

        producer = new KafkaProducer<String, HotPost>(props, new StringSerializer(),
                new KafkaJsonSerializer<HotPost>());
    }

    public void publish(ChildrenDto childrenDto) {
        String topicName = "hot-posts";

        HotPost hotPost = new HotPost();
        hotPost.setId(childrenDto.getData().getParentId());

        producer.send(new ProducerRecord<String, HotPost>(topicName, hotPost.getId(), hotPost));
        producer.flush();
        logger.info("Hot post published successfully.");
    }

    public void close() {
        producer.close();
    }
}
