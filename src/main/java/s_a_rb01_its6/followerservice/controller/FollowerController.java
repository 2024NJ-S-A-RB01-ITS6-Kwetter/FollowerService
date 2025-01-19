package s_a_rb01_its6.followerservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import s_a_rb01_its6.followerservice.domain.requests.FollowRequest;
import s_a_rb01_its6.followerservice.domain.requests.FollowResponse;
import s_a_rb01_its6.followerservice.service.FollowerService;

import java.util.List;

import static s_a_rb01_its6.followerservice.configuration.Constant.API_URL;

@RequiredArgsConstructor
@RestController
@RequestMapping(value =API_URL + "/follow")
public class FollowerController {

    private final FollowerService followerService;

    //TODO implement to load active follow requests on login


    @PostMapping("/request")
    public ResponseEntity<String> follow(@RequestBody FollowRequest followRequest){
        followerService.requestFollow(followRequest);
        return ResponseEntity.ok("Added");
    }

    @PostMapping("/response")
    public ResponseEntity<String> handleFollowResponse(@RequestBody FollowResponse followResponse){
        followerService.handleFollowResponse(followResponse);
        return ResponseEntity.ok("Handled");
    }

    @GetMapping("/isfollowing")
    public ResponseEntity<Boolean> checkIfFollowing(@RequestParam String user1Id, @RequestParam String user2Id) {
        {
            boolean isFollowing = followerService.checkIfFollowing(user1Id, user2Id);
            return ResponseEntity.ok(isFollowing);
        }
    }
    @GetMapping("/followers")
    public ResponseEntity<List<String>> getFollowers (@RequestParam String userId){
        {
            List<String> followers = followerService.getFollowers(userId);
            return ResponseEntity.ok(followers);
        }
    }
    @GetMapping("/following")
    public ResponseEntity<List<String>> getFollowing (@RequestParam String userId){
        {
            List<String> following = followerService.getFollowing(userId);
            return ResponseEntity.ok(following);
        }
    }
}
