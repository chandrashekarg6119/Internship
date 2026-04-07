package com.chatapp.controller;

import com.chatapp.dto.GroupDTO;
import com.chatapp.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDTO.GroupResponse> createGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody GroupDTO.CreateGroupRequest request) {
        return ResponseEntity.ok(groupService.createGroup(userDetails.getUsername(), request));
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO.GroupResponse>> getUserGroups(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(groupService.getUserGroups(userDetails.getUsername()));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupDTO.GroupResponse> getGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDTO.GroupResponse> updateGroup(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @RequestBody GroupDTO.CreateGroupRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(groupId, request, userDetails.getUsername()));
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupDTO.GroupResponse> addMember(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @RequestBody GroupDTO.AddMemberRequest request) {
        return ResponseEntity.ok(groupService.addMember(groupId, request.getUserId(), userDetails.getUsername()));
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<?> removeMember(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long groupId,
            @PathVariable Long userId) {
        groupService.removeMember(groupId, userId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}
