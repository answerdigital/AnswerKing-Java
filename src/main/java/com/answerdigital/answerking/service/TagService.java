package com.answerdigital.answerking.service;

import com.answerdigital.answerking.exception.custom.NameUnavailableException;
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
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.TAGS_DO_NOT_EXIST;
import static com.answerdigital.answerking.exception.util.GlobalErrorMessage.getCustomException;

@Service
public class TagService {
    private final TagRepository tagRepository;

    private final ProductService productService;

    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

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

    /**
     * <p>Finds a {@link com.answerdigital.answerking.model.Tag} based on its ID, or else if
     * no {@link com.answerdigital.answerking.model.Tag} is found - a
     * {@link com.answerdigital.answerking.exception.generic.NotFoundException} is thrown.</p>
     *
     * <p>This method is to be used internally within the TagService hence why it's access
     * modifier is Private.</p>
     *
     * @param id The ID of the {@link com.answerdigital.answerking.model.Tag}.
     * @return The found {@link com.answerdigital.answerking.model.Tag}.
     */
    private Tag findById(final Long id) {
        return tagRepository.findById(id)
            .orElseThrow(() -> getCustomException(TAGS_DO_NOT_EXIST, id));
    }

    /**
     * <p>Finds a {@link com.answerdigital.answerking.model.Tag} based on its ID, or else if
     * no {@link com.answerdigital.answerking.model.Tag} is found - a
     * {@link com.answerdigital.answerking.exception.generic.NotFoundException} is thrown. If
     * a {@link com.answerdigital.answerking.model.Tag} is found</p>
     *
     * @param id The ID of the {@link com.answerdigital.answerking.model.Tag}.
     * @return The found {@link com.answerdigital.answerking.model.Tag}, converted
     * into a {@link com.answerdigital.answerking.response.TagResponse}.
     */
    public TagResponse findByIdResponse(final Long id) {
        return tagMapper.convertTagEntityToTagResponse(findById(id));
    }

    /**
     * Updates a Tag {@link com.answerdigital.answerking.model.Tag}.
     * @param id The ID of the Tag {@link com.answerdigital.answerking.model.Tag} to update.
     * @param tagRequest The TagRequest object {@link com.answerdigital.answerking.request.TagRequest}.
     * @return The updated Tag {@link com.answerdigital.answerking.model.Tag},
     * in the form of a TagResponse {@link com.answerdigital.answerking.response.TagResponse}.
     * @throws NameUnavailableException When the Tag {@link com.answerdigital.answerking.model.Tag} name already exists.
     */
    public TagResponse updateTag(final Long id, final TagRequest tagRequest) {
        if (tagRepository.existsByNameAndIdIsNot(tagRequest.name(), id)) {
            throw getCustomException(TAGS_ALREADY_EXIST, tagRequest.name());
        }

        final Tag tag = findById(id);
        // TODO BENCH-402 verify that the tag is not retired once tag retirement has been added

        final Tag updatedTag = tagMapper.updateRequestToTag(tag, tagRequest);
        addProductsToTag(updatedTag, tagRequest.productIds());

        return tagMapper.convertTagEntityToTagResponse(tagRepository.save(updatedTag));
    }

    /**
     * Adds a List of Products {@link com.answerdigital.answerking.model.Product}
     * to a given Tag {@link com.answerdigital.answerking.model.Tag}.
     * @param tag The Tag {@link com.answerdigital.answerking.model.Tag} object
     * to add the Products {@link com.answerdigital.answerking.model.Product} to.
     * @param productIds The List of Product {@link com.answerdigital.answerking.model.Product} IDs.
     */
    private void addProductsToTag(final Tag tag, final List<Long> productIds) {
        final List<Product> products = productService.findAllProductsInListOfIds(productIds);
        productService.validateProductsAreNotRetired(products);
        tag.setProducts(products);
    }
}
