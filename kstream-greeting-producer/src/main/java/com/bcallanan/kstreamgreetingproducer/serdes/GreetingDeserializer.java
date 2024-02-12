/**
 * 
 */
package com.bcallanan.kstreamgreetingproducer.serdes;

import org.apache.kafka.common.serialization.Deserializer;

import com.bcallanan.kstreamgreetingproducer.domain.Greeting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
//@Slf4j
public class GreetingDeserializer implements Deserializer< Greeting > {

    private ObjectMapper objectMapper;
    
    /**
     * @param objectMapper
     */
    public GreetingDeserializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Greeting deserialize(String topic, byte[] data) {
        // TODO Auto-generated method stub
        try {
            return objectMapper.readValue( data, Greeting.class );
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
        // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
}
