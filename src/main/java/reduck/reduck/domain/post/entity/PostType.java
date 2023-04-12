package reduck.reduck.domain.board.entity;

public enum BoardType {

    qna("질문 게시판"),
    stack("기술 공유 게시판"),
    ;

    private String type;



    BoardType(String type) {
        this.type = type;
    }
}
