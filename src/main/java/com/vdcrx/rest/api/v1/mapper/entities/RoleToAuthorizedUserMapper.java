package com.vdcrx.rest.api.v1.mapper.entities;

import com.vdcrx.rest.domain.entities.Role;
import com.vdcrx.rest.security.user_details.AuthorizedUserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Role to authorized user mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleToAuthorizedUserMapper {

    @Mapping(target = "roleType", source = "name")
    AuthorizedUserRole mapToAuthorizedUserRole(Role role);
}
