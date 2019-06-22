package com.meynier.kafka.creator;

import com.meynier.kafka.constants.IKafkaConstants;

public class ProducerBuilder {

    private String kafkaBroker = IKafkaConstants.KAFKA_BROKERS;

    private Integer messageFoundCount=IKafkaConstants.MESSAGE_COUNT;

    private String clientId=IKafkaConstants.CLIENT_ID;

    private String topicName=IKafkaConstants.TOPIC_NAME;

    private String groupIdConfig=IKafkaConstants.GROUP_ID_CONFIG;

    private String offsetResetLatest=IKafkaConstants.OFFSET_RESET_LATEST;

    private String offsetResetEarlier=IKafkaConstants.OFFSET_RESET_EARLIER;

    private Integer maxPollRecords=IKafkaConstants.MAX_POLL_RECORDS;

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
        ClientIdStep setClientId(final String clientId);
    }

    public interface ClientIdStep {
        KeySerializerStep setTopic(final String topicName);
    }

    public interface KeySerializerStep{
        ValueSerializerStep setTopic(final String topicName);
    }

    public interface OffsetStep {
        InitialStep fromEarlier();
        InitialStep fromLatest();
    }

    public interface Build {
        ProducerBuilder build();
    }

    public static class Builder implements InitialStep, BrokerHostStep, Build{

        @Override
        public ProducerBuilder build() {
            return null;
        }
    }


}
