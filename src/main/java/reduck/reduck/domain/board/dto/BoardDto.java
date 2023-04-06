package reduck.reduck.domain.board.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class BoardDto {
    private String title;
    private String content;
    private String originId;
    private List<String> imagePathList;
}
