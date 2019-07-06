package com.meynier.kafka.launcher;

import com.meynier.kafka.creator.KafkaConsumerBuilder;
import com.meynier.kafka.creator.KafkaProducerBuilder;
import com.meynier.kafka.service.KafkaService;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

public class ProduceApp {

    static Options options = new Options();

    static {
        options.addOption(new Option("b", "KAFKA_BROKERS", true, "List of kafka broker compliant with the following regex format => (HOST:PORT,{1})+"));
        options.addOption(new Option("c", "CLIENT_ID", true, "Client id of the producer"));
        options.addOption(new Option("t", "TOPIC_NAME", true, "Topic name that will be consumed"));
    }

    public static void main(String[] args) throws ParseException {

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd = parser.parse(options, args);

        final Producer producer =
                KafkaProducerBuilder
                        .build()
                        .addBrokers(cmd.getOptionValue("b"))
                        .setClientId(cmd.getOptionValue("c"))
                        .setTopic(cmd.getOptionValue("t"))
                        .produce();

        KafkaService.runProducer();
    }


}
