package com.feelfreetocode.kafka.stream;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;

public class StreamsStarterApp {
	public static void main(String[] args) {
		System.out.println("Hello World... from starter");
		
		Properties config= new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-app");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		
		KStreamBuilder builder= new KStreamBuilder();
		
//		1 stream from kafka
		KStream<String , String> wordCountInput =  builder.stream("word-count-input");
		
//		2. map values to lower case 
		KTable<String, Long> wordCounts =  wordCountInput.mapValues(value->value.toLowerCase())
//		3.flatmap value split by space 
		.flatMapValues(value->Arrays.asList(value.split(" ")))
//		setting value as key
		.selectKey((ignoredKey , word)->{
			System.out.println(word);
			return word;
		})
//		group by key to get count
		.groupByKey()
//		count occurence
		.count("counts");
		
//		wordCounts.to("word-count-output");
		wordCounts.to(Serdes.String(), Serdes.Long() , "word-count-output");
		KafkaStreams streams = new KafkaStreams(builder , config);
		
		streams.start();
		System.out.println(streams.toString());
		
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	
		
	}
}



























