package dev.alvartaco.notifications.service;

interface IMessageService {

    void notify(String categoryId, String messageBody, String messageCreatorId) throws Exception;

}
