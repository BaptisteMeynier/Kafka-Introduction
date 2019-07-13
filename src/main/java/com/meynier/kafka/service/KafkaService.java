package com.meynier.kafka.service;

import com.meynier.kafka.constants.IKafkaConstants;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

public class KafkaService {

    public static void runProducer(Producer<Long, String> producer, final String topicName) {

        for (int index = 0; index < IKafkaConstants.MESSAGE_COUNT; index++) {

            ProducerRecord<Long, String> record =
                    new ProducerRecord<>(topicName, "This is record " + index);

            try {
                RecordMetadata metadata = producer.send(record).get();
                System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
                        + " with offset " + metadata.offset());
            } catch (ExecutionException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            } catch (InterruptedException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }
        }
    }

    public static void runConsumer(Consumer consumer, final String topicName) {
        int noMessageFound = 0;

        try {
            consumer.subscribe(Collections.singletonList(topicName));
            while (true) {
                ConsumerRecords<Long, String> consumerRecords = consumer.poll(Duration.ofSeconds(60));

                if (consumerRecords.count() == 0) {
                    noMessageFound++;
                    if (noMessageFound > IKafkaConstants.MAX_NO_MESSAGE_FOUND_COUNT)
                        // If no message found count is reached to threshold exit loop.
                        break;
                    else
                        continue;
                }

                //print each record.
                consumerRecords.forEach(record -> {
                    System.out.println("Record Key " + record.key());
                    System.out.println("Record value " + record.value());
                    System.out.println("Record partition " + record.partition());
                    System.out.println("Record offset " + record.offset());
                });
            }
        } finally {
            consumer.close();
        }

    }

}
