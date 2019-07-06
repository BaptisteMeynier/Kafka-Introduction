package com.meynier.kafka.creator;

import com.meynier.kafka.constants.IKafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

public class KafkaProducerBuilder {

    public static BrokerStep build() {
        return new Builder();
    }

    public interface BrokerStep {
        BrokerHostsStep addBrokers(final String brokers);
        BrokerHostStep addBrokerHost(final String groupId);
    }

    public interface BrokerHostStep {
        InitialStep withPort(final int port);
    }

    public interface BrokerHostsStep {
        TopicStep setClientId(final String client);
    }

    public interface InitialStep {
        BrokerHostStep addBrokerHost(final String host);
    }

    public interface TopicStep {
        OptionalConfigurationStep setTopic(final String topicName);
    }

    public interface OptionalConfigurationStep {

        Producer produce();
    }


    private static class Builder implements BrokerStep, BrokerHostsStep, InitialStep, BrokerHostStep, TopicStep {

        private String BROKER_PATTERN = "%s:%n";

        private String host;


        private void checkArg(String arg, final String msg) {
            if (Objects.nonNull(arg) && arg.isEmpty()) {
                throw new IllegalArgumentException(msg);
            }
            Objects.requireNonNull(arg, msg);
        }


        @Override
        public Producer produce() {
            final Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",",this.kafkaBroker));
            props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupIdConfig);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.autoCommit);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.offset);
            final Consumer consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(this.topicName));
            return consumer;
        }
    }




}
