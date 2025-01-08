package s_a_rb01_its6.followerservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import s_a_rb01_its6.followerservice.configuration.RabbitMQConfig;
import s_a_rb01_its6.followerservice.domain.requests.FollowRequest;
import s_a_rb01_its6.followerservice.domain.requests.FollowResponse;
import s_a_rb01_its6.followerservice.events.FollowEvent;
import s_a_rb01_its6.followerservice.events.UserUpdatedEvent;
import s_a_rb01_its6.followerservice.repository.FollowerRepository;
import s_a_rb01_its6.followerservice.repository.FollowerRequestRepository;
import s_a_rb01_its6.followerservice.repository.entities.FollowRequestEntity;
import s_a_rb01_its6.followerservice.repository.entities.FollowerEntity;
import s_a_rb01_its6.followerservice.service.FollowerService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {

    private final FollowerRequestRepository followRequestRepository;
    private final FollowerRepository FollowRepository;
    private final RabbitTemplate rabbitTemplate;


    //TODO implement the ability for profiles to be private or public
    @Override
    public void requestFollow(FollowRequest followRequest) {
        //see if profile is public or private and if private send a follow request else follow directly


        FollowRequestEntity followRequestEntity = FollowRequestEntity.builder()
                .requester_id(followRequest.requester_id())
                .requested_id(followRequest.requested_id())
                .requester_username(followRequest.requester_username())
                .requested_username(followRequest.requested_username())
                .status(followRequest.status())
                .created_at(followRequest.created_at())
                .build();
        followRequestRepository.save(followRequestEntity);

        rabbitTemplate.convertAndSend(RabbitMQConfig.FOLLOW_REQUEST_EXCHANGE,
                RabbitMQConfig.FOLLOW_REQUEST_ROUTING_KEY, followRequest);

    }

    @Override
    public void handleFollowResponse(FollowResponse followResponse) {
        // Retrieve the follow request
        FollowRequestEntity request = followRequestRepository.findById(followResponse.id())
                .orElseThrow(() -> new RuntimeException("Follow request not found"));

        if ("ACCEPTED".equalsIgnoreCase(followResponse.status())) {
            // Create new follower
            FollowerEntity newFollower = FollowerEntity.builder()
                    .user1Id(followResponse.requester_id())
                    .user2Id(followResponse.requested_id())
                    .user1Username(followResponse.requester_username())
                    .user2Username(followResponse.requested_username())
                    .since(followResponse.created_at())
                    .build();
            FollowRepository.save(newFollower);

            // Emit event to RabbitMQ
            FollowEvent event = FollowEvent.builder()
                    .requester_id(followResponse.requester_id())
                    .requested_id(followResponse.requested_id())
                    .requested_username(followResponse.requested_username())
                    .requester_username(followResponse.requester_username())
                    .build();

            rabbitTemplate.convertAndSend(RabbitMQConfig.FOLLOW_RESPONSE_EXCHANGE,
                    RabbitMQConfig.FOLLOW_RESPONSE_ROUTING_KEY, event);
        }

        // Delete follow request after processing
        followRequestRepository.delete(request);

    }

    @Override
    public boolean checkIfFollowing(String follower, String followee) {
        return FollowRepository.existsByUser1IdAndUser2Id(follower, followee) ||
                FollowRepository.existsByUser1IdAndUser2Id(followee, follower);
    }

    @Override
    public List<String> getFollowers(String followee) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public List<String> getFollowing(String follower) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    // delete user follower by id that is listened to from rabbitmq
    @Transactional
    @RabbitListener(queues = RabbitMQConfig.USER_DELETE_QUEUE)
    public void deleteUserFollowing(String id) {
        try {
            FollowRepository.deleteAllByUser1IdOrUser2Id(id, id);
            System.out.println("Deleted followers for userId: " + id);
        } catch (Exception e) {
            System.err.println("Error deleting followers for userId: " + id);
            throw new AmqpRejectAndDontRequeueException(e); // Sends to DLQ
        }
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.USER_UPDATE_QUEUE)
    public void updateUserFollower(UserUpdatedEvent userUpdatedEvent){
        FollowRepository.updateUsernameByUserId(userUpdatedEvent.getId(), userUpdatedEvent.getUsername());
    }
}
