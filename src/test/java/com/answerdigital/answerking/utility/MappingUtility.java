package com.answerdigital.answerking.utility;

import com.answerdigital.answerking.mapper.OrderMapper;
import com.answerdigital.answerking.mapper.TagMapper;
import com.answerdigital.answerking.model.Order;
import com.answerdigital.answerking.model.Tag;
import com.answerdigital.answerking.response.OrderResponse;
import com.answerdigital.answerking.response.TagResponse;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

public final class MappingUtility {
    private static final TagMapper TAG_MAPPER = Mappers.getMapper(TagMapper.class);

    private static final OrderMapper ORDER_MAPPER = Mappers.getMapper(OrderMapper.class);

    /**
     * Takes <i>n</i> arguments of type Tag and converts them to a TagResponse.
     * @param tags A Collection of type {@link com.answerdigital.answerking.model.Tag}.
     * @return A List of type {@link com.answerdigital.answerking.response.TagResponse}.
     */
    public static List<TagResponse> allTagsToResponse(final Tag ...tags) {
        return Arrays.stream(tags)
            .map(TAG_MAPPER::convertTagEntityToTagResponse)
            .toList();
    }

    /**
     * Takes an Order and converts it to and OrderResponse.
     * @param order An instance of an {@link com.answerdigital.answerking.model.Order}.
     * @return An {@link com.answerdigital.answerking.response.OrderResponse}.
     */
    public static OrderResponse orderToResponse(final Order order) {
        return ORDER_MAPPER.orderToOrderResponse(order);
    }
}
