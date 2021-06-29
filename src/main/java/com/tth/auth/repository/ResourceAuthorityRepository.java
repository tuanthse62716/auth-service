package com.tth.auth.repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.tth.auth.dto.resourceAuthority.ResourceType;
import com.tth.auth.entity.ResourceAuthority;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ResourceAuthorityRepository extends JpaRepository<ResourceAuthority, UUID> {
  
  long deleteByTargetTypeAndTargetId(ResourceType targetType, UUID targetId);
  
  long deleteByResourceTypeAndResourceId(ResourceType resourceType, String resourceId);
  
  @Query(value =
    "SELECT ra FROM ResourceAuthority ra "
  + "WHERE resourceType = :resourceType "
  + "AND (resourceId IS NULL OR resourceId = :resourceId) "
  + "AND targetId IN (:targetIds) "
  + "AND bitwise_and(permissions, :permissions) = :permissions ")
  List<ResourceAuthority> findOnSpecificResource(
      @Param("resourceType") @NotNull ResourceType resourceType,
      @Param("resourceId") @NotNull String resourceId,
      @Param("targetIds") @NotEmpty Collection<UUID> targetIds,
      @Param("permissions") @NotNull int permissions,
      Pageable pageable);

  @Query(value =
    "SELECT ra FROM ResourceAuthority ra "
  + "WHERE resourceType = :resourceType "
  + "AND resourceId IS NULL "
  + "AND targetId IN (:targetIds) "
  + "AND bitwise_and(permissions, :permissions) = :permissions ")
  List<ResourceAuthority> findOnResourceType(
      @Param("resourceType") @NotNull ResourceType resourceType,
      @Param("targetIds") @NotEmpty Collection<UUID> targetIds,
      @Param("permissions") @NotNull int permissions,
      Pageable pageable);
  
  @Query(nativeQuery = true, value =
    "SELECT DISTINCT resource_id FROM {h-schema}resource_authority "
  + "WHERE resource_type = :resourceType "
  + "AND target_id IN (:targetIds) "
  + "AND permissions & :permissions = :permissions ")
  List<String> findResourceIdsByTargets(
      @Param("resourceType") @NotNull String resourceType,
      @Param("targetIds") @NotEmpty Collection<UUID> targetIds,
      @Param("permissions") @NotNull int permissions);

}
