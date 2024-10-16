package dev.alvartaco.notifications.model.dto;

/**
 * DTO Created to be used in REST API
 */
public class MessageDTO {

    private String categoryId;
    private String messageBody;

    public MessageDTO(String categoryId, String messageBody) {
        this.categoryId = categoryId;
        this.messageBody = messageBody;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
