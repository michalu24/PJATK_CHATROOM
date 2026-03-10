package org.example.pjatk_chatroom.service;

import org.example.pjatk_chatroom.domain.MessageDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatKafkaListener {

    private final SimpMessagingTemplate ws;
    private final MessageService messageService;

    public ChatKafkaListener(SimpMessagingTemplate ws, MessageService messageService) {
        this.ws = ws;
        this.messageService = messageService;
    }

    @KafkaListener(
            topics = "${chat.kafka.topic}",
            groupId = "${chat.kafka.group-id}",
            containerFactory = "chatKafkaListenerContainerFactory"
    )
    public void onMessage(MessageDto msg) {
        /**
         * 1) Odbieramy wiadomość msg z Kafki.
         * 2) Wywołaj messageService.addMessageWithNormalization(msg)
         *    — dodaje do historii i normalizuje.
         * 3) Wyślij tę wiadomość przez WebSocket na destination "/topic/greetings"
         *    używając ws.convertAndSend("/topic/greetings", msg)
         */
        messageService.addMessageWithNormalization(msg);
        ws.convertAndSend("/topic/greetings", msg);
    }
}
