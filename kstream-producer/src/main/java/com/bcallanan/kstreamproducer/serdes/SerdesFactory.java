/**
 * 
 */
package com.bcallanan.kstreamproducer.serdes;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

import com.bcallanan.kstreamproducer.domain.Greeting;

/**
 * 
 */
//@Slf4j
public class SerdesFactory extends AbstractBaseSerdes {

    public static Serde<Greeting> greetingSerdesFactoryBuilder() {
        
        return new GreetingSerdes();
    }
    
    public static Serde<Greeting> greetingSerdesUsingGenerics() {
        
        GenericJSONSerializer<Greeting> serializer = 
                new GenericJSONSerializer<Greeting> ( getStaticObjectMapper() );
        
        GenericJSONDeserializer<Greeting> deserializer = 
                new GenericJSONDeserializer<Greeting> ( Greeting.class, getStaticObjectMapper() );
        
        return Serdes.serdeFrom( serializer, deserializer);
    }
}
