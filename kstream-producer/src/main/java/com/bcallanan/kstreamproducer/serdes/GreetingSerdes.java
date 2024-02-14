/**
 * 
 */
package com.bcallanan.kstreamproducer.serdes;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.bcallanan.kstreamproducer.domain.Greeting;

/**
 * 
 */
//@Slf4j
public class GreetingSerdes extends AbstractBaseSerdes implements Serde<Greeting> {

    /**
     */
    public GreetingSerdes() {}
    
    @Override
    public Serializer<Greeting> serializer() {
        return new GreetingSerializer( getObjectMapper() );
    }

    @Override
    public Deserializer<Greeting> deserializer() {
        return new GreetingDeserializer( getObjectMapper() );
    }
}
