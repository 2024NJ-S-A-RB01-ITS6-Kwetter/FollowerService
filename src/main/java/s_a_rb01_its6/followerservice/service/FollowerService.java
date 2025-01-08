package s_a_rb01_its6.followerservice.service;

import s_a_rb01_its6.followerservice.domain.requests.FollowRequest;
import s_a_rb01_its6.followerservice.domain.requests.FollowResponse;

import java.util.List;

public interface FollowerService {
    void requestFollow(FollowRequest followRequest);
    void handleFollowResponse(FollowResponse followResponse);
    boolean checkIfFollowing(String follower, String followee);
    List<String> getFollowers(String followee);
    List<String> getFollowing(String follower);
}
