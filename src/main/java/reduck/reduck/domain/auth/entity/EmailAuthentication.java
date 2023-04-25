package reduck.reduck.domain.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuthentication extends BaseEntity {
    private int authenticationNumber;
    private String email;
}
