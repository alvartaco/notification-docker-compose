package dev.alvartaco.notifications.service;

import dev.alvartaco.notifications.model.dto.CategoryDTO;
import dev.alvartaco.notifications.exception.CategoryException;
import dev.alvartaco.notifications.model.Category;
import dev.alvartaco.notifications.repository.JdbcClientCategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private JdbcClientCategoryRepository jdbcClientCategoryRepository;

    // TODO: Uncomment me when mysql  is running @Test
    public void getCategoryDTOByCategoryId() throws CategoryException {

        Short testCategoryId = 1;
        String testCategoryName = "Mock Category DTO Name";
        Category mockCategory = new Category(testCategoryId,
                testCategoryName);

        // Mock the behavior of the repository to return the mock Category
        Mockito.when((jdbcClientCategoryRepository
                        .findByCategoryId(testCategoryId)))
                .thenReturn(Optional.of(mockCategory));

        // Act
        CategoryDTO result =
                categoryService.mapToDTO(
                categoryService.getCategoryByCategoryId(testCategoryId));

        //  Assert Existing categoryId
        assertNotNull(result);
        assertEquals(testCategoryId, result.getCategoryId());
        assertEquals(testCategoryName, result.getCategoryName());
    }

    // TODO: Uncomment me when mysql  is running @Test
    public void getCategoryByCategoryId() throws CategoryException {

        Short testCategoryId = 1;
        String testCategoryName = "Mock Category Name";
        Category mockCategory = new Category(testCategoryId,
                testCategoryName);

        // Mock the behavior of the repository to return the mock Category
        Mockito.when((jdbcClientCategoryRepository
                .findByCategoryId(testCategoryId)))
                .thenReturn(Optional.of(mockCategory));

        // Act
        Category result =
                categoryService.getCategoryByCategoryId(testCategoryId);

        // Assert for  categoryId
        assertNotNull(result);
        assertEquals(testCategoryId, result.getCategoryId());
        assertEquals(testCategoryName, result.getCategoryName());
    }

    /*
    @Test
    public void getAllCategoryDTOsByCategoryNameAsc() throws CategoryException {


        Short testCategoryId = 1;
        String testCategoryName = "Mock Category Name1";
        Category mockCategory1 = new Category(testCategoryId,testCategoryName);
        Short testCategoryId2 = 2;
        String testCategoryName2 = "Mock Category Name2";
        Category mockCategory2 = new Category(testCategoryId2,testCategoryName2);
        List<Category> mockCategories = Arrays.asList(mockCategory1, mockCategory2);
        List<CategoryDTO> mockCategoriesDTOs =
                Arrays.asList(
                        categoryService.mapToDTO(mockCategory1),
                        categoryService.mapToDTO(mockCategory2));

        // Mock the behavior of the repository to return the mock Categories
        Mockito.when((jdbcClientCategoryRepository
                        .findAllByCategoryNameAsc()))
                    .thenReturn(mockCategories);

        List<CategoryDTO> results =
                categoryService.getAllCategoryDTOsByCategoryNameAsc();

        //assertEquals(mockCategoriesDTOs.size(), results.size());
        assertTrue(mockCategoriesDTOs.containsAll(results));
        assertTrue(results.containsAll(mockCategoriesDTOs));

        //assertEquals("Verify same metrics series",
        //        CollectionUtils.getCardinalityMap(mockCategoriesDTOs),
        //        CollectionUtils.getCardinalityMap(mockCategoriesDTOs));

    }
    */
}
