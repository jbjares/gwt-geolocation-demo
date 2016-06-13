package eu.vital.maps.server.servlets;

/*
 * #%L
 * GWT Maps API V3 - Showcase
 * %%
 * Copyright (C) 2011 - 2016 GWT Maps API V3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import eu.vital.maps.server.inmemoryqueue.TrafficEventsProducer;


@Component("kafkaEvents")
@Configuration
public class EventsConsumer {
	
	  @Autowired
	  private TrafficEventsProducer trafficEventsProducer;
	  
	  private ConsumerConnector consumer;
	  private String topic;
	  
	  public EventsConsumer()
	  {
	    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
	            createConsumerConfig());
	    this.topic = "events";
	  }
	  private static ConsumerConfig createConsumerConfig()
	  {
	    Properties props = new Properties();
	    props.put("zookeeper.connect", "localhost:2181");
	    props.put("group.id", "test");
	    props.put("zookeeper.session.timeout.ms", "400");
	    props.put("zookeeper.sync.time.ms", "200");
	    props.put("auto.commit.interval.ms", "1000");
	    return new ConsumerConfig(props);
	  }
	 
	  @PostConstruct
	  public void run() throws InterruptedException {
	    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
	    topicCountMap.put(topic, new Integer(1));
	    Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
	    KafkaStream<byte[], byte[]> stream =  consumerMap.get(topic).get(0);
	    ConsumerIterator<byte[], byte[]> it = stream.iterator();
	    while(it.hasNext()){
	    	trafficEventsProducer.produceMyObject(new String(it.next().message()));
	    }
	      //System.out.println(new String(it.next().message()));
	  }
	
	
}
