/**
 * 
 */
package com.bcallanan.kstreamproducer.serdes;

import org.apache.kafka.common.serialization.Deserializer;

import com.bcallanan.kstreamproducer.domain.Greeting;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
//@Slf4j
public class GenericJSONDeserializer< T > implements Deserializer< T > {

    private Class< T > deserializedClassType;
    private ObjectMapper objectMapper;
    
    /**
     * @param objectMapper
     */
    public GenericJSONDeserializer( Class< T > deserializedClassType, ObjectMapper objectMapper ) {
        this.deserializedClassType = deserializedClassType;
        this.objectMapper = objectMapper;
    }

    @Override
    public T deserialize(String topic, byte[] data) {

        if ( data != null ) {
            try {
                return objectMapper.readValue( data, deserializedClassType );
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        return null;
    }
}
