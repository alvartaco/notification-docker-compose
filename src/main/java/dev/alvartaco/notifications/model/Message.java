package dev.alvartaco.notifications.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * records are considered DTO
 * that is because it does not have one
 *
 * @param messageId
 * @param category
 * @param messageBody
 * @param createdOn
 * @param messageCreatorId
 */
public record Message(
        Integer messageId,
        @NotNull
        Category category,
        @NotEmpty
        String messageBody,
        @NotEmpty
        LocalDateTime createdOn,
        @NotEmpty
        String messageCreatorId

) {
}