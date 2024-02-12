/**
 * 
 */
package com.bcallanan.kstreamgreetingproducer.serdes;

import org.apache.kafka.common.serialization.Serde;

import com.bcallanan.kstreamgreetingproducer.domain.Greeting;

/**
 * 
 */
//@Slf4j
public class SerdesFactory {

    public static Serde<Greeting> greetingSerdesFactoryBuilder() {
        
        return new GreetingSerdes();
    }
}
