package s_a_rb01_its6.followerservice.repository.entities;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name = "follow_requests")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FollowRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requester_id;
    private String requester_username;
    private String requested_id;
    private String requested_username;
    private String status;
    private Timestamp created_at;
}