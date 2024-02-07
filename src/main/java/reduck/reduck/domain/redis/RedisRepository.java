package reduck.reduck.domain.redis;

import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<RedisCache, String> {
}
