/**
 * 
 */
package com.bcallanan.kstreamgreetingproducer.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.bcallanan.kstreamgreetingproducer.domain.Greeting;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 
 */
//@Slf4j
public class GreetingSerdes implements Serde<Greeting> {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule( new JavaTimeModule() )
            .configure( SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    
    /**
     */
    public GreetingSerdes() {}
    
    @Override
    public Serializer<Greeting> serializer() {
        // TODO Auto-generated method stub
        return new GreetingSerializer( objectMapper );
    }

    @Override
    public Deserializer<Greeting> deserializer() {
        // TODO Auto-generated method stub
        return new GreetingDeserializer( objectMapper );
    }

    
}
