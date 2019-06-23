package com.meynier.kafka.creator;

import com.meynier.kafka.constants.IKafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class KafkaConsumerBuilder<T extends Deserializer,U extends Deserializer> {

    private KafkaConsumerBuilder(Builder builder) {

    }

    public static BrokerHostStep addBrokerHost(final String host) {
        return new Builder();
    }

    public interface InitialStep {
        BrokerHostStep addBrokerHost(final String host);
        GroupIdStep setGroupId(final String groupId);
    }

    public interface BrokerHostStep {
        InitialStep withPort(final int port);
    }

    public interface GroupIdStep {
        OffsetStep setTopic(final String topicName);
    }

    public interface OffsetStep {
        OptionalConfigurationStep fromEarlier();
        OptionalConfigurationStep fromLatest();
    }

    public interface OptionalConfigurationStep <T,U> {
        OptionalConfigurationStep setMaxPollRecords(int maxPollRecords);
        OptionalConfigurationStep enableAutoCommit();
        OptionalConfigurationStep disableAutocommit();

        Consumer<T, U> subscribe();
    }


    private static class Builder<T,U> implements InitialStep, BrokerHostStep, GroupIdStep, OffsetStep, OptionalConfigurationStep {

        private String BROKER_PATTERN = "%s:%n";

        private String host;

        private List<String> kafkaBroker = new ArrayList<>();

        private String topicName = IKafkaConstants.TOPIC_NAME;

        private String groupIdConfig = IKafkaConstants.GROUP_ID_CONFIG;

        private String offset = IKafkaConstants.OFFSET_RESET_EARLIER;

        private Integer maxPollRecords = IKafkaConstants.MAX_POLL_RECORDS;

        private boolean autoCommit = IKafkaConstants.AUTO_COMMIT;


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
        public InitialStep withPort(int port) {
            this.kafkaBroker.add(String.format(BROKER_PATTERN, host, port));
            return this;
        }

        @Override
        public OffsetStep setTopic(String topicName) {
            checkArg(host, "Topic cannot be null or empty");
            this.topicName = topicName;
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
            final Type[] deserializer = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();

            final Class<T> type;
            final Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",",this.kafkaBroker));
            props.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupIdConfig);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deserializer[0].getClass().getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer[1].getClass().getName());
            props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.autoCommit);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.offset);
            final Consumer<T, U> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList(this.topicName));
            return consumer;
        }
    }


}
