package reduck.reduck.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import reduck.reduck.domain.board.entity.BoardImage;
@Repository
public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
}
