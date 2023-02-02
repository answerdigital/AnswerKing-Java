package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.ProductTestBuilder;
import com.answerdigital.answerking.builder.TagRequestTestBuilder;
import com.answerdigital.answerking.builder.TagTestBuilder;
import com.answerdigital.answerking.exception.generic.NotFoundException;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Tag;
import com.answerdigital.answerking.repository.TagRepository;
import com.answerdigital.answerking.request.TagRequest;
import com.answerdigital.answerking.response.TagResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    @InjectMocks
    private TagService tagService;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ProductService productService;

    private final TagTestBuilder tagTestBuilder;

    private final TagRequestTestBuilder tagRequestTestBuilder;

    private final ProductTestBuilder productTestBuilder;

    private static final Long NONEXISTENT_PRODUCT_ID = 2L;

    public TagServiceTest() {
        tagTestBuilder = new TagTestBuilder();
        tagRequestTestBuilder = new TagRequestTestBuilder();
        productTestBuilder = new ProductTestBuilder();
    }

    /* TODO
        Add tag without product returns product object successfully
        Add tag with product returns product object successfully
        Add tag with nonexistent product throws Exception
        Get all tags returns set of tags
     */

    @Test
    void addTagWithoutProductReturnsTagObject() {
        // given
        final TagRequest tagRequest = tagRequestTestBuilder.withDefaultValues()
            .withDefaultValues()
            .build();
        final Tag expectedResponse = tagTestBuilder
            .withDefaultValues()
            .build();

        // when
        doReturn(List.of())
            .when(productService)
            .findAllProductsInListOfIds(anyList());
        doReturn(expectedResponse)
            .when(tagRepository)
            .save(any(Tag.class));

        final TagResponse tagResponse = tagService.addTag(tagRequest);

        // then
        assertTagVsTagResponseEquality(expectedResponse, tagResponse);
    }

    @Test
    void addTagWithProductReturnsTagObject() {
        // given
        final Product product = productTestBuilder.withDefaultValues().build();
        final TagRequest tagRequest = tagRequestTestBuilder.withDefaultValues()
                .withProductId(product.getId())
                .build();
        final Tag expectedResponse = tagTestBuilder.withDefaultValues().withProduct(product).build();

        // when
        doReturn(List.of(product))
            .when(productService)
            .findAllProductsInListOfIds(anyList());
        doReturn(expectedResponse)
            .when(tagRepository)
            .save(any(Tag.class));

        final TagResponse tagResponse = tagService.addTag(tagRequest);

        // then
        assertTagVsTagResponseEquality(expectedResponse, tagResponse);
    }

    @Test
    void addTagWithNonExistentProductThrowsNotFoundException() {
        // given
        final TagRequest tagRequest = tagRequestTestBuilder
            .withDefaultValues()
            .withProductId(NONEXISTENT_PRODUCT_ID)
            .build();

        // when
        doThrow(NotFoundException.class)
            .when(productService)
            .findAllProductsInListOfIds(anyList());

        // then
        assertThrows(NotFoundException.class, () -> tagService.addTag(tagRequest));
    }

    /**
     * Helper method which asserts the equality of a Tag against a TagResponse object.
     * @param tag The Tag to compare.
     * @param response The TagResponse to compare.
     */
    private void assertTagVsTagResponseEquality(final Tag tag, final TagResponse response) {
        assertAll("Tag vs TagResponse Equality",
                () -> assertEquals(tag.getId(), response.getId()),
                () -> assertEquals(tag.getName(), response.getName()),
                () -> assertEquals(tag.getDescription(), response.getDescription()),
                () -> {
                    final List<Long> tagIds = tag.getProducts().stream()
                            .map(Product::getId)
                            .toList();

                    assertEquals(tagIds, response.getProductIds());
                }
        );
    }
}
