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
import reduck.reduck.global.exception.errorcode.CommonErrorCode;
import reduck.reduck.global.exception.errorcode.UserErrorCode;
import reduck.reduck.global.exception.exception.IllegalArgumentException;
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

        if (followRepository.findByUserAndFollowingUser(user, followingUser).isPresent())
            throw new IllegalArgumentException(CommonErrorCode.DUPLICATE_ARGUMENT, "이미 팔로잉한 대상입니다.");

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
    public List<FollowerResponse> getFollowers(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));

        List<Follow> followers = followRepository.findAllByFollowingUser(user);
        return followers.stream()
                .map(follow -> FollowerResponse.from(follow.getUser()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로잉 목록 조회
     */
    public List<FollowerResponse> getFollowings(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));

        List<Follow> followings = followRepository.findAllByUser(user);
        return followings.stream()
                .map(following -> FollowerResponse.from(following.getFollowingUser()))
                .collect(Collectors.toList());
    }

    /**
     * 팔로잉 취소 기능
     */
    public void cancel(User user, String followingUserId) {
        User followingUser = userRepository.findByUserId(followingUserId)
                .orElseThrow(() -> new NotFoundException(UserErrorCode.USER_NOT_EXIST));
        followRepository.findByUserAndFollowingUser(user, followingUser)
                .ifPresent(followRepository::delete);
    }
}
