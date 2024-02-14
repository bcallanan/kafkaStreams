/**
 * 
 */
package com.bcallanan.kstreamproducer;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bcallanan.kstreamproducer.topology.KStreamTopology;

public class KafkaStreamApp {

    private static final Logger log = LoggerFactory.getLogger(KafkaStreamApp.class);

    public void createTopics( Properties configProps,
            List<String> greetingTopics) {
        
        AdminClient client = AdminClient.create( configProps );
        int partitions = 1;
        short replication = 1;
        
        List< NewTopic>  newTopics = greetingTopics
                .stream()
                .map(  topic -> {
                    return  new NewTopic( topic, partitions, replication);
                })
                .collect( Collectors.toList());
        
        CreateTopicsResult topicResults = client.createTopics( newTopics );
        try {
            topicResults.all().get(); // blocking sync call
            log.info( "Topics successful: {}", topicResults.toString());
        } catch ( Exception e ) {
            log.error( "Exception topics failed: {}", e.getMessage(), e);
        }
    }
    /**
     * @param args
     */
    public static void main(String[] args) {

        Properties props = new Properties();
        
        // This first property is essentially the bookmark of reading the topics in the
        // stream. Or basically the 'leave-of-point' from a restart. It is equal to the
        // consumer-groups used for tracking the consumer bookmark 
        props.put( StreamsConfig.APPLICATION_ID_CONFIG, "greeting-app");
        props.put( StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.99.108:39092");
        props.put( ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        
        props.put( StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        props.put( StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        
        KafkaStreamApp app = new KafkaStreamApp();
        app.createTopics( props,
                Arrays.asList( KStreamTopology.GREETINGS, KStreamTopology.GREETINGS_UPPERCASE, KStreamTopology.GREETINGS_MERGED));
        
        Topology topology = KStreamTopology.buildSerdeTopologyWithGenerics();
        
        KafkaStreams kafkaStreams = new KafkaStreams( topology, props );
        try {
            kafkaStreams.start();
            // These are the commands for the docker images:
            // [appuser@broker ~]$ kafka-console-producer --bootstrap-server broker1:9092 --topic greetings
            // [appuser@broker ~]$ kafka-console-consumer --bootstrap-server broker:9092 --topic greeting-uppercase --from-beginning
            //
            // output from this app.
            //
            // [greetings]: null, hello
            // [greetings]: null, HELLO
            // [greetings]: null, huh
            // [greetings]: null, HUH

        } catch( Exception e) {
            
            System.out.println( "error" + e.getMessage());
            e.printStackTrace();
            log.error( "Start failed: {}", e.getMessage(), e);
        }
        finally {
            Runtime.getRuntime().addShutdownHook( new Thread( kafkaStreams::close ));
        }
    }
}
