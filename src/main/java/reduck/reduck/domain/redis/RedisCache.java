package reduck.reduck.domain.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash("testEntity")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RedisCache {
    @Id
    private String idx;

    private String name;

    private String content;
}
