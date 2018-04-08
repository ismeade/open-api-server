package com.ade.open.api.server.service;

import com.ade.open.api.server.context.Context;
import com.ade.open.api.server.context.ReturnCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liyang on 18-2-2.
 */
@Component
public class RequestSynchronizer {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final Map<String, Context> map = new ConcurrentHashMap<>();

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback((correlationData, b, s) -> {
            if (!b) {
                LOG.error("[send fail] {} | {} ", correlationData, s);
            }
        });
    }

    @Value("${async.time.out}")
    private long timeout;
    @Value("${rabbit.exchange}")
    private String exchange;
    @Value("${rabbit.topic.launch}")
    private String topicLaunch;

    public String send(Context context) {
        if (check(context)) {
            String routeKey = topicLaunch + context.getParameter("service", String.class) + "." + context.getParameter("version", String.class).replaceAll("\\.", "_");
            Message message = MessageBuilder
                    .withBody(context.getRequestBody().getBytes())
                    .setMessageId(context.getId())
                    .setTimestamp(new Date())
                    .build();
            LOG.info("[MQ send] {} | {} | {}", exchange, routeKey, context.getRequestBody());
            try {
                this.rabbitTemplate.convertAndSend(exchange, routeKey, message);
                map.put(context.getId(), context);
                return context.getResponseBody(timeout);
            } catch (Exception e) {
                LOG.error(e.getLocalizedMessage(), e);
            } finally {
                map.remove(context.getId());
            }
        }
        return "";
    }

    private boolean check(Context context) {
        return Objects.nonNull(context) && ReturnCode.success(context.check());
    }

    private void notifyRequest(String id, String body) {
        Context context = map.remove(id);
        if (Objects.nonNull(context)) {
            LOG.info("[match] id = {} body = {}", id, body);
            context.setResponseBody(body);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${rabbit.queue.result}", autoDelete = "true"),
            exchange = @Exchange(value = "${rabbit.exchange}", type = "topic", durable = "true"),
            key = "${rabbit.topic.result}")
    )
    @RabbitHandler
    public void process(@Payload Message message) throws IOException {
        try {
            String messageId = message.getMessageProperties().getMessageId();
            String body = new String(message.getBody(), "UTF-8");
            LOG.info("[MQ receive] {} | {}", messageId, body);
            notifyRequest(messageId, body);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
    }

}
