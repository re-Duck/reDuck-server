package reduck.reduck.domain.tag.serivce;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reduck.reduck.domain.post.entity.Post;
import reduck.reduck.domain.post.entity.TemporaryPost;
import reduck.reduck.domain.post.repository.PostRepository;
import reduck.reduck.domain.post.repository.TemporaryPostRepository;
import reduck.reduck.domain.tag.dto.TagDto;
import reduck.reduck.domain.tag.entity.Tag;
import reduck.reduck.domain.tag.entity.TemporaryTag;
import reduck.reduck.domain.tag.repository.TagRepository;
import reduck.reduck.domain.tag.repository.TemporaryTagRepository;
import reduck.reduck.global.exception.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TagService {
    private final TemporaryPostRepository temporaryPostRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final TemporaryTagRepository temporaryTagRepository;

    public List<TagDto> getTags(String postOriginId) {
        Post post = postRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException());
        List<Tag> tags = tagRepository.findAllByPost(post);
        return tags.stream().map(tag -> TagDto.from(tag)).collect(Collectors.toList());
    }

    public List<TagDto> getTemporaryTags(String postOriginId) {
        TemporaryPost temporaryPost = temporaryPostRepository.findByPostOriginId(postOriginId)
                .orElseThrow(() -> new NotFoundException());
        List<TemporaryTag> tags = temporaryTagRepository.findAllByTemporaryPost(temporaryPost);
        return tags.stream().map(tag -> TagDto.from(tag)).collect(Collectors.toList());
    }
}
