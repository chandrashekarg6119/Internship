package com.chatapp.service;

import com.chatapp.dto.GroupDTO;
import com.chatapp.dto.UserDTO;
import com.chatapp.entity.Group;
import com.chatapp.entity.User;
import com.chatapp.repository.GroupRepository;
import com.chatapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired private GroupRepository groupRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;

    @Transactional
    public GroupDTO.GroupResponse createGroup(String creatorUsername, GroupDTO.CreateGroupRequest request) {
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Group group = Group.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdBy(creator)
                .build();

        group.getMembers().add(creator);
        group.getAdmins().add(creator);

        if (request.getMemberIds() != null) {
            for (Long memberId : request.getMemberIds()) {
                userRepository.findById(memberId).ifPresent(u -> group.getMembers().add(u));
            }
        }

        return mapToResponse(groupRepository.save(group));
    }

    public List<GroupDTO.GroupResponse> getUserGroups(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return groupRepository.findGroupsByMember(user).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public GroupDTO.GroupResponse getGroupById(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        return mapToResponse(group);
    }

    @Transactional
    public GroupDTO.GroupResponse addMember(Long groupId, Long userId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getAdmins().contains(requester)) {
            throw new RuntimeException("Only admins can add members");
        }

        User newMember = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        group.getMembers().add(newMember);
        return mapToResponse(groupRepository.save(group));
    }

    @Transactional
    public void removeMember(Long groupId, Long userId, String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isSelf = requester.getId().equals(userId);
        boolean isAdmin = group.getAdmins().contains(requester);

        if (!isSelf && !isAdmin) {
            throw new RuntimeException("Not authorized to remove this member");
        }

        User member = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        group.getMembers().remove(member);
        groupRepository.save(group);
    }

    @Transactional
    public GroupDTO.GroupResponse updateGroup(Long groupId, GroupDTO.CreateGroupRequest request,
                                               String requesterUsername) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getAdmins().contains(requester)) {
            throw new RuntimeException("Only admins can update group");
        }

        if (request.getName() != null) group.setName(request.getName());
        if (request.getDescription() != null) group.setDescription(request.getDescription());

        return mapToResponse(groupRepository.save(group));
    }

    public GroupDTO.GroupResponse mapToResponse(Group group) {
        Set<UserDTO.UserResponse> members = group.getMembers().stream()
                .map(userService::mapToResponse)
                .collect(Collectors.toSet());

        return GroupDTO.GroupResponse.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .avatar(group.getAvatar())
                .createdById(group.getCreatedBy().getId())
                .createdByUsername(group.getCreatedBy().getUsername())
                .members(members)
                .memberCount(members.size())
                .createdAt(group.getCreatedAt())
                .build();
    }
}
