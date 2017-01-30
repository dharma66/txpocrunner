package com.sage.prometheus.poc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class Receiver
{
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);

    public void receiveMessage(String response) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        ResponseMessage msg = mapper.readValue(response, ResponseMessage.class);

        Instant ended = Instant.parse(msg.completed);
        RunnerApplication.messages.put(msg.requestId, ended);

        RunnerApplication.messagesProcessed.incrementAndGet();
    }
}
