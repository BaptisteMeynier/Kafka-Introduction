package com.meynier.kafka.launcher;


import com.meynier.kafka.creator.KafkaConsumerBuilder;
import com.meynier.kafka.service.KafkaService;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Optional;

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

    static {
        options.addOption(new Option("b", "KAFKA_BROKERS", true, "List of kafka broker compliant with the following regex format => (HOST:PORT,{1})+"));
        options.addOption(new Option("t", "TOPIC_NAME", true, "Topic name that will be consumed"));
        options.addOption(new Option("g", "GROUP_ID", true, "Group id of the consumer"));
        options.addOption(new Option("e", "OFFSET_RESET_EARLIER", false, "Set the reading offset at the earliest position"));
        options.addOption(new Option("l", "OFFSET_RESET_LATEST", false, "Set the reading offset at the latest position"));
    }


    public static void main(String[] args) throws ParseException {
        // add t option

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("e")) {
            // print the date and time
        }

        Consumer consumer = KafkaConsumerBuilder
                .<StringDeserializer, IntegerDeserializer>build()
                .addBrokers(cmd.getOptionValue("b"))
                .setGroupId(cmd.getOptionValue("g"))
                .setTopic(cmd.getOptionValue("t"))
                .fromEarlier()
                .subscribe();

        KafkaService.runConsumer(consumer);
    }
}
