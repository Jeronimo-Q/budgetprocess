package com.familyspences.budgetprocess.messages;

import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverMessagesBroker {

    private final Gson gson = new Gson();

    @RabbitListener(queues = "${budget.procesar.queue-name}")
    public void receiveMessage(String messageJson) {
        System.out.println("📥 Mensaje recibido (JSON): " + messageJson);

    }
}
