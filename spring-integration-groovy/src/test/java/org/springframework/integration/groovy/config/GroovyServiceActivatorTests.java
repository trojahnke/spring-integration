/*
 * Copyright 2002-2010 the original author or authors.
 *
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
 */

package org.springframework.integration.groovy.config;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.BeanDefinitionParsingException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Mark Fisher
 * @since 2.0
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class GroovyServiceActivatorTests {

	@Autowired
	private MessageChannel referencedScriptInput;

	@Autowired
	private MessageChannel inlineScriptInput;


	@Test
	public void referencedScript() throws Exception{
		QueueChannel replyChannel = new QueueChannel();
		replyChannel.setBeanName("returnAddress");
		for (int i = 1; i <= 3; i++) {
			Message<?> message = MessageBuilder.withPayload("test-" + i).setReplyChannel(replyChannel).build();
			this.referencedScriptInput.send(message);
			Thread.sleep(1000);
		}
		String value1 = (String) replyChannel.receive(0).getPayload();
		String value2 = (String) replyChannel.receive(0).getPayload();
		String value3 = (String) replyChannel.receive(0).getPayload();
		assertTrue(value1.startsWith("groovy-test-1-foo - bar"));
		assertTrue(value2.startsWith("groovy-test-2-foo - bar"));
		assertTrue(value3.startsWith("groovy-test-3-foo - bar"));
		// becouse we are using 'prototype bean the suffix date will be different

		assertFalse(value1.substring(26).equals(value2.substring(26)));
		assertFalse(value2.substring(26).equals(value3.substring(26)));
		
		assertNull(replyChannel.receive(0));
	}

	@Test
	public void inlineScript() throws Exception{
		QueueChannel replyChannel = new QueueChannel();
		replyChannel.setBeanName("returnAddress");
		for (int i = 1; i <= 3; i++) {
			Message<?> message = MessageBuilder.withPayload("test-" + i).setReplyChannel(replyChannel).build();
			this.inlineScriptInput.send(message);
		}
		assertEquals("inline-test-1", replyChannel.receive(0).getPayload());
		assertEquals("inline-test-2", replyChannel.receive(0).getPayload());
		assertEquals("inline-test-3", replyChannel.receive(0).getPayload());
		assertNull(replyChannel.receive(0));
	}
	
	@Test(expected=BeanDefinitionParsingException.class)
	public void inlineScriptAndVariables() throws Exception{
		new ClassPathXmlApplicationContext("GroovyServiceActivatorTests-fail-context.xml", this.getClass());
	}

}
