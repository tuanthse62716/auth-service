package com.tth.auth.dto.resourceAuthority;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.AssertTrue;

import lombok.Value;

@Value
public class ResourceAuthorityCriteria {
  
  private ResourceType targetType;
  
  private UUID targetId;
  
  private ResourceType resourceType;
  
  private String resourceId;
  
  private Set<ResourcePermission> permissions;
  
  @AssertTrue(message="require targetType for targetId")
  boolean isRequireTargetTypeForTargetId() {
    return targetId == null ^ 
        (targetId != null && targetType != null);
  }
  
  @AssertTrue(message="require resourceType for resourceId")
  boolean isRequireResourceTypeForResourceId() {
    return resourceId == null ^ 
        (resourceId != null && resourceType != null);
  }
  
  // TODO
  private Boolean onResourceType;
  private Boolean unpaged;
  
}
