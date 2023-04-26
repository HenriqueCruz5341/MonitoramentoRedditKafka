package ufes.kafka.helpers;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaJsonSerializer<T> implements Serializer<T> {

    private static final Logger logger = LoggerFactory.getLogger(KafkaJsonSerializer.class.getName());

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, T o) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsBytes(o);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return retVal;
    }

    @Override
    public void close() {

    }
}
