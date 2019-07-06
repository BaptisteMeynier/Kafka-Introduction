#!/usr/bin/env bash

java -cp ../../../target/kafka-introduction-jar-with-dependencies.jar com.meynier.kafka.launcher.ConsumeApp -b localhost:9092 -g group1 -t Hello-Kafka