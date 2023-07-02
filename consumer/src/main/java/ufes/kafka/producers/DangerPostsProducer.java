package ufes.kafka.producers;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ufes.kafka.dto.dangerPost.DangerPost;
import ufes.kafka.dto.messaging.ChildrenDto;
import ufes.kafka.helpers.KafkaJsonSerializer;

public class DangerPostsProducer {
    private static final Logger logger = LoggerFactory.getLogger(DangerPostsProducer.class.getName());

    private Producer<String, DangerPost> producer;

    public DangerPostsProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.Serializer");

        producer = new KafkaProducer<String, DangerPost>(props, new StringSerializer(),
                new KafkaJsonSerializer<DangerPost>());
    }

    public void publish(ChildrenDto childrenDto) {
        String topicName = "danger-posts";

        DangerPost dangerPost = new DangerPost();
        dangerPost.setId(childrenDto.getData().getParentId());

        producer.send(new ProducerRecord<String, DangerPost>(topicName, dangerPost.getId(), dangerPost));
        producer.flush();
        logger.info("Danger post published successfully.");
    }

    public void close() {
        producer.close();
    }
}
