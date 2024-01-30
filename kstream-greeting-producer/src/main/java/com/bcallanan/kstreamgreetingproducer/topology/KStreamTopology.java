/**
 * 
 */
package com.bcallanan.kstreamgreetingproducer.topology;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;

/**
 * 
 */
public class KStreamTopology {

    public static final String GREETINGS = "greetings";
    public static final String GREETINGS_UPPERCASE = "greetings-uppercase";
    
    
    public static Topology buildTopology() {
        
        // first get the topic items frm the lowercase topic
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, String> greetingStream = streamsBuilder
            .stream(GREETINGS,
                    Consumed.with( Serdes.String(), Serdes.String()));
        
        greetingStream.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));
        
        KStream<String, String> modifiedKStreamValues = greetingStream
                .map( (key, value) -> KeyValue.pair( key.toUpperCase(), value.toUpperCase()));
//            .filterNot(( key, value) -> value.length() > 5 )
//            .mapValues( (readOnlyKey, value) -> value.toUpperCase());

        modifiedKStreamValues.print(Printed.< String, String> toSysOut().withLabel(GREETINGS));

        modifiedKStreamValues.to( GREETINGS_UPPERCASE, 
                Produced.with( Serdes.String(), Serdes.String()));

        return streamsBuilder.build();
    }
}
