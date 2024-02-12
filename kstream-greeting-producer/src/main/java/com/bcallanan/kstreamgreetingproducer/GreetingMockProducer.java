/**
 * 
 */
package com.bcallanan.kstreamgreetingproducer;

import java.time.LocalDateTime;
import java.util.List;

import com.bcallanan.kstreamgreetingproducer.domain.Greeting;
import com.bcallanan.kstreamgreetingproducer.topology.KStreamTopology;
import com.bcallanan.kstreamgreetingproducer.util.ProducerMessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
public class GreetingMockProducer {

    public static String KAFKA_BOOT_SERVER = "192.168.99.108:39092";
    
    private static void greetings1(ObjectMapper objectMapper) {
        List<Greeting> greetings1 = List.of(
                //    new Greeting("Transient Error", LocalDateTime.now()),
                new Greeting("Hello, Good Morning!", LocalDateTime.now())
                //                    new Greeting("Hello, Good Evening!", LocalDateTime.now()),
                //                    new Greeting("Hello, Good Night!", LocalDateTime.now())
            );
        greetings1
        .forEach(greeting -> {
            try {
                var greetingJSON = objectMapper.writeValueAsString(greeting);
                var recordMetaData = ProducerMessageUtil.
                        publishMessageSync(KStreamTopology.GREETINGS1, "greet", greetingJSON);
                            
                log.info("Published the alphabet message : {} ", recordMetaData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void greetings2(ObjectMapper objectMapper) {
        var greetings2 = List.of(
                new Greeting("¡Hola buenos dias!", LocalDateTime.now()),
                new Greeting("¡Hola buenas tardes!", LocalDateTime.now()),
                new Greeting("¡Hola, buenas noches!", LocalDateTime.now())
                );
        
        greetings2
        .forEach(greeting -> {
            try {
                var greetingJSON = objectMapper.writeValueAsString(greeting);
                var recordMetaData = ProducerMessageUtil.
                        publishMessageSync( KStreamTopology.GREETINGS2, "greet", greetingJSON);
                            
                log.info("Published the alphabet message : {} ", recordMetaData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        //greetings1(objectMapper);
        greetings2(objectMapper);

    }
}
