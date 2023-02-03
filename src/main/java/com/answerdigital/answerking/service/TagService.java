package com.answerdigital.answerking.service;

import com.answerdigital.answerking.mapper.TagMapper;
import com.answerdigital.answerking.model.Product;
import com.answerdigital.answerking.model.Tag;
import com.answerdigital.answerking.repository.TagRepository;
import com.answerdigital.answerking.request.TagRequest;
import com.answerdigital.answerking.response.TagResponse;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.TAGS_ALREADY_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.getCustomException;

@Service
public class TagService {
    private final TagRepository tagRepository;

    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    private final ProductService productService;

    @Autowired
    public TagService(final TagRepository tagRepository, final ProductService productService) {
        this.tagRepository = tagRepository;
        this.productService = productService;
    }

    public TagResponse addTag(final TagRequest tagRequest) {
        if (tagRepository.existsByName(tagRequest.name())) {
            throw getCustomException(TAGS_ALREADY_EXIST, tagRequest.name());
        }

        final List<Product> products = productService.findAllProductsInListOfIds(tagRequest.productIds());

        final Tag newTag = tagMapper.addRequestToTag(tagRequest);
        newTag.setProducts(products);

        final Tag savedTag = tagRepository.save(newTag);
        return tagMapper.convertTagEntityToTagResponse(savedTag);
    }

    public List<TagResponse> findAll() {
        final Set<Tag> tagsList = tagRepository.findAll();

        return tagsList.stream()
            .map(tagMapper::convertTagEntityToTagResponse)
            .toList();
    }
}
