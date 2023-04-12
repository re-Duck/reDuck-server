package reduck.reduck.domain.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reduck.reduck.domain.board.entity.BoardType;

@Getter
@Setter
public class BoardDto {
    private String title;
    private String userId;
    private String content;
    private String postOriginId;
    private BoardType boardType;
}
