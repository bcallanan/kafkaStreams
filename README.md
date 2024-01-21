# Advanced kafkaStreams - real time use cases

Tech stack: Java 17, Docker, SpringBoot etc 3.2... I'll get around to updating the rest of the versions (but until then - look in the MVN POM). 

This repo is dedicated to kafka development and kafka streams. This repo will dig deep into the use of KStream and Ktable APIs. Also reviewed during this development we'll also dive into the different operators that are part of the Kafka Streams apis and implement a hand-ons service usings High Level DSL which are part of the KStreams API.

Build a Stateless and a Stateful set of applications.

Advance concepts exploration of Aggregations, Joins(eg. think of sql table joins - left, inner, outer)  and Windowing using KafkaStreams API.

   - <b>Remember</b>: 
     - <b>Left join</b>: This join returns all the rows of the table on the left side of the join and
       matches rows for the table on the right side of the join. For the rows for which there
       is no matching row on the right side, the result-set will contain null. LEFT JOIN is
       also known as LEFT OUTER JOIN. (Right join is opposite or reversed. Instead of the left
       side... you get the right side)
     - <b>Inner join</b>: The cross section between table A & B, selects all rows from both the
       tables as long as the condition is satisfied. This 'inner' keyword will create the
       result-set by combining all rows from both the tables where the condition satisfies
       i.e value of the common field will be the same. Again, the cross section. 
     - <b>Outer Join(Full Join)</b>: 'Full' 'Outer' join(Some databases use the keywords optionally)
       creates the result-set by combining results of both LEFT JOIN and RIGHT JOIN. The
       result-set will contain all the rows from both tables. For the rows for which there
       is no matching, the result-set will contain NULL values.

Build a Kafka Streams Enterprise level Retails Application using SpringBoot.
Writing interactive Queries to exposed the aggregated data using a RESTful apis with spring rest controllers from SpringBoot.

Unit and Integration using JUnit 5 as well.

![Alt text](./kafkaStreams.jpg?raw=true "Kafka Streams")

There are many types of use cases for using Kafka Streams API, a few are:

 - apply data transformations
 - data enrichment
 - branching the data into multiple data streams
 - aggregating the data
 - joining the data from multiple kafka topics and writing back to a topic. This addresses the immutability concept of kafka data before the consumer(s) services receive the data. 

![Alt text](./kafkaStreamAsAProducerConsumer.jpg?raw=true "Kafka Streams - producer/consumer")

The kafka Streams API uses the Java8 Functional programming style and DSL. 
 - Lambdas
 - Map, filter, and flatMap operators

#### Streams API: Stateless vs Stateful

Kafka consumer applications built on the KafkaConsumer API are stateless. The Kafka Consumer App(s) flow is as follows:
 
  - Read the event,
  - Process the event,
  - Move onto the next event
    
There's no notion of the consumer sharing cache with the Kafka Broker where the data can be aggregated or manipulated in any way. The consumer app is a terminal endpoint. The consumer app has other design options where a consumer can also 're-publish'. However, the intent of the Stream API is a little different. More on this later.
 
Consumer applications are great for notifications and/or processing each event independent of one another(eg: fifo queue). Consumer apps also do not have an easy way to join or aggregate events.

Real-time process are stateful operations. Use cases such as:
 - <b>retail</b>:
   - realtime calculations for number(s) of orders
   - realtime revenue projection
 - <b>Entertainment</b>:
   - realtime calculations of number of tickets sold.
   - realtime revenue projections by a movie.
        
#### Streams API Implementations
 
 
 There are two main implementations within the API, Streams DSL API and the Processor API.
 
 - <b>Stream DSL API</b>:
   - Higher level API - Predefined operations
     - Map
     - Flat Map
 - <b>Processor API</b>:
   - Low level API
   - Complex compared to the Streams DSL
   - Streams DSL is built on top of the Processor API
    
##### Terminologies   
 
Kafka stream processing has a series of processors:
 - <b>Source Processor</b>: source topic - reading from the source topic or topics
 - <b>Stream Processor</b>: processing logic - Aggregating, transforming, or joining the data. This is where the data enrichment happens.
 - <b>Sink Processor</b>: destination topic or end of the line.
    
  The concept of designing the Kafka Stream processing in this way is a Directed Acyclic Graph (DAG). Which means, these are connected nodes and are directly related to each other. This a flat DAG and has no parent(s). This DAG design as a Kafka Stream is called the Kafka Stream "Topology".
  
![Alt text](./FlatStreamProcessingTopology.jpg?raw=true "Kafka Streams - Stream Processing")


##### Stream Branching

More evolved topologies may be required for some designs to produce an aggregation or branch topology. This is called a sub-topology. Maybe easier to call it a tree topology, at least IMO anyway. 

![Alt text](./TreeStreamProcessingTopology.jpg?raw=true "Kafka Streams - Stream Processing")


##### Data Flow

At any given, the topology only processes one record at a time. With Sub-Topology(s) this rule is applicable to each Sub-Topology. This is how order is maintain should a topic have some type of order required.

![Alt text](./StreamProcessingDataFlow.jpg?raw=true "Kafka Streams - Stream Processing")


##### KStreams API


In a topology, the source processor is the key component in the topology. It is the component which has access to the topic data records and supplies the data records to the whole topology. The source processor has two design options to implement the solution, and when to use them:

   1) <b>KStream</b>:
      - <b>Continuous Stream Processing</b>: If you have a continuous stream of records where new data is
        continuously appended, and you need to perform transformations, filtering, aggregations, or
        complex stream processing operations, KStream is a suitable choice. KStream allows you to
        process the data as it arrives and maintain stateful operations.
      - <b>Real-time Analytics</b>: If your use case involves real-time analytics, where you need to compute
        aggregations over time windows, perform sliding window operations, or derive insights from
        the stream of records, KStream is a good fit. It enables you to perform various analytical
        operations on the stream data.
      - <b>Complex Stream Transformations</b>: When you require complex transformations and operations on
        the stream data, such as mapping, filtering, branching, or joining multiple streams together,
        KStream provides the necessary flexibility and functionality. It allows you to manipulate
        and transform the stream records based on your application logic.
   1) <b>KTable</b>:  
      - <b>Materialized Views</b>: If you need to maintain the latest state of the stream data for each key
        and perform point lookups efficiently, KTable is a suitable choice. KTable can be used to
        build materialized views of the stream, where you can retrieve the current value for a 
        specific key quickly.
      - <b>Table Joins</b>: If your use case involves joining the stream data with other tables or streams
        based on a common key, KTable provides a convenient way to perform these joins. It allows
        you to join KTables or KStreams together efficiently based on their keys.
      - <b>Interactive Queries</b>: When you need to support interactive queries and retrieve the latest
        value for a specific key in real-time, KTable is a good option. KTable maintains the latest
        value for each key, making it efficient for point lookups and queries.
      - <b>Stateful Processing</b>: If your application requires maintaining state and processing records
        based on the complete history of the stream data, KTable can be used to maintain the latest
        state for each key. It allows you to update and process the table based on new incoming
        records.

KStream is an abstraction in Kafka Streams which holds or has access to each event in the kafka Topic.

##### Joins

When performing a join operation between a KTable and a KStream in Apache Kafka’s Streams library, the result is typically a new KStream. The join operation combines records from the KTable and the KStream based on a common key and produces an output stream with the joined records. The result depends on the type of join operation performed:

  - <b>Inner Join</b>: An inner join between a KTable and a KStream will produce a new KStream that
    includes only the records where there is a match between the key of the KTable and the key
    of the KStream. The result will contain the joined records, combining the values from both
    the KTable and the KStream.
  - <b>Left Join</b>: A left join between a KTable and a KStream will produce a new KStream that includes all
    records from the KStream, along with matching records from the KTable. If there is no matching record
    in the KTable for a particular key in the KStream, a null value will be included in the result.
  - <b>Outer Join</b>: An outer join between a KTable and a KStream will produce a new KStream that includes
    all records from both the KTable and the KStream, whether or not there is a match between their keys.
    If there is no matching record in either the KTable or the KStream for a particular key, a null value
    will be included in the result.
      
The resulting KStream from the join operation can be further processed, filtered, transformed, or aggregated using the available operators and functions in Kafka Streams. It allows you to perform various operations on the joined stream to derive insights or produce the desired output based on your application requirements. Kafka Streams provides flexibility and allows you to perform joins between KStreams and KTables in different ways. You can use the leftJoin() or outerJoin() methods to explicitly perform left or outer joins, where the result retains both the characteristics of the KStream and the KTable. This flexibility enables you to choose the appropriate join type based on your specific use case and requirements.

