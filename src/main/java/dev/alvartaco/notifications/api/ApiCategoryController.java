package dev.alvartaco.notifications.api;

import dev.alvartaco.notifications.model.dto.CategoryDTO;
import dev.alvartaco.notifications.exception.CategoryException;
import dev.alvartaco.notifications.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Rest Controller intended for testing,
 * to view in the browser the List of loaded categories
 */
@RestController
public class ApiCategoryController {

    private static final Logger log = LoggerFactory.getLogger(ApiCategoryController.class);
    private final CategoryService categoryService;
    public ApiCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/categories")
    List<CategoryDTO> getAllByCategoryNameAsc() throws CategoryException {
        log.info("#NOTIFICATIONS-D-C - INSIDE /categories");
        return categoryService.getAllCategoryDTOsByCategoryNameAsc();
    }

    @GetMapping("/api/categories/{categoryId}")
    CategoryDTO getCategoryByCategoryId(@PathVariable short categoryId) throws CategoryException {
        log.info("#NOTIFICATIONS-D-C - INSIDE /categories/{categoryId}");
        return categoryService.getCategoryDTOByCategoryId((short) categoryId);
    }
}
