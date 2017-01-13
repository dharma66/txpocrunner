package com.sage.prometheus.poc;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class RunnerApplication
{
	final static String postQueueName = "worker-queue";
	final static String readQueueName = "response-queue";
	final static Map<String, Long> messages = new HashMap<>();

	@Bean
	Queue responseQueue()
	{
		return new Queue(readQueueName, false);
	}

	@Bean
	TopicExchange responseExchange()
	{
		return new TopicExchange("worker-exchange");
	}

	@Bean
	Binding responseBinding(Queue queue, TopicExchange exchange)
	{
		return BindingBuilder.bind(queue).to(exchange).with(readQueueName);
	}

	@Bean
	SimpleMessageListenerContainer responseContainer(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter)
	{
	    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.setQueueNames(readQueueName);
		container.setMessageListener(listenerAdapter);
		container.setConcurrentConsumers(4);

		return container;
	}

	@Bean
	MessageListenerAdapter responseListenerAdapter(Receiver receiver)
	{
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
	public static void main(String[] args) {
		SpringApplication.run(RunnerApplication.class, args);
	}
}
