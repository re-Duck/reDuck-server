package reduck.reduck.domain.chatgpt.entity;

import lombok.*;
import reduck.reduck.domain.chatgpt.dto.ChatGptLogRequest;
import reduck.reduck.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatGptLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatGpt chatGpt;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String userCode;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String userQuestion;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String gptAnswer;

    private String date;
    private String detailTime;
    public static ChatGptLog of(ChatGptLogRequest chatGptLogRequest, ChatGpt policy) {
        return ChatGptLog.builder()
                .chatGpt(policy)
                .userQuestion(chatGptLogRequest.getUserQuestion())
                .userCode(chatGptLogRequest.getUserCode())
                .gptAnswer(chatGptLogRequest.getGptAnswer())
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .detailTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
