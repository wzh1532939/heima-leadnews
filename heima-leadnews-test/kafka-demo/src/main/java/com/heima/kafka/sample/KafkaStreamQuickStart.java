package com.heima.kafka.sample;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * 流式处理
 */
public class KafkaStreamQuickStart {
    public static void main(String[] args) {
        Properties prop=new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.88.133:9092");
        prop.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        prop.put(StreamsConfig.APPLICATION_ID_CONFIG,"streams-quickstart");
        StreamsBuilder streamsBuilder=new StreamsBuilder();
        streamProcessor(streamsBuilder);
        KafkaStreams kafkaStreams=new KafkaStreams(streamsBuilder.build(),prop);
        kafkaStreams.start();
    }

    /**
     * 流式计算
     * @param streamsBuilder
     */
    private static void streamProcessor(StreamsBuilder streamsBuilder) {
        KStream<String, String> stream = streamsBuilder.stream("itcast-topic-input");
        stream.flatMapValues(new ValueMapper<String, Iterable<String>>() {
            @Override
            public Iterable<String> apply(String value) {
                String[] valAry = value.split(" ");
                return Arrays.asList(valAry);
            }
        }).groupBy((key,value)->value)
                . windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                .count()
                .toStream()
                .map((key,val)->{
                    System.out.println("key="+key+"val="+val);
                    return new KeyValue<>(key.key().toString(),val.toString());
                })
                .to("itcast-topic-out");
    }
}
