package reduck.reduck.domain.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RedisController {
    private final RedisRepository repository;

    @PostMapping("/redis/v1")
    public ResponseEntity<RedisCache> add(@RequestBody RedisCache cache) {
        log.info("Controller Requset : {}", cache);

        RedisCache save = repository.save(cache);

        Optional<RedisCache> result = repository.findById(save.getIdx());
        if (result.isPresent()) {
            System.out.println(result.get().getName());
            return new ResponseEntity<>(null, HttpStatus.OK);
        }else {

            log.error("error");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }
    }
}
