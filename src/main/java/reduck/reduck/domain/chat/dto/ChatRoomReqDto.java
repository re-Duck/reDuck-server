package reduck.reduck.domain.chat.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomReqDto {

    private String roomId; // 클라에서 지정 후 전달.
    private String roomName;
    private List<String> otherIds; // 1:1 대화에서 상대방.


}
