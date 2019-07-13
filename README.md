# Kafka Introduction

## ZooKeeper

ZooKeeper is a centralized service for maintaining configuration information, naming, providing distributed synchronization, and providing group services.

## Kafka

#### COMMANDS

##### Create a topic:
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic topic-name       
example : bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic Hello-Kafka        

##### List topic:
bin/kafka-topics.sh --list --zookeeper localhost:2181

##### Alter topic:
bin/kafka-topics.sh —zookeeper localhost:2181 --alter --topic topic_name --partitions count   
We have already created a topic “Hello-Kafka” with single partition count and one replica factor.   
Now using “alter” command we have changed the partition count.   
bin/kafka-topics.sh --zookeeper localhost:2181 --alter --topic Hello-kafka --partitions 2    

##### Delete topic:
bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic Hello-kafka

#### SETTINGS

##### EARLIEST LATEST
When you consume a topic you can choose to set two reading strategy for your new consumming GROUP.   
Either consuming message from the begining (EARLIEST)  
Either consuming only new producing message (LATEST)   
When you have already consume messages from a group you cant use this feature for reprocess then

##### ACKS ALL
Acks = acknowledge  
Can be contain value 0 UDP (no guarantee), 1 TCP (leader) or ALL (replicated).     
The setting acks all must be configure with min.insync.replicas this setting is put on the topic.
min.insync.replicas command the acknowledge feature to validate the acknowledge for a number of broker (min value is 2)

##### RETRIES
Work with acks all and min insync.replicas. Allow to define the behaviors when propertie min.insync.replicas is not validated. 
Message should be send again.

_________________________

## Launch
### Docker-compose
#### Single broker
docker-compose -f src/main/docker/zk-single-kafka-single.yml up   

```
[baptiste launcher]$ docker ps    
CONTAINER ID        IMAGE                             COMMAND                  CREATED             STATUS              PORTS                                        NAMES    
6e23a6b46d58        confluentinc/cp-kafka:5.1.3       "/etc/confluent/do..."   14 seconds ago      Up 13 seconds       9092/tcp, 0.0.0.0:19092->19092/tcp           docker_kafka1_1    
17bd04652f53        confluentinc/cp-zookeeper:5.1.3   "/etc/confluent/do..."   22 hours ago        Up 13 seconds       2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp   docker_zoo1_1     

docker exec -ti 17bd04652f53 /bin/sh   

kafka-topics --list --zookeeper localhost:2181   

# kafka-topics --list --zookeeper localhost:2181                                                                                   
Hello-Kafka   
__confluent.support.metrics   
__consumer_offsets   
demo   

root@zoo1:/# kafka-topics --describe --zookeeper localhost:2181 --topic demo
Topic:demo      PartitionCount:1        ReplicationFactor:1     Configs:
        Topic: demo     Partition: 0    Leader: 1       Replicas: 1     Isr: 1
```

#### Multiple brokers
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

kafka-topics --create --zookeeper localhost:22181 --replication-factor 2  --partitions 3 --config min.insync.replicas=2 --topic Hello-Kafka 
Created topic "Hello-Kafka".

# kafka-topics --describe --zookeeper localhost:22181 --topic Hello-Kafka
Topic:Hello-Kafka       PartitionCount:3        ReplicationFactor:2     Configs:min.insync.replicas=2
        Topic: Hello-Kafka      Partition: 0    Leader: 3       Replicas: 3,1   Isr: 3,1
        Topic: Hello-Kafka      Partition: 1    Leader: 1       Replicas: 1,2   Isr: 1,2
        Topic: Hello-Kafka      Partition: 2    Leader: 2       Replicas: 2,3   Isr: 2,3
```

Outside the container
```
[kafka-introduction]$ ./src/main/launcher/multiple/multiple_produce.sh 
Record sent with key 0 to partition 0 with offset 333
Record sent with key 1 to partition 1 with offset 333
Record sent with key 2 to partition 2 with offset 334
Record sent with key 3 to partition 0 with offset 334
Record sent with key 4 to partition 1 with offset 334
Record sent with key 5 to partition 2 with offset 335
Record sent with key 6 to partition 0 with offset 335
Record sent with key 7 to partition 1 with offset 335
Record sent with key 8 to partition 2 with offset 336

```
On on other terminal session
```
[baptiste@DESKTOP-FUI7H3K multiple]$ ./multiple_consume.sh
Record Key null
Record value This is record 748
Record partition 1
Record offset 582
Record Key null
Record value This is record 749
Record partition 2
Record offset 583

```

(The recording key is null, this is possible to define a key to produce the message on a specific partition)

#### Documentation
https://www.youtube.com/watch?v=KWAELycyxTc
https://dzone.com/articles/kafka-producer-and-consumer-example
https://kafka.apache.org/intro