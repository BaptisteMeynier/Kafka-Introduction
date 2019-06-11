# Kafka Introduction

## ZooKeeper

ZooKeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services.

## Kafka

##### Create a topic:
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 
--partitions 1 --topic topic-name   
example : bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1   
          --partitions 1 --topic Hello-Kafka   


##### List topic:
bin/kafka-topics.sh --list --zookeeper localhost:2181

## Launch
### Docker-compose
docker-compose -f src/main/docker/zk-single-kafka-single.yml up


#### Documentation
https://www.youtube.com/watch?v=KWAELycyxTc
https://dzone.com/articles/kafka-producer-and-consumer-example
https://kafka.apache.org/intro