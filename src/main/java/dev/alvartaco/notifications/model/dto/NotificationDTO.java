package dev.alvartaco.notifications.model.dto;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Integer notificationId;
    private Integer messageId;
    private Short messageCategoryId;
    private Integer userId;
    private String notificationChannelType;
    private String notificationStatus;
    private LocalDateTime notificationCreatedOn;
    private LocalDateTime notificationUpdatedOn;
    private Short notificationRetryNumber;

    public NotificationDTO(Integer notificationId,
                           Integer messageId,
                           Short messageCategoryId,
                           Integer userId,
                           String notificationChannelType,
                           String notificationStatus,
                           LocalDateTime notificationCreatedOn,
                           LocalDateTime notificationUpdatedOn,
                           Short notificationRetryNumber) {
        this.notificationId = notificationId;
        this.messageId = messageId;
        this.messageCategoryId = messageCategoryId;
        this.userId = userId;
        this.notificationChannelType = notificationChannelType;
        this.notificationStatus = notificationStatus;
        this.notificationCreatedOn = notificationCreatedOn;
        this.notificationUpdatedOn = notificationUpdatedOn;
        this.notificationRetryNumber = notificationRetryNumber;
    }

    public @NotEmpty String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(@NotEmpty String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Short getMessageCategoryId() {
        return messageCategoryId;
    }

    public void setMessageCategoryId(Short messageCategoryId) {
        this.messageCategoryId = messageCategoryId;
    }

    public String getNotificationChannelType() {
        return notificationChannelType;
    }

    public void setNotificationChannelType(String notificationChannelType) {
        this.notificationChannelType = notificationChannelType;
    }

    public LocalDateTime getNotificationCreatedOn() {
        return notificationCreatedOn;
    }

    public void setNotificationCreatedOn(LocalDateTime notificationCreatedOn) {
        this.notificationCreatedOn = notificationCreatedOn;
    }

    public LocalDateTime getNotificationUpdatedOn() {
        return notificationUpdatedOn;
    }

    public void setNotificationUpdatedOn(LocalDateTime notificationUpdatedOn) {
        this.notificationUpdatedOn = notificationUpdatedOn;
    }

    public Short getNotificationRetryNumber() {
        return notificationRetryNumber;
    }

    public void setNotificationRetryNumber(Short notificationRetryNumber) {
        this.notificationRetryNumber = notificationRetryNumber;
    }

    public Integer getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Integer notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
