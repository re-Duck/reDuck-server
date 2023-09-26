package reduck.reduck.domain.chatgpt.entity;

import lombok.Getter;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class ChatGptLog extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatGpt chatGpt;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String userMessage;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String gptMessage;
}
