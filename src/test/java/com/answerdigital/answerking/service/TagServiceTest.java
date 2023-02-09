package com.answerdigital.answerking.service;

import com.answerdigital.answerking.builder.product.ProductTestBuilder;
import com.answerdigital.answerking.builder.tag.TagRequestTestBuilder;
import com.answerdigital.answerking.builder.tag.TagTestBuilder;
import com.answerdigital.answerking.exception.custom.NameUnavailableException;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static com.answerdigital.answerking.utility.MappingUtility.allTagsToResponse;

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
        assertTagVsResponseEquality(expectedResponse, tagResponse);
        verify(productService).findAllProductsInListOfIds(anyList());
        verify(tagRepository).save(any(Tag.class));
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
        assertTagVsResponseEquality(expectedResponse, tagResponse);
        verify(productService).findAllProductsInListOfIds(anyList());
        verify(tagRepository).save(any(Tag.class));
    }

    @Test
    void addTagWhenTagAlreadyExistsThrowsNameUnavailableException() {
        // given
        final Product product = productTestBuilder
            .withDefaultValues()
            .build();
        final TagRequest tagRequest = tagRequestTestBuilder
            .withDefaultValues()
            .withProductId(product.getId())
            .build();

        // when
        doReturn(true)
            .when(tagRepository)
            .existsByName(anyString());

        // then
        assertThrows(NameUnavailableException.class, () -> tagService.addTag(tagRequest));
        verify(tagRepository).existsByName(anyString());
    }

    @Test
    void findAllTagsWithNoTagsReturnEmptyList() {
        // when
        doReturn(Set.of())
            .when(tagRepository)
            .findAll();

        final List<TagResponse> response = tagService.findAll();

        // then
        assertEquals(0, response.size());
        verify(tagRepository).findAll();
    }

    @Test
    void findAllTagsReturnListOfTags() {
        // given
        final Tag tagOne = new TagTestBuilder()
            .withDefaultValues()
            .build();
        final Tag tagTwo = new TagTestBuilder()
            .withDefaultValues()
            .withId(2L)
            .withName("Low Calorie")
            .withDescription("This product is low in calories.")
            .build();
        final Set<Tag> tags = Set.of(tagOne, tagTwo);

        // when
        doReturn(tags)
            .when(tagRepository)
            .findAll();

        final List<TagResponse> response = tagService.findAll();

        // then
        assertEquals(2, response.size());
        assertTrue(response.containsAll(allTagsToResponse(tagOne, tagTwo)));
        verify(tagRepository).findAll();
    }

    /**
     * Helper method which asserts the equality of a Tag against a TagResponse object.
     * @param tag The Tag to compare.
     * @param response The TagResponse to compare.
     */
    private void assertTagVsResponseEquality(final Tag tag, final TagResponse response) {
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
