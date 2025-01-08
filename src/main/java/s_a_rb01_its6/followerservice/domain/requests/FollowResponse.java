package s_a_rb01_its6.followerservice.domain.requests;

import java.sql.Timestamp;

public record FollowResponse (Long id, String requester_id, String requester_username,
                              String requested_id, String requested_username,
                              String status, Timestamp created_at){}