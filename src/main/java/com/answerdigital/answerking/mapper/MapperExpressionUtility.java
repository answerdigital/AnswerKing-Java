package com.answerdigital.answerking.mapper;

import com.answerdigital.answerking.model.Category;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Tag;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class MapperExpressionUtility {
    private MapperExpressionUtility() {}

    public static List<Long> mapProductIdsFromCategory(final Category category) {
        return category.getProducts().stream()
                .map(Product::getId)
                .toList();
    }

    public static List<Long> mapProductIdsFromTag(final Tag tag) {
        return tag.getProducts().stream()
                .map(Product::getId)
                .toList();
    }

    public static Set<Long> mapTagIdsFromProduct(final Product product) {
        return product.getTags().stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());
    }
}
