package s_a_rb01_its6.followerservice.repository.entities;
import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "followers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user1_id")
    private String user1Id;

    @Column(name = "user1_username")
    private String user1Username;

    @Column(name = "user2_id")
    private String user2Id;

    @Column(name = "user2_username")
    private String user2Username;

    private Timestamp since;
}