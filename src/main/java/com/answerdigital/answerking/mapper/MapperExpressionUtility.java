package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Tag;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class MapperExpressionUtility {

    private MapperExpressionUtility() {
    }

    public static List<Long> mapProductIdsFromCategory(final Category category) {
        return category.getProducts().stream()
                .map(Product::getId)
                .toList();
    }

    public static List<Long> mapProductIdsFromTag(final Tag tag) {
        final List<Product> products = tag.getProducts();

        return products.isEmpty()
                ? Collections.emptyList()
                : products.stream()
                .map(Product::getId)
                .toList();
    }

    public static Set<Long> mapTagIdsFromProduct(final Product product) {
        final Set<Tag> tags = product.getTags();

        return tags.isEmpty()
                ? Collections.emptySet()
                : tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
    }

}
