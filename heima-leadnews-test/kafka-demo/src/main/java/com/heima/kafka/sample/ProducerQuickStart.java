package com.heima.kafka.sample;

import com.sun.corba.se.impl.encoding.CDROutputObject;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.streams.StreamsConfig;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class ProducerQuickStart {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.kafka连接配置信息
        Properties prop=new Properties();
        prop.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.88.133:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,"org.apache.kafka.common.serialization.StringSerializer");
        //2.创建kafka生产对象
        KafkaProducer<String,String> producer=new KafkaProducer<String,String>(prop);
        //3.发送消息
        for (int i = 0; i <10 ; i++) {
            ProducerRecord<String,String> producerRecord=new ProducerRecord<String,String>("itcast-topic-input","hello kafka");
            producer.send(producerRecord);
        }
//        //同步
////        RecordMetadata recordMetadata = producer.send(producerRecord).get();
//        //异步
//        producer.send(producerRecord, new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
//                if(e!=null){
//                    e.printStackTrace();
//                }
//                System.out.println(recordMetadata.offset());
//            }
//        });
////        System.out.println(recordMetadata.offset());
//        //4.关闭消息通道
        producer.close();
    }
}
