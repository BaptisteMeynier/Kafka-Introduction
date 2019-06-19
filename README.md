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

```   
docker-compose -f src/main/docker/k-multiple-kafka-multiple.yml up   

 
[baptiste@DESKTOP ~]$ docker ps   
CONTAINER ID        IMAGE                              COMMAND                  CREATED             STATUS              PORTS               NAMES    
089416e909ed        confluentinc/cp-kafka:latest       "/etc/confluent/do..."   33 minutes ago      Up 23 seconds                           docker_kafka-1_1    
2186ab7cf804        confluentinc/cp-kafka:latest       "/etc/confluent/do..."   33 minutes ago      Up 23 seconds                           docker_kafka-3_1   
27ea276aa418        confluentinc/cp-kafka:latest       "/etc/confluent/do..."   33 minutes ago      Up 23 seconds                           docker_kafka-2_1   
ffe9eca02ce6        confluentinc/cp-zookeeper:latest   "/etc/confluent/do..."   33 minutes ago      Up 23 seconds                           docker_zookeeper-1_1   
9a70667e6a93        confluentinc/cp-zookeeper:latest   "/etc/confluent/do..."   33 minutes ago      Up 23 seconds                           docker_zookeeper-2_1   
94ca2b318337        confluentinc/cp-zookeeper:latest   "/etc/confluent/do..."   33 minutes ago      Up 23 seconds                           docker_zookeeper-3_1    
   
   
docker exec -it 089416e909ed /bin/sh    
```

#### Documentation
https://www.youtube.com/watch?v=KWAELycyxTc
https://dzone.com/articles/kafka-producer-and-consumer-example
https://kafka.apache.org/intro