package reduck.reduck.domain.board.entity.mapper;

import reduck.reduck.domain.board.dto.BoardDto;
import reduck.reduck.domain.board.entity.Board;

public class BoardMapper {
    public Board from(Object obj) {
        if (obj instanceof BoardDto) {
            BoardDto boardDto = (BoardDto) obj;
            Board board = Board.builder()
                    .originId(boardDto.getOriginId())
                    .content(boardDto.getContent())
                    .title(boardDto.getTitle())
                    .build();
            return board;
        }
        return null;
    }
}
