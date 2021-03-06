package com.meynier.kafka.launcher;


import com.meynier.kafka.creator.KafkaConsumerBuilder;
import com.meynier.kafka.creator.OffsetStrategy;
import com.meynier.kafka.service.KafkaService;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.consumer.Consumer;


public class ConsumeApp {

    static Options options = new Options();

    static {
        options.addOption(new Option("b", "KAFKA_BROKERS", true, "List of kafka broker compliant with the following regex format => (HOST:PORT,{1})+"));
        options.addOption(new Option("g", "GROUP_ID", true, "Group id of the consumer"));
        options.addOption(new Option("t", "TOPIC_NAME", true, "Topic name that will be consumed"));
        options.addOption(new Option("e", "OFFSET_RESET_EARLIEST", false, "Set the reading offset at the earliest position"));
        options.addOption(new Option("l", "OFFSET_RESET_LATEST", false, "Set the reading offset at the latest position"));
    }


    public static void main(String[] args) throws ParseException {

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);

        OffsetStrategy os = OffsetStrategy.EARLIEST;
        if (cmd.hasOption("l")) {
            os = OffsetStrategy.LATEST;
        }

        final String topicName = cmd.getOptionValue("t");

        final Consumer consumer =
                KafkaConsumerBuilder
                .addBrokers(cmd.getOptionValue("b"))
                .setGroupId(cmd.getOptionValue("g"))
                .setOffsetStrategy(os)
                .build();

        KafkaService.runConsumer(consumer,topicName);
    }
}
