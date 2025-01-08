package s_a_rb01_its6.followerservice.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import s_a_rb01_its6.followerservice.repository.entities.FollowerEntity;

public interface FollowerRepository extends JpaRepository<FollowerEntity, Long> {

    Boolean existsByUser1IdAndUser2Id(String user1Id, String user2Id);

    void deleteAllByUser1IdOrUser2Id(String user1Id, String user2Id);

    @Modifying
    @Transactional
    @Query("UPDATE FollowerEntity f " +
            "SET f.user1Username = CASE WHEN f.user1Id = :userId THEN :newUsername ELSE f.user1Username END, " +
            "f.user2Username = CASE WHEN f.user2Id = :userId THEN :newUsername ELSE f.user2Username END " +
            "WHERE f.user1Id = :userId OR f.user2Id = :userId")
    void updateUsernameByUserId(String userId, String newUsername);
}
