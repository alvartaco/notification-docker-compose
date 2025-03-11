package dev.alvartaco.notifications.repository.secure;

import dev.alvartaco.notifications.model.secure.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface IUserRepository extends MongoRepository<User,String> {
    @Query("{email :?0}")
    User findByEmail(String email);
}