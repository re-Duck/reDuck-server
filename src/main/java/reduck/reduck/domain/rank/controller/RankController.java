package reduck.reduck.domain.rank.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reduck.reduck.domain.rank.service.RankService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rank")
public class RankController {
    private final RankService rankService;
}
