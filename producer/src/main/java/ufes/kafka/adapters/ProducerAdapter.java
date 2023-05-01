package ufes.kafka.adapters;

import java.util.Optional;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ufes.kafka.helpers.KafkaJsonSerializer;
import ufes.kafka.helpers.PropertiesLoader;

public class ProducerAdapter<V> {

    private static final Logger logger = LoggerFactory.getLogger(ProducerAdapter.class.getName());
    Producer<String, V> producer;

    public ProducerAdapter() {
    }

    public void start() {
        Optional<Properties> propertiesOpt = bindProperties();
        if (propertiesOpt.isEmpty()) {
            return;
        }
        producer = new KafkaProducer<>(propertiesOpt.get(), new StringSerializer(), new KafkaJsonSerializer<V>());
    }

    public void flush() {
        if (producer == null) {
            logger.error("Producer não iniciado");
            return;
        }
        producer.flush();
    }

    public void send(String topic, String key, V message) {
        if (producer == null) {
            logger.error("Producer não iniciado");
            return;
        }

        logger.info("Enviando mensagem: " + message + ", para o tópico: " + topic + ", com a chave: " + key);
        producer.send((new ProducerRecord<>(topic, key, message)));
    }

    public void close() {
        if (producer == null) {
            logger.error("Producer não iniciado");
            return;
        }
        producer.close();
    }

    private Optional<Properties> bindProperties() {
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        Optional<String> bootstrapServersOpt = propertiesLoader.getProperty("kafka.bootstrap.servers");
        Optional<String> keySerializerOpt = propertiesLoader.getProperty("kafka.key.serializer");
        Optional<String> valueSerializerOpt = propertiesLoader.getProperty("kafka.value.serializer");

        if (bootstrapServersOpt.isEmpty() || keySerializerOpt.isEmpty() || valueSerializerOpt.isEmpty()) {
            return Optional.empty();
        }

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServersOpt.get());
        // props.put("acks", "all");
        // props.put("retries", 0);
        // props.put("batch.size", 16384);
        // props.put("linger.ms", 1);
        // props.put("buffer.memory", 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerOpt.get());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerOpt.get());

        return Optional.of(props);
    }

}
