package s_a_rb01_its6.followerservice.events;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowEvent implements Serializable {
    private String requester_id;
    private String requested_id;
    private String requester_username;
    private String requested_username;
}