package reduck.reduck.domain.post.entity;

import java.util.Arrays;

public enum PostType {

    QNA("QNA"),
    STACK("STACK"),
    ;

    private String type;


    PostType(String type) {
        this.type = type;
    }

    public static PostType getType(String postType) {
        PostType type = Arrays.stream(values())
                .filter(value -> value.type.equals(postType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        return type;
    }

    public PostType toUpperCase() {
        String type = this.type;
        return getType(type.toUpperCase());
    }
}
