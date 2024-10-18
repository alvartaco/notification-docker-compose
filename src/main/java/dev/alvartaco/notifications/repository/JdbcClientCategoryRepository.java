package dev.alvartaco.notifications.repository;

import dev.alvartaco.notifications.exception.CategoryException;
import dev.alvartaco.notifications.model.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository of DataBase Category Records
 * JDBC Client
 */
@Repository
public class JdbcClientCategoryRepository implements ICategoryRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcClientCategoryRepository.class);
    private final JdbcClient jdbcClient;
    public JdbcClientCategoryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    /**
     * It returns al Database Categories order by name asc
     * @return List of Category
     */
    public List<Category> findAllByCategoryNameAsc() throws CategoryException {
        // TESTED //
        try {
            return jdbcClient.sql("select * from category order by category_name asc")
                    .query(Category.class)
                    .list();
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - List<Category> findAllByCategoryNameAsc() ");
            throw new CategoryException(e.toString());
        }
    }

    /**
     * Executes the SQL to get a Category by its categoryId
     * @param categoryId The id of the requested Category
     * @return Optional of Category
     */
    public Optional<Category> findByCategoryId(Short categoryId) throws CategoryException {
        // TESTED //
        try {
            return jdbcClient.sql("SELECT category_id, category_name FROM category WHERE category_id = :categoryId" )
                    .param("categoryId", categoryId)
                    .query(Category.class)
                    .optional();
        } catch (Exception e) {
            log.error("#NOTIFICATIONS-D-C - Optional<Category> findByCategoryId() ");
            throw new CategoryException(e.toString());
        }
    }
}
