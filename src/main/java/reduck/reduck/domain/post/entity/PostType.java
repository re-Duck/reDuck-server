package reduck.reduck.domain.post.entity;

import reduck.reduck.domain.user.entity.DevelopAnnual;

import java.util.Arrays;

public enum PostType {

    qna("qna"),
    stack("stack"),
    ;

    private String type;


    PostType(String type) {
        this.type = type;
    }

    public static PostType getType(String postType) {
        System.out.println("=========================================================");
        System.out.println("postType = " + postType);

        PostType type = Arrays.stream(values())
                .filter(value -> value.type.equals(postType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        return type;
    }
}
