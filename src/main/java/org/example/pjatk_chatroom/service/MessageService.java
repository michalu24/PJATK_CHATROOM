package org.example.pjatk_chatroom.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import org.example.pjatk_chatroom.domain.Message;
import org.example.pjatk_chatroom.domain.MessageDto;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private static final int MAX_HISTORY_SIZE = 200;
    private static final String ANON = "Anon";
    private static final String EMPTY_STRING = "";
    private static final int ZERO = 0;

    private final Deque<MessageDto> history = new ArrayDeque<>(MAX_HISTORY_SIZE);

    public void addMessageWithNormalization(MessageDto message) {
        /**
         * 1) Jeśli 'message' == null → zakończ metodę (return).
         * 2) Pobierz 'author' = message.author().
         * 3) Znormalizuj autora: 'author' = normalize(author).
         * 4) Pobierz 'content' = message.content().
         * 5) Przytnij bezpiecznie: 'content' = safeTrim(content).
         * 6) Wejdź w blok synchronized(history).
         * 7)   Jeśli history.size() >= MAX_HISTORY_SIZE → usuń najstarszy element: history.removeFirst().
         * 8)   Dodaj nową wiadomość na koniec: history.addLast(new MessageDto(author, content)).
         * 9) Wyjdź z bloku synchronized.
         */
        if (message == null) return;
        String author = message.author();
        author = normalize(author);
        String content = message.content();
        content = safeTrim(content);
        synchronized (history) {
            if (history.size() >= MAX_HISTORY_SIZE) {
                history.removeFirst();
            }
            history.addLast(new MessageDto(author, content));
        }
    }

    public List<Message> lastForActiveUser(int limit, String currentUser) {
        /**
         * 1) Pobierz ostatnie wiadomości DTO: List<MessageDto> dtos = lastMessagesForLimit(limit).
         * 2) Dla każdego MessageDto m:
         * 3)   Zamień na widok: Message vm = toViewModel(m, currentUser).
         * 4) Zbierz wyniki do niezmienialnej listy i zwróć.
         */
        List<MessageDto> dtos = lastMessagesForLimit(limit);
        List<Message> list = new ArrayList<>();
        for (MessageDto m : dtos) {
            Message vm = toViewModel(m, currentUser);
            list.add(vm);
        }
        return List.copyOf(list);
    }

    private List<MessageDto> lastMessagesForLimit(int limit) {
        /**
         * 1) Wejdź w blok synchronized(history).
         * 2) Ustal 'size' = history.size().
         * 3) Ustal jaką liczbę mamy zwrócić(numberOfMessages) = min(max(ZERO, limit), size).
         * 4) Jeśli numberOfMessages == ZERO → zwróć pustą listę.
         * 5) Utwórz migawkę: MessageDto[] snapshot = history.toArray(new MessageDto[size]).
         * 6) Utwórz nową listę wynikową o pojemności 'numberOfMessages'.
         * 7) Skopiuj podlistę z końca snapshot:
         *    od indeksu (size - numberOfMessages) włącznie do indeksu 'size' wyłącznie.
         * 8) Zwróć listę z ostatnimi 'numberOfMessages' elementami.
         */
        synchronized (history) {
            int size = history.size();
            int numberOfMessages = Math.min(Math.max(ZERO, limit), size);
            if (numberOfMessages == ZERO) {
                return Collections.emptyList();
            }
            MessageDto[] snapshot = history.toArray(new MessageDto[size]);
            List<MessageDto> result = new ArrayList<>(numberOfMessages);
            for (int i = size - numberOfMessages; i < size; i++) {
                result.add(snapshot[i]);
            }
            return result;
        }
    }

    private Message toViewModel(MessageDto m, String currentUser) {
        /**
         * 1) Ustal 'mine' = isMineMessage(m, currentUser).
         * 2) Utwórz obiekt widoku: new Message(m.author(), m.content(), mine).
         * 3) Zwróć ten obiekt.
         */
        boolean mine = isMineMessage(m, currentUser);
        return new Message(m.author(), m.content(), mine);
    }

    private static boolean isMineMessage(MessageDto m, String currentUser) {
        /**
         * 1) Jeśli m.author() == null → zwróć false.
         * 2) W przeciwnym razie porównaj bez rozróżniania wielkości liter:
         * 3)   zwróć m.author().equalsIgnoreCase(currentUser).
         */
        if (m.author() == null) return false;
        return m.author().equalsIgnoreCase(currentUser);
    }

    private static String normalize(String s) {
        /**
         * 1) Sprawdź czy String ma treść (nie null/nie pusta/nie same spacje):
         * 2)   jeśli NIE → zwróć stałą ANON.
         * 3)   jeśli TAK → zwróć s.trim().
         */
        if (s == null || s.trim().isEmpty()) {
            return ANON;
        }
        return s.trim();
    }

    private static String safeTrim(String s) {
        /**
         * 1) Jeśli s == null → zwróć stałą EMPTY_STRING.
         * 2) W przeciwnym razie → zwróć s.trim().
         */
        if (s == null) {
            return EMPTY_STRING;
        }
        return s.trim();
    }
}
