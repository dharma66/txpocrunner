package com.sage.prometheus.poc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Receiver
{
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    public void receiveMessage(String requestId) throws Exception
    {
        long completionTime = System.currentTimeMillis();

        long startTime = RunnerApplication.messages.get(requestId).longValue();

        logger.info("Message: " + requestId + " - " + (completionTime - startTime) + "ms");
    }
}
