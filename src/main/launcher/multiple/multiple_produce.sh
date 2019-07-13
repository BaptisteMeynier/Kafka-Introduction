#!/usr/bin/env bash

java -cp ./target/kafka-introduction-jar-with-dependencies.jar com.meynier.kafka.launcher.ProduceApp -b localhost:19092,localhost:29092,localhost:39092 -c client1 -t Hello-Kafka