package com.tth.auth.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.tth.auth.entity.GroupMember;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface GroupMemberRepository extends JpaRepository<GroupMember, UUID> {
  
  int countByGroupId(UUID groupId);
  
  Optional<GroupMember> findByGroupIdAndUserId(@NotNull UUID groupId, @NotNull UUID userId);
  
  @Query(nativeQuery = true, value =
    "SELECT gm.group_id "
  + "FROM {h-schema}group_member gm "
  + "LEFT JOIN {h-schema}tth_group gr ON gr.id = gm.group_id "
  + "WHERE gm.user_id = :userId "
  + "AND gr.enabled = true ")
  List<UUID> findEnabledGroupIds(@Param("userId") @NotNull UUID userId);
  
  @Query(value =
    "SELECT gm "
  + "FROM GroupMember gm "
  + "WHERE gm.groupId = :groupId "
  + "AND (:keyword IS null OR gm.user.username LIKE %:keyword%) "
  + "AND (:keyword IS null OR gm.user.username LIKE %:keyword% "
  + "  OR CONCAT(gm.user.personalInformation.lastName, ' ', gm.user.personalInformation.firstName) LIKE %:keyword%) ")
  @EntityGraph(attributePaths = {"user", "user.personalInformation",
      "modifiedBy", "modifiedBy.personalInformation"})
  <T> Page<T> findMembers(@Param("groupId") @NotNull UUID groupId,
      @Param("keyword") String keyword,
      Pageable pageable,
      @NotNull Class<T> type);
  
}
