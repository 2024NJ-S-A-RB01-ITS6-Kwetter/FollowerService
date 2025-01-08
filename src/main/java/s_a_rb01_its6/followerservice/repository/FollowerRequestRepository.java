package s_a_rb01_its6.followerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s_a_rb01_its6.followerservice.repository.entities.FollowRequestEntity;

public interface FollowerRequestRepository extends JpaRepository<FollowRequestEntity, Long> {
}
