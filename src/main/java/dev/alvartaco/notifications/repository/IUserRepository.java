package dev.alvartaco.notifications.repository;

import dev.alvartaco.notifications.model.User;

import java.util.List;

public interface IUserRepository {
    List<User> findAll();
}
