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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

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

    @Operation(summary = "Get a single tag.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tag with the provided id has been found.",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid parameters are provided.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Tag with the given id does not exist.",
            content = {@Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<TagResponse> getTagById(@Valid @PathVariable final Long id) {
        return new ResponseEntity<>(tagService.findByIdResponse(id), HttpStatus.OK);
    }

    @Operation(summary = "Create a new tag.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "The tag has been created.",
                    content = {
                        @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid parameters are provided.",
                    content = {
                        @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagResponse> addTag(@Valid @RequestBody final TagRequest tagRequest) {
        return new ResponseEntity<>(tagService.addTag(tagRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing tag.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "The Tag has been updated.",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid parameters are provided.",
                content = {
                    @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Tag with the given id does not exist.",
                content = {
                    @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagResponse> updateTag(
            @Valid @PathVariable final Long id,
            @Valid @RequestBody final TagRequest tagRequest
    ) {
        return new ResponseEntity<>(tagService.updateTag(id, tagRequest), HttpStatus.OK);
    }

    @Operation(summary = "Retire an existing tag.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "No Content.",
                content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TagResponse.class))}),
        @ApiResponse(responseCode = "400", description = "Invalid parameters are provided.",
                content = {
                    @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "404", description = "Tag with the given id does not exist.",
                content = {
                    @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))}),
        @ApiResponse(responseCode = "410", description = "Tag with the given id is already retired.",
                content = {
                    @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE})
    public ResponseEntity<TagResponse> retireTagById(@Valid @PathVariable final Long id) {
        tagService.retireTag(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
