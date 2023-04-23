package reduck.reduck.domain.auth.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import reduck.reduck.global.entity.BaseEntity;

import javax.persistence.Entity;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailAuthentication extends BaseEntity {
    private int authenticationNumber;
    private String email;
}
