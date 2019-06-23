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





}
