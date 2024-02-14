/**
 * 
 */
package com.bcallanan.kstreamproducer.serdes;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
//@Slf4j
public class GenericJSONSerializer< T > implements Serializer< T > {

    private ObjectMapper objectMapper;
    
    /**
     * @param objectMapper
     */
    public GenericJSONSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public byte[] serialize(String topic, T data) {
        // TODO Auto-generated method stub
        try {
            return objectMapper.writeValueAsBytes( data );
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
