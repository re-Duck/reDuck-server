package reduck.reduck.domain.tag.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reduck.reduck.domain.tag.dto.TagDto;
import reduck.reduck.domain.tag.serivce.TagService;
import reduck.reduck.global.entity.Response;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @GetMapping("/post/{postOriginId}")
    public ResponseEntity<Response<List<TagDto>>> getTags(
            @PathVariable("postOriginId") String postOriginId
    ) {
        List<TagDto> result = tagService.getTags(postOriginId);
        return new ResponseEntity(Response.successResponse(result), HttpStatus.OK);
    }

    @GetMapping("/temporary-post/{postOriginId}")
    public ResponseEntity<Response<List<TagDto>>> getTemporaryTags(
            @PathVariable("postOriginId") String postOriginId
    ) {
        List<TagDto> result = tagService.getTemporaryTags(postOriginId);
        return new ResponseEntity(Response.successResponse(result), HttpStatus.OK);
    }
}
