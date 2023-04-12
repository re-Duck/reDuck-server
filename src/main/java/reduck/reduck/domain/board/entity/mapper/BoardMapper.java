package reduck.reduck.domain.board.entity.mapper;

import reduck.reduck.domain.board.dto.BoardDto;
import reduck.reduck.domain.board.entity.Board;

public class BoardMapper {
    public static Board from(BoardDto obj) {
        Board board = Board.builder()
                .postOriginId(obj.getPostOriginId())
                .content(obj.getContent())
                .title(obj.getTitle())
                .boardType(obj.getBoardType())
                .build();
        return board;

    }
}
