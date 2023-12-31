package reduck.reduck.domain.follow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reduck.reduck.domain.follow.Entity.Follow;
import reduck.reduck.domain.follow.dto.FollowRequest;
import reduck.reduck.domain.follow.dto.FollowerResponse;
import reduck.reduck.domain.follow.repository.FollowRepository;
import reduck.reduck.domain.user.entity.User;
import reduck.reduck.domain.user.repository.UserRepository;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.NotFoundException;
import reduck.reduck.util.AuthenticationToken;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Transactional
    public void follow(FollowRequest followDto) {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));

        User followingUser = userRepository.findByUserId(followDto.getUserId())
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));

        Follow follow = Follow.builder()
                .user(user)
                .followingUser(followingUser)
                .build();

        followRepository.save(follow);
    }

    /**
     * 팔로워 목록 조회
     *
     * @return
     */
    public List<FollowerResponse> getFollowers() {
        String userId = AuthenticationToken.getUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));

        List<Follow> followers = followRepository.findAllByFollowingUser(user);
        return followers.stream().map(
                follow -> FollowerResponse.from(follow.getUser())
        ).collect(Collectors.toList());
    }
}
