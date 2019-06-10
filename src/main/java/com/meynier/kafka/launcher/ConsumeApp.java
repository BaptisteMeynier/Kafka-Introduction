package com.meynier.kafka.launcher;


import com.meynier.kafka.service.KafkaService;

public class ConsumeApp {

    public static void main(String[] args) {
        KafkaService.runConsumer();
    }
}
