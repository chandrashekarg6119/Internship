package com.chatapp.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class GroupDTO {

    @Data
    public static class CreateGroupRequest {
        private String name;
        private String description;
        private List<Long> memberIds;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupResponse {
        private Long id;
        private String name;
        private String description;
        private String avatar;
        private Long createdById;
        private String createdByUsername;
        private Set<UserDTO.UserResponse> members;
        private int memberCount;
        private LocalDateTime createdAt;
    }

    @Data
    public static class AddMemberRequest {
        private Long userId;
    }
}
