package dev.alvartaco.notifications.repository;

import dev.alvartaco.notifications.exception.MessageException;
import dev.alvartaco.notifications.model.Message;
import jakarta.validation.Valid;

/**
 * Interface to handle different types of Message Repositories
 */
public interface IMessageRepository {

        Integer create(@Valid Message message) throws MessageException;
        
}
