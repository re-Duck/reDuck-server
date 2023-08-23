package reduck.reduck.domain.chat.entity;

import lombok.*;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Session extends BaseEntity {

    private String session_id;

    private String user_id;

    private String room_id;

}
