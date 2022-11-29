package com.answerdigital.answerking.response;

import com.answerdigital.answerking.mapper.CategoryMapper;
import com.answerdigital.answerking.mapper.CategoryMapperImpl;
import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CategoryMapperImpl.class, CategoryMapper.class, Product.class, Category.class})
class CategoryResponseTest {

    @Autowired
    CategoryMapper categoryMapper;

    private static final Long CATEGORY_ID = 1L;
    private static final Long PRODUCT_ID = 1L;

    @Test
    void categoryMapperToMapCategoryResponseTest() {

        final var product = Product.builder()
                                           .id(PRODUCT_ID)
                                           .name("Coca cola")
                                           .description("This is a coke")
                                           .price(BigDecimal.valueOf(2.00d))
                                           .retired(true)
                                           .build();
        final var categoryName = "Drinks";
        final var categoryDesc = "Our selection of drinks";

        final var category = Category.builder()
                                              .name(categoryName)
                                              .description(categoryDesc)
                                              .id(CATEGORY_ID)
                                              .products(new HashSet<>())
                                              .build();
        category.addProduct(product);

        final var categoryResponse = categoryMapper.convertCategoryEntityToCategoryResponse(category);

        assertNotNull(categoryResponse);
        assertEquals(PRODUCT_ID, categoryResponse.getProducts().get(0));
        assertEquals(CATEGORY_ID, categoryResponse.getId());
        assertEquals(categoryName, categoryResponse.getName());
        assertEquals(categoryDesc, categoryResponse.getDescription());
    }

}