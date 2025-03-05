package dev.alvartaco.notifications.service.secure;

import dev.alvartaco.notifications.model.secure.User;

import java.util.List;

public interface IUserService {

    public List<User> getAllUser();

    public User findUserProfileByJwt(String jwt);

    public User findUserByEmail(String email);

    public User findUserById(String userId);

    public List<User> findAllUsers();
}
