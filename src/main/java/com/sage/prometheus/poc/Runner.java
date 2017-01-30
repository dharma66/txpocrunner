package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.Instant;
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
            RunnerApplication.messages.put(uuid, Instant.now());
            String message = String.format("{ \"requestId\" : \"%s\", \"numTransactions\" : \"%d\" }", uuid, 1000);
            rabbitTemplate.convertAndSend(RunnerApplication.postQueueName, message);
        }

        long timeToPost = System.currentTimeMillis() - start;

        start = System.currentTimeMillis();
        while(RunnerApplication.messagesProcessed.get() < numRequests)
        {
            Thread.sleep(10);
        }

        long completionTime = System.currentTimeMillis() - start;
        long processingTime = completionTime - timeToPost;

        System.out.println("***********************************");
        System.out.println("***********************************");
        System.out.println("");
        System.out.println("Messages Posted: " + numRequests);
        System.out.println("");
        System.out.println("Time to post messages: " + timeToPost + "ms");
        System.out.println("Total time to process: " + processingTime + "ms");
        System.out.println("");
        System.out.println("Average time per message: " + (processingTime/numRequests) + "ms");
        System.out.println("Messages per second: " + numRequests / (processingTime/1000));
        System.out.println("");
        System.out.println("***********************************");
        System.out.println("***********************************");
        System.out.println("");
    }
}
