package com.meynier.kafka.creator;

import com.meynier.kafka.constants.IKafkaConstants;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Objects;

public class KafkaConsumerBuilder {


    private KafkaConsumerBuilder(Builder builder){

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

        KafkaConsumerBuilder build();
    }


    private static class Builder implements InitialStep, BrokerHostStep, GroupIdStep, KeyDeserializerStep, ValueSerializerStep, OffsetStep, OptionalConfigurationStep {

        private String kafkaBroker = IKafkaConstants.KAFKA_BROKERS;

        private Integer messageFoundCount = IKafkaConstants.MESSAGE_COUNT;

        private String clientId = IKafkaConstants.CLIENT_ID;

        private String topicName = IKafkaConstants.TOPIC_NAME;

        private String groupIdConfig = IKafkaConstants.GROUP_ID_CONFIG;

        private String offsetResetLatest = IKafkaConstants.OFFSET_RESET_LATEST;

        private String offsetResetEarlier = IKafkaConstants.OFFSET_RESET_EARLIER;

        private Integer maxPollRecords = IKafkaConstants.MAX_POLL_RECORDS;

        private boolean autoCommit = true;


        private void checkArg(String arg, final String msg){
            if(Objects.nonNull(arg) && arg.isEmpty()){
                throw new IllegalArgumentException(msg);
            }
            Objects.requireNonNull(arg,msg);
        }

        @Override
        public OptionalConfigurationStep setMaxPollRecords(int maxPollRecords) {
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
            checkArg(host,"Broker host cannot be null or empty");

            return this;
        }

        @Override
        public GroupIdStep setGroupId(String groupId) {
            return this;
        }

        @Override
        public InitialStep withBrokerPort(int port) {
            return this;
        }

        @Override
        public KeyDeserializerStep setTopic(String topicName) {
            return this;
        }

        @Override
        public ValueSerializerStep setKeyDeserializer(Class<? extends Deserializer> deserializer) {
            return this;
        }

        @Override
        public OffsetStep setValueDeserializer(Class<? extends Deserializer> deserializer) {
            return this;
        }

        @Override
        public OptionalConfigurationStep fromEarlier() {
            return this;
        }

        @Override
        public OptionalConfigurationStep fromLatest() {
            return this;
        }

        @Override
        public KafkaConsumerBuilder build() {
            return new KafkaConsumerBuilder(this);
        }
    }


}
