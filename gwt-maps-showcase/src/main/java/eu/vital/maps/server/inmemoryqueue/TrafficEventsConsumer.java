package eu.vital.maps.server.inmemoryqueue;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrafficEventsConsumer {
	 
	@Autowired
	private QueueConfig queueConfig;
	
	public String peekTheMostImportantEvent() throws InterruptedException {
	   return queueConfig.blockingQueue().peek();
	}
	
}