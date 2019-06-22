package com.meynier.kafka.launcher;


import com.meynier.kafka.service.KafkaService;
import org.apache.commons.cli.Options;

public class ConsumeApp {



    /*
        String KAFKA_BROKERS = "localhost:9092";

    Integer MESSAGE_COUNT=1000;

    String CLIENT_ID="client1";

    String TOPIC_NAME="demo";

    String GROUP_ID_CONFIG="consumerGroup1";

    Integer MAX_NO_MESSAGE_FOUND_COUNT=100;

    String OFFSET_RESET_LATEST="latest";

    String OFFSET_RESET_EARLIER="earliest";

    Integer MAX_POLL_RECORDS=1;
     */

    // create Options object
    static Options options = new Options();

    static{
        options.addOption("KAFKA_BROKERS", true, "List of kafka broker");
        options.addOption("TOPIC_NAME", true, "Topic name that will be consumed");
        options.addOption("e", false, "Set the reading offset at the earliest position");
        options.addOption("l", false, "Set the reading offset at the latest position");
    }


    public static void main(String[] args) {
        // add t option



        KafkaService.runConsumer();
    }
}
