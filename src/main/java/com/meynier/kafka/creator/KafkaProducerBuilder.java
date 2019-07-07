package com.meynier.kafka.creator;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

public class KafkaProducerBuilder {

    public static BrokerStep build() {
        return new Builder();
    }

    public interface BrokerStep {
        ClientIdStep addBrokers(final String brokers);
    }

    public interface PortStep {
        ClientIdStep withPort(final int port);
    }

    public interface ClientIdStep {
        PortStep addBrokerHost(final String host);
        TopicStep setClientId(final String client);
    }

    public interface TopicStep {
        OptionalConfigurationStep setTopic(final String topicName);
    }

    public interface OptionalConfigurationStep {
        OptionalConfigurationStep setOptionalParam(final String key,final String value);
        Producer produce();
    }

    private static class Builder implements BrokerStep, ClientIdStep, PortStep, TopicStep, OptionalConfigurationStep {

        private final static String BROKER_PATTERN = "%s:%n";
        private String brokers;
        private String clientId;
        private String topic;
        private String host;
        private final List<String> kafkaBroker = new ArrayList<>();
        private Properties properties = new Properties();

        private void checkArg(String arg, final String msg) {
            if (Objects.nonNull(arg) && arg.isEmpty()) {
                throw new IllegalArgumentException(msg);
            }
            Objects.requireNonNull(arg, msg);
        }

        @Override
        public ClientIdStep addBrokers(String brokers) {
            checkArg(brokers, "Broker host cannot be null or empty");
            this.brokers=brokers;
            return this;
        }

        @Override
        public PortStep addBrokerHost(String host) {
            this.host=host;
            return this;
        }

        @Override
        public TopicStep setClientId(String clientId) {
            checkArg(clientId, "clientId cannot be null or empty");
            this.clientId=clientId;
            return this;
        }

        @Override
        public OptionalConfigurationStep setTopic(String topicName) {
            checkArg(topicName, "topicName cannot be null or empty");
            this.topic=topicName;
            return this;
        }

        @Override
        public OptionalConfigurationStep setOptionalParam(String key, String value) {
            if (properties.containsKey(key)) {
                throw new IllegalArgumentException("Key was already recorded");
            }
            this.properties.put(key,value);
            return this;
        }

        @Override
        public ClientIdStep withPort(int port) {
            kafkaBroker.add(String.format(BROKER_PATTERN,this.host,port));
            return this;
        }

        @Override
        public Producer produce() {
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.brokers);
            properties.put(ProducerConfig.CLIENT_ID_CONFIG, this.clientId);
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            //props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());

            return new KafkaProducer<>(properties);
        }


    }




}
