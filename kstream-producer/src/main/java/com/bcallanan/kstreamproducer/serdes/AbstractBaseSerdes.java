/**
 * 
 */
package com.bcallanan.kstreamproducer.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 
 */
//@Slf4j
public abstract class AbstractBaseSerdes {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule( new JavaTimeModule() )
            .configure( SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    
    /**
     */
    public AbstractBaseSerdes() {}

    /**
     * @param objectMapper the objectMapper to set
     * @return 
     */
    protected ObjectMapper getObjectMapper() {
        return objectMapper;
    }
    
    /**
     * @param objectMapper the objectMapper to set
     * @return 
     */
    protected static ObjectMapper getStaticObjectMapper() {
        return new ObjectMapper()
                .registerModule( new JavaTimeModule() )
                .configure( SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
    }
    
    
}
