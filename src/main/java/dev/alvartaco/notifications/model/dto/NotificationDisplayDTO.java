package dev.alvartaco.notifications.model.dto;

public class NotificationDisplayDTO {

    private Integer notificationId;
    private Integer messageId;
    private String messageCategoryName;
    private Short categoryId;
    private String userName;
    private Integer userId;
    private String channelType;
    private String status;
    private String createdOn;
    private String updatedOn;
    private Short retryNumber;

    public NotificationDisplayDTO(
                             Integer notificationId,
                             Integer messageId,
                             String messageCategoryName,
                             Short categoryId,
                             String userName,
                             Integer userId,
                             String channelType,
                             String status,
                             String createdOn,
                             String updatedOn,
                             Short retryNumber
    ) {
        this.notificationId = notificationId;
        this.messageId = messageId;
        this.messageCategoryName = messageCategoryName;
        this.categoryId = categoryId;
        this.userName = userName;
        this.userId = userId;
        this.channelType = channelType;
        this.status = status;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.retryNumber =  retryNumber;
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

    public String getMessageCategoryName() {
        return messageCategoryName;
    }

    public void setMessageCategoryName(String messageCategoryName) {
        this.messageCategoryName = messageCategoryName;
    }

    public Short getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Short categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Short getRetryNumber() {
        return retryNumber;
    }

    public void setRetryNumber(Short retryNumber) {
        this.retryNumber = retryNumber;
    }
}
