package com.answerdigital.answerking.controller;

import com.answerdigital.answerking.exception.util.ErrorResponse;
import com.answerdigital.answerking.request.TagRequest;
import com.answerdigital.answerking.response.TagResponse;
import com.answerdigital.answerking.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/tags")
@Tag(name = "Tags", description = "Create and manage tags.")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(final TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "Get all tags.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "When all the tags have been returned.",
                    content = {
                        @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TagResponse.class)))}
            )
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return new ResponseEntity<>(tagService.findAll(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new tag.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "When the tag has been created.",
                    content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))}),
        @ApiResponse(responseCode = "400", description = "When invalid parameters are provided.",
                    content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagResponse> addTag(@Valid @RequestBody final TagRequest tagRequest) {
        return new ResponseEntity<>(tagService.addTag(tagRequest), HttpStatus.CREATED);
    }
}
