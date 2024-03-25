package reduck.reduck.domain.board.entity;

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
        PostType type = Arrays.stream(values())
                .filter(value -> value.type.equals(postType))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
        return type;
    }
}
