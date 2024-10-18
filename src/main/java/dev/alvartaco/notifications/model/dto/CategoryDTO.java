package dev.alvartaco.notifications.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO Created because Categories are no Records
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

    private Short categoryId;
    private String categoryName;

}
