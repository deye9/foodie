package com.foodie.user.contracts;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public record PermissionRequest(
  @NotEmpty String id,
  LocalDateTime createdAt,
  LocalDateTime updatedAt,
  LocalDateTime deletedAt,
  @NotEmpty String permissionName,
  @NotEmpty String permissionDescription,
  @Valid boolean canCreate,
  @Valid boolean canRead,
  @Valid boolean canUpdate,
  @Valid boolean canDelete
) {}
