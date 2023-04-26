package ufes.kafka.helpers;

import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesLoader {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class.getName());
    private Properties properties;

    private Optional<Properties> loadProperties() {
        Properties configuration = new Properties();

        try {
            InputStream inputStream = PropertiesLoader.class
                    .getClassLoader()
                    .getResourceAsStream("application.properties");
            configuration.load(inputStream);
            inputStream.close();

            return Optional.of(configuration);
        } catch (Exception e) {
            logger.error("Erro ao carregar o arquivo de configurações", e);
            return Optional.empty();
        }
    }

    public Optional<String> getProperty(String key) {
        if (properties == null) {
            Optional<Properties> propertiesOpt = loadProperties();

            if (propertiesOpt.isPresent()) {
                properties = propertiesOpt.get();
            } else {
                return Optional.empty();
            }
        }

        String value = properties.getProperty(key);

        if (value == null) {
            logger.error("Propriedade não encontrada: " + key);
            return Optional.empty();
        }

        return Optional.of(value);
    }
}