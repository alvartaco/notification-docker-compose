package dev.alvartaco.notifications.service;

import dev.alvartaco.notifications.model.dto.CategoryDTO;
import dev.alvartaco.notifications.exception.CategoryException;
import dev.alvartaco.notifications.model.Category;
import dev.alvartaco.notifications.repository.ICategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);
    private final ICategoryRepository iCategoryRepository;
    public CategoryService(@Qualifier("jdbcClientCategoryRepository") ICategoryRepository iCategoryRepository) {
        this.iCategoryRepository = iCategoryRepository;
    }

    /**
     * Returns a CategoryDTO
     */
    public CategoryDTO getCategoryDTOByCategoryId(Short categoryId ) throws CategoryException {
        return mapToDTO(iCategoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found")));
    }

    /**
     * Returns a Category
     */
    public Category getCategoryByCategoryId(Short categoryId ) throws CategoryException {
        return iCategoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new CategoryException("Category not found"));
    }

    /**
     *  Returns a list of CategoryDTO
     */
    public List<CategoryDTO> getAllCategoryDTOsByCategoryNameAsc() throws CategoryException {
        try {
            List<CategoryDTO> categoriesDTO = new ArrayList<>();
            for (Category category : iCategoryRepository.findAllByCategoryNameAsc()) {
                categoriesDTO.add(mapToDTO(category));
            }
            log.info("#NOTIFICATIONS-D-C - OK getAllCategoryDTOsByCategoryNameAsc().");
            return categoriesDTO;
        } catch ( Exception e) {
            log.error("#NOTIFICATIONS-D-C - ERROR getAllCategoryDTOsByCategoryNameAsc().");
            throw new CategoryException("Category not found");
        }
    }

    /**
     * Used to map important fields to CategoryDTO
     * @return CategoryDTO
     */
    public CategoryDTO mapToDTO(Category category) {
        return new CategoryDTO(
                category.getCategoryId(),
                category.getCategoryName()
        );
    }
}
