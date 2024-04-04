package com.heima.kafka.stream;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;

@Configuration
@Slf4j
public class KafkaStreamHelloListenner {
    @Bean
    public KStream<String,String> KStream(StreamsBuilder stringBuilder){
        KStream<String, String> stream = stringBuilder.stream("itcast-topic-input");
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
        return stream;
    }
}
