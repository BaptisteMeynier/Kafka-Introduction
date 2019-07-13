package com.meynier.kafka.launcher;

import com.meynier.kafka.creator.KafkaProducerBuilder;
import com.meynier.kafka.service.KafkaService;
import org.apache.commons.cli.*;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

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
                        .addBrokers(cmd.getOptionValue("b"))
                        .setClientId(cmd.getOptionValue("c"))
                        .setOptionalParam(ProducerConfig.ACKS_CONFIG,"all") // possible value -1
                        .setOptionalParam(ProducerConfig.RETRIES_CONFIG,"5")
                        .setOptionalParam(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,Boolean.TRUE.toString())
                        .build();

        KafkaService.runProducer(producer, cmd.getOptionValue('t'));
    }


}
