# Kafka Introduction

## ZooKeeper

## Kafka

##### Create a topic:
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 
--partitions 1 --topic topic-name   
example : bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1   
          --partitions 1 --topic Hello-Kafka   


##### List topic:
bin/kafka-topics.sh --list --zookeeper localhost:2181


#### Documentation
https://www.youtube.com/watch?v=KWAELycyxTc