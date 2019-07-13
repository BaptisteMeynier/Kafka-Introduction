#!/usr/bin/env bash

java -cp ./target/kafka-introduction-jar-with-dependencies.jar com.meynier.kafka.launcher.ConsumeApp -b localhost:19092,localhost:29092,localhost:39092 -g group1 -t Hello-Kafka