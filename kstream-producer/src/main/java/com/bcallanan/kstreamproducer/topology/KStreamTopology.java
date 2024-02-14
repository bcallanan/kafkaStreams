/**
 * 
 */
package com.bcallanan.kstreamproducer.topology;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bcallanan.kstreamproducer.KafkaStreamApp;
import com.bcallanan.kstreamproducer.domain.Greeting;
import com.bcallanan.kstreamproducer.serdes.SerdesFactory;

/**
 * 
 */

public class KStreamTopology {

    public static final String GREETINGS = "greetings";
    public static final String GREETINGS1 = "greetings1";
    public static final String GREETINGS2 = "greetings2";
    public static final String GREETINGS_UPPERCASE = "greetings-uppercase";
    private static final Logger log = LoggerFactory.getLogger(KafkaStreamApp.class);
    public static final String GREETINGS_MERGED = "greetingsMerged";

    public static KStream<String, Greeting> getGreetingKstream( StreamsBuilder streamBuilder ) {

        KStream<String, Greeting> greetingStream1 = streamBuilder
               .stream(GREETINGS1,
                       Consumed.with( Serdes.String(), SerdesFactory.greetingSerdesFactoryBuilder() ));
           
       greetingStream1.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS));
           
       KStream<String, Greeting> greetingStream2 = streamBuilder
               .stream(GREETINGS2,
                       Consumed.with( Serdes.String(), SerdesFactory.greetingSerdesFactoryBuilder() ));
       
       greetingStream2.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS2));

       KStream<String, Greeting> mergedStream = greetingStream1.merge( greetingStream2 ); //, (Named) GREETINGS2);
           
       mergedStream.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS_MERGED));
           
       return mergedStream;

    }
    
    public static KStream<String, Greeting> getGreetingKstreamWithGenerics( StreamsBuilder streamBuilder ) {

        KStream<String, Greeting> greetingStream1 = streamBuilder
               .stream(GREETINGS1,
                       Consumed.with( Serdes.String(), SerdesFactory.greetingSerdesFactoryBuilder() ));
           
       greetingStream1.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS));
           
       KStream<String, Greeting> greetingStream2 = streamBuilder
               .stream(GREETINGS2,
                       Consumed.with( Serdes.String(), SerdesFactory.greetingSerdesFactoryBuilder() ));
       
       greetingStream2.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS2));

       KStream<String, Greeting> mergedStream = greetingStream1.merge( greetingStream2 ); //, (Named) GREETINGS2);
           
       mergedStream.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS_MERGED));
           
       return mergedStream;

   }

    public static Topology buildSerdeTopologyWithGenerics() {

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Greeting> mergedStream = getGreetingKstreamWithGenerics(  streamsBuilder );
        
        mergedStream.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS_MERGED));

        KStream<String, Greeting> modifiedKStreamValues = mergedStream
                .filter(( key, value) -> value.message().length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter: " + GREETINGS_MERGED + ":" +  key +":" + value );
                })
            .map( (key, value) -> KeyValue.pair( key.toUpperCase(), 
                    new Greeting( value.message().toUpperCase(), value.timeStamp() )));

        modifiedKStreamValues.print(Printed.< String, Greeting> toSysOut().withLabel(GREETINGS_MERGED));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_MERGED, 
                Produced.with( Serdes.String(), SerdesFactory.greetingSerdesUsingGenerics() ));

        return streamsBuilder.build();
    }

    public static Topology buildOptimizedAndCustomSerdeTopology() {

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, Greeting> mergedStream = getGreetingKstream(  streamsBuilder );
        
        mergedStream.print(Printed.<String, Greeting> toSysOut().withLabel(GREETINGS_MERGED));

        KStream<String, Greeting> modifiedKStreamValues = mergedStream
                .filter(( key, value) -> value.message().length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter: " + GREETINGS_MERGED + ":" +  key +":" + value );
                })
            .map( (key, value) -> KeyValue.pair( key.toUpperCase(), 
                    new Greeting( value.message().toUpperCase(), value.timeStamp() )));

        modifiedKStreamValues.print(Printed.< String, Greeting> toSysOut().withLabel(GREETINGS_MERGED));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_MERGED, 
                Produced.with( Serdes.String(), SerdesFactory.greetingSerdesFactoryBuilder() ));

        return streamsBuilder.build();
    }

    public static Topology buildCustomSerdeTopology() {

        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream1 = streamsBuilder
            .stream(GREETINGS1,
                    Consumed.with( Serdes.String(), Serdes.String()));
        greetingStream1.print(Printed.< String, String> toSysOut().withLabel(GREETINGS1));
        
        KStream<String, String> greetingStream2 = streamsBuilder
            .stream(GREETINGS2,
                    Consumed.with( Serdes.String(), Serdes.String()));
        greetingStream2.print(Printed.< String, String> toSysOut().withLabel(GREETINGS2));

        KStream<String, String> mergedStream = greetingStream1.merge( greetingStream2 ); //, (Named) GREETINGS2);
        
        mergedStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        KStream<String, String> modifiedKStreamValues = mergedStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter " + key +":" + value );
                })
            .map( (key, value) -> KeyValue.pair( key.toUpperCase(), value.toUpperCase()));

        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_MERGED, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();
    }

    public static Topology buildMergeWithDefaultedSerdesTopology() {

        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream1 = streamsBuilder.stream(GREETINGS1 );
        greetingStream1.print(Printed.< String, String> toSysOut().withLabel(GREETINGS1));
        
        KStream<String, String> greetingStream2 = streamsBuilder.stream(GREETINGS2);
        greetingStream2.print(Printed.< String, String> toSysOut().withLabel(GREETINGS2));

        KStream<String, String> mergedStream = greetingStream1.merge( greetingStream2 ); //, (Named) GREETINGS2);
        
        mergedStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        KStream<String, String> modifiedKStreamValues = mergedStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter " + key +":" + value );
                })
            .map( (key, value) -> KeyValue.pair( key.toUpperCase(), value.toUpperCase()));

        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_MERGED );

        return streamsBuilder.build();
    }

    public static Topology buildMergeTopology() {

        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream1 = streamsBuilder
            .stream(GREETINGS1,
                    Consumed.with( Serdes.String(), Serdes.String()));
        greetingStream1.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        KStream<String, String> greetingStream2 = streamsBuilder
            .stream(GREETINGS2,
                    Consumed.with( Serdes.String(), Serdes.String()));
        greetingStream2.print(Printed.< String, String> toSysOut().withLabel(GREETINGS2));

        KStream<String, String> mergedStream = greetingStream1.merge( greetingStream2 ); //, (Named) GREETINGS2);
        
        mergedStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        KStream<String, String> modifiedKStreamValues = mergedStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter " + key +":" + value );
                })
            .map( (key, value) -> KeyValue.pair( key.toUpperCase(), value.toUpperCase()));

        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS_MERGED));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_MERGED, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();
    }

    public static Topology buildMapTopology() {

        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream = streamsBuilder
            .stream(GREETINGS,
                    Consumed.with( Serdes.String(), Serdes.String()));
        
        greetingStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        KStream<String, String> modifiedKStreamValues = greetingStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter " + key +":" + value );
                })
            .map( (key, value) -> KeyValue.pair( key.toUpperCase(), value.toUpperCase()));

        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_UPPERCASE, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();
    }

    public static Topology buildMapValueTopology() {

        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream = streamsBuilder
            .stream(GREETINGS,
                    Consumed.with( Serdes.String(), Serdes.String()));
        
        greetingStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        KStream<String, String> modifiedKStreamValues = greetingStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter " + key +":" + value );
                })
                .mapValues( (readOnlyKey, value) -> value.toUpperCase());
         
        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));

        System.out.println( "buildMapValueTopology: Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_UPPERCASE, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();
    }

    public static Topology buildFlatMapTopology() {
        
        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream = streamsBuilder
            .stream(GREETINGS,
                    Consumed.with( Serdes.String(), Serdes.String()));
        
        greetingStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        KStream<String, String> modifiedKStreamValues = greetingStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "buildFlatMapTopology after filter " + key +":" + value );
                    log.info( "buildFlatMapTopology after filter key: {}, value: {}", key, value);
                    
                })
                .flatMap(( key, value ) -> {
                  List<String> newValueList = Arrays.asList( value.split( "-" ));
                  List<KeyValue<String, String>> modStreamList = newValueList
                    .stream()
                    .map( splitValue -> KeyValue.pair( key.toUpperCase(), splitValue))
                    .collect( Collectors.toList());
      
                    return modStreamList;
              });

        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_UPPERCASE, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();
    }
    
    public static Topology buildFlatMapValueTopology() {
        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream = streamsBuilder
            .stream(GREETINGS,
                    Consumed.with( Serdes.String(), Serdes.String()));
        
        greetingStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        KStream<String, String> modifiedKStreamValues = greetingStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter " + key +":" + value );
                })
                .flatMapValues( (key, val) -> {
                    
                    List<String> newValueList = Arrays.asList( val.split( "-" ));

                    List<String> modStreamList = newValueList
                            .stream()
                            .map( String::toUpperCase )
                            .collect( Collectors.toList());
            
                    return modStreamList;
                });

        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_UPPERCASE, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();

    }
    
    public static Topology buildFlatMapValueWithMapValuesAndPeekTopology() {

        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream = streamsBuilder
            .stream(GREETINGS,
                    Consumed.with( Serdes.String(), Serdes.String()));
        
        greetingStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        KStream<String, String> modifiedKStreamValues = greetingStream
                .filter(( key, value) -> value.length() > 5 )
                .peek( (key, value) -> {
                    System.out.println( "after filter " + key +":" + value );
                })
                .mapValues( (readOnlyKey, value) -> value.toUpperCase())
                .peek( (key, value) -> {
                    System.out.println( "after map values " + key +":" + value );
                })
                .flatMapValues( (key, val) -> {
            
                    List<String> newValueList = Arrays.asList( val.split( "-" ));

                    List<String> modStreamList = newValueList
                        .stream()
                        .map( String::toUpperCase )
                        .collect( Collectors.toList());
    
                    return modStreamList;
        });
        
        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));

        System.out.println( "Topic data : " + modifiedKStreamValues);
        
        modifiedKStreamValues.to( GREETINGS_UPPERCASE, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();
    }
}
