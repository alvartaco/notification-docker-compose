package dev.alvartaco.notifications.service;

import dev.alvartaco.notifications.model.User;
import dev.alvartaco.notifications.repository.IUserRepository;
import dev.alvartaco.notifications.repository.InMemoryUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService{

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final IUserRepository iUsersRepository;
    public UserService(@Qualifier("inMemoryUserRepository")
                       IUserRepository iUsersRepository) {
        this.iUsersRepository = iUsersRepository;
    }

    public List<User> findAll() {
        log.info("#NOTIFICATIONS-D-C - List<User> findAll()");
        return iUsersRepository.findAll();
    }

    /**
     * it Finds all users that are subscribed to a Message Category
     * @param categoryId
     * @return
     */
    public List<User> getUsersByCategoryId(short categoryId){
        log.info("#NOTIFICATIONS-D-C - List<User> getUsersByCategoryId(short categoryId)");
        return iUsersRepository.findAll().stream()
                .filter(
                        user -> user.userSubscriptions()
                                .stream()
                                .anyMatch(category -> category.getCategoryId() == categoryId )
                )
                .toList();
    }

    /**
     * it finds the channels that a user is subscribed to receive the notifications
     * @param user
     * @return
     */
    public List<String> getUserChannelTypes(User user){
        log.info("#NOTIFICATIONS-D-C - List<String> getUserChannelTypes(User user)");
        return  user.userChannels()
                .stream()
                .toList();
    }

    /**
     * Returns the User by Id
     * @param userId
     * @return
     */
    public User getUsersByUserId(Integer userId){
        log.info("#NOTIFICATIONS-D-C - public User getUsersByUserId(Integer userId)");
        return iUsersRepository.findAll().stream()
                .filter(user -> userId.equals(user.userId()))
                .findAny()
                .orElse(null);

    }
}
