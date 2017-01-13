package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

@Component
public class Runner implements CommandLineRunner
{
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private final RabbitTemplate rabbitTemplate;

    public Runner(RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception
    {
        int numRequests = 100;
        logger.info("\n\n\n\nPosting " + numRequests + " messages");

        long start = System.currentTimeMillis();
        for(int i = 0; i < numRequests; i++)
        {
            String uuid = UUID.randomUUID().toString();
            RunnerApplication.messages.put(uuid, new Long(System.currentTimeMillis()));
            rabbitTemplate.convertAndSend(RunnerApplication.postQueueName, uuid);
            logger.info("Posted message:    " + uuid);
        }

        logger.info("Messages posted in " + (System.currentTimeMillis() - start) + "ms");
    }
}
