package reduck.reduck.domain.post.entity;

public enum PostType {

    qna("질문 게시판"),
    stack("기술 공유 게시판"),
    ;

    private String type;



    PostType(String type) {
        this.type = type;
    }
}
