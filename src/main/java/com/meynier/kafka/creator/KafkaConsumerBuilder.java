package com.meynier.kafka.creator;

import com.meynier.kafka.constants.IKafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.*;

import java.util.*;

public class KafkaConsumerBuilder {

    public static GroupIdStep addBrokers(final String brokers) {
        return new Builder().addBrokers(brokers);
    }

    public static BrokerHostStep addBrokerHost(final String host) {
        return new Builder().addBrokerHost(host);
    }

    public interface BrokerStep {
        GroupIdStep addBrokers(final String brokers);

        BrokerHostStep addBrokerHost(final String groupId);
    }

    public interface GroupIdStep {
        BrokerHostStep addBrokerHost(final String host);

        OffsetStep setGroupId(final String groupId);
    }

    public interface BrokerHostStep {
        GroupIdStep withPort(final int port);
    }


    public interface OffsetStep {
        OptionalConfigurationStep setOffsetStrategy(OffsetStrategy os);

        OptionalConfigurationStep fromEarlier();

        OptionalConfigurationStep fromLatest();
    }

    public interface OptionalConfigurationStep {
        OptionalConfigurationStep setOptionalParam(String key, String value);

        OptionalConfigurationStep setMaxPollRecords(int maxPollRecords);

        OptionalConfigurationStep enableAutoCommit();

        OptionalConfigurationStep disableAutocommit();

        Consumer build();
    }


    private static class Builder implements BrokerHostStep, BrokerStep, GroupIdStep, OffsetStep, OptionalConfigurationStep {

        private String BROKER_PATTERN = "%s:%n";

        private String host;

        private final List<String> kafkaBroker = new ArrayList<>();

        private String topicName = IKafkaConstants.TOPIC_NAME;

        private String groupIdConfig = IKafkaConstants.GROUP_ID_CONFIG;

        private String offset = IKafkaConstants.OFFSET_RESET_EARLIER;

        private Integer maxPollRecords = IKafkaConstants.MAX_POLL_RECORDS;

        private boolean autoCommit = IKafkaConstants.AUTO_COMMIT;

        private final Properties properties = new Properties();

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
        public GroupIdStep addBrokers(String brokers) {
            this.kafkaBroker.add(brokers);
            return this;
        }

        @Override
        public BrokerHostStep addBrokerHost(String host) {
            checkArg(host, "Broker host cannot be null or empty");
            this.host = host;
            return this;
        }

        @Override
        public OffsetStep setGroupId(String groupId) {
            this.groupIdConfig = groupId;
            return this;
        }

        @Override
        public GroupIdStep withPort(int port) {
            this.kafkaBroker.add(String.format(BROKER_PATTERN, host, port));
            return this;
        }

        @Override
        public OptionalConfigurationStep setOffsetStrategy(OffsetStrategy os) {
            this.offset = os.toString();
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
        public OptionalConfigurationStep setOptionalParam(String key, String value) {
            if (properties.containsKey(key)) {
                throw new IllegalArgumentException("Key was already recorded");
            }
            this.properties.put(key, value);
            return this;
        }


        @Override
        public Consumer build() {
            properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.join(",", this.kafkaBroker));
            properties.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupIdConfig);
            properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
            properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
            properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, this.autoCommit);
            properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.offset);
            return new KafkaConsumer<>(properties);
        }
    }


}
