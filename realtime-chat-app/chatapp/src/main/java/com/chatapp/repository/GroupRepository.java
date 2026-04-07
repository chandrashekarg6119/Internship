package com.chatapp.repository;

import com.chatapp.entity.Group;
import com.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("SELECT g FROM Group g JOIN g.members m WHERE m = :user")
    List<Group> findGroupsByMember(User user);

    @Query("SELECT g FROM Group g WHERE g.name LIKE CONCAT('%', :name, '%')")
    List<Group> searchByName(String name);
}
