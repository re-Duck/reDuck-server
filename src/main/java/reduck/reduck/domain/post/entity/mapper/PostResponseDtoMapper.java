package reduck.reduck.domain.post.entity.mapper;

import org.springframework.util.StringUtils;
import reduck.reduck.domain.post.dto.CommentResponseDto;
import reduck.reduck.domain.post.dto.PostResponseDto;
import reduck.reduck.domain.post.entity.Comment;
import reduck.reduck.domain.post.entity.Post;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostResponseDtoMapper {

    public static PostResponseDto excludeFrom(Post post, String target) {
        System.out.println("시작 ! ==================================================");
        PostResponseDto postResponseDto = from(post);
        PostResponseDto newPostResponseDto = new PostResponseDto();

        System.out.println("postResponseDto.toString() = " + postResponseDto.toString());
        Field[] declaredFields = post.getClass().getDeclaredFields();
        Method[] declaredMethods = postResponseDto.getClass().getDeclaredMethods();
        Method[] declaredMethods2 = newPostResponseDto.getClass().getDeclaredMethods();
        System.out.println("============#############================");
        List<Supplier<PostResponseDto>> obj = new LinkedList<>();
        Map<String,Object> map = new HashMap<>();
        for (Method method : declaredMethods) {
            System.out.println("method.getName() = " + method.getName());
//
            Pattern compileGet = Pattern.compile("get.*");
            Matcher matcherGet = compileGet.matcher(method.getName());
            if (matcherGet.find()) {
                String capitalize = StringUtils.capitalize(target);
                String ss = "get" + capitalize;
                if (!method.getName().equals(ss)) {
                    try {
                        Object invoke = method.invoke(postResponseDto);
                        System.out.println("invoke = " + invoke);
                        map.put(method.getName(),invoke);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("=====================================================");
        for (Method method : declaredMethods2) {
            Pattern compile = Pattern.compile("set.*");
            Matcher matcher = compile.matcher(method.getName());
            if (matcher.find()) {
                String capitalize = StringUtils.capitalize(target);
                String ss = "set" + capitalize;
                if (!method.getName().equals(ss)) {
                    System.out.println("method.getName() = " + method.getName());
                    try {
                        String mName = "get"+method.getName().substring(3);
//                        mName
                        System.out.println("mName = " + mName);
                        Object getPostOriginId = method.invoke(newPostResponseDto, map.get(mName));
                        System.out.println("invoke2 = " + getPostOriginId);

                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//        PostResponseDto post1 = obj.get(0).get();
        System.out.println("newPostResponseDto = " + newPostResponseDto.getPostOriginId());
//        System.out.println("post1.getPostContent() = " + post1.getPostContent());
        return newPostResponseDto;
    }

    public static PostResponseDto from(Post post) {
        List<CommentResponseDto> comments = new ArrayList<>();
        for (Comment comm : post.getComments()) {
            CommentResponseDto commentResponseDto = CommentResponseDtoMapper.of(post.getUser(), comm);
            comments.add(commentResponseDto);
        }
        PostResponseDto postResponseDto = PostResponseDto.builder()
                //user
                .postAuthorId(post.getUser().getUserId())
                .postAuthorName(post.getUser().getName())
                .postAuthorProfileImg(post.getUser().getProfileImg())
                //post
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postOriginId(post.getPostOriginId())
                .postType(post.getPostType())
                .postCreatedAt(post.getCreatedAt())
                .postUpdatedAt(post.getUpdatedAt())
                //comment
                .comments(comments)
                .build();
        return postResponseDto;
    }
}
