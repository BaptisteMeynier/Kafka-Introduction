package com.meynier.kafka.creator;

import com.meynier.kafka.constants.IKafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;

public class KafkaConsumerBuilder {

    private KafkaConsumerBuilder(Builder builder) {

    }

    public static BrokerHostStep withBrokerHost(final String host) {
        return new Builder();
    }

    public interface InitialStep {
        BrokerHostStep addBrokerHost(final String host);

        GroupIdStep setGroupId(final String groupId);
    }

    public interface BrokerHostStep {
        InitialStep withBrokerPort(final int port);
    }

    public interface GroupIdStep {
        KeyDeserializerStep setTopic(final String topicName);
    }

    public interface KeyDeserializerStep {
        ValueSerializerStep setKeyDeserializer(final Class<? extends Deserializer> deserializer);
    }

    public interface ValueSerializerStep {
        OffsetStep setValueDeserializer(final Class<? extends Deserializer> deserializer);
    }

    public interface OffsetStep {
        OptionalConfigurationStep fromEarlier();

        OptionalConfigurationStep fromLatest();
    }

    public interface OptionalConfigurationStep {
        OptionalConfigurationStep setMaxPollRecords(int maxPollRecords);

        OptionalConfigurationStep enableAutoCommit();

        OptionalConfigurationStep disableAutocommit();

        Consumer<Long, String> subscribe();
    }


    private static class Builder<T,U> implements InitialStep, BrokerHostStep, GroupIdStep, KeyDeserializerStep, ValueSerializerStep, OffsetStep, OptionalConfigurationStep {

        private String BROKER_PATTERN = "%s:%n";

        private String host;

        private int port;

        private List<String> kafkaBroker = new ArrayList<>();

        private Integer messageFoundCount = IKafkaConstants.MESSAGE_COUNT;

        private String clientId = IKafkaConstants.CLIENT_ID;

        private String topicName = IKafkaConstants.TOPIC_NAME;

        private String groupIdConfig = IKafkaConstants.GROUP_ID_CONFIG;

        private String offset;

        private Integer maxPollRecords = IKafkaConstants.MAX_POLL_RECORDS;

        private String keyDeserializer;

        private String valueDeserializer;

        private boolean autoCommit = true;


        private void checkArg(String arg, final String msg) {
            if (Objects.nonNull(arg) && arg.isEmpty()) {
                throw new IllegalArgumentException(msg);
            }
            Objects.requireNonNull(arg, msg);
        }

        @Override
        public OptionalConfigurationStep setMaxPollRecords(int maxPollRecords) {
            this.maxPollRecords = maxPollRecords;
            return this;
        }

        @Override
        public OptionalConfigurationStep enableAutoCommit() {
            this.autoCommit = true;
            return this;
        }

        @Override
        public OptionalConfigurationStep disableAutocommit() {
            this.autoCommit = false;
            return this;
        }

        @Override
        public BrokerHostStep addBrokerHost(String host) {
            checkArg(host, "Broker host cannot be null or empty");
            this.host = host;
            return this;
        }

        @Override
        public GroupIdStep setGroupId(String groupId) {
            this.groupIdConfig = groupId;
            return this;
        }

        @Override
        public InitialStep withBrokerPort(int port) {
            this.kafkaBroker.add(String.format(BROKER_PATTERN, host, port));
            return this;
        }

        @Override
        public KeyDeserializerStep setTopic(String topicName) {
            this.topicName = topicName;
            return this;
        }

        @Override
        public ValueSerializerStep setKeyDeserializer(Class<? extends Deserializer> deserializer) {
            this.keyDeserializer = deserializer.getName();
            return this;
        }

        @Override
        public OffsetStep setValueDeserializer(Class<? extends Deserializer> deserializer) {
            this.valueDeserializer = deserializer.getName();
            return this;
        }

        @Override
        public OptionalConfigurationStep fromEarlier() {
            this.offset = IKafkaConstants.OFFSET_RESET_EARLIER;
            return this;
        }

        @Override
        public OptionalConfigurationStep fromLatest() {
            this.offset = IKafkaConstants.OFFSET_RESET_LATEST;
            return this;
        }

        @Override
        public Consumer<T, U> subscribe() {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",",this.kafkaBroker));
            props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupIdConfig);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, this.keyDeserializer);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, this.valueDeserializer);
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.autoCommit);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.offset);
            Consumer<Long, String> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(this.topicName));
            return consumer;
        }
    }


}
