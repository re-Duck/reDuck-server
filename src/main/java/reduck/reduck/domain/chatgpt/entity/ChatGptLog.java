package reduck.reduck.domain.chatgpt.entity;

import lombok.*;
import reduck.reduck.domain.chatgpt.dto.ChatGptLogRequest;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatGptLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatGpt chatGpt;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String userMessage;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String gptMessage;

    public static ChatGptLog of(ChatGptLogRequest chatGptLogRequest, ChatGpt policy) {
       return ChatGptLog.builder()
                .chatGpt(policy)
                .gptMessage(chatGptLogRequest.gptMessage)
                .userMessage(chatGptLogRequest.userMessage)
                .build();
    }
}
