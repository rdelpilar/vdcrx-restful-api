package com.vdcrx.rest.api.v1.mapper.security;

import com.vdcrx.rest.api.v1.model.dto.JwtAuthorizedUserDto;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.domain.entities.Role;
import com.vdcrx.rest.security.user_details.AuthorizedUser;
import com.vdcrx.rest.security.user_details.AuthorizedUserRole;
import org.mapstruct.*;

/**
 * Authorized user mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorizedUserMapper {

    @Mapping(target = "roleType", source = "name")
    AuthorizedUserRole mapToAuthorizedUserRole(Role role);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "authorities", source = "authorities"),
            @Mapping(target = "enabled", source = "enabled"),
            @Mapping(target = "accountNonExpired", source = "accountNonExpired"),
            @Mapping(target = "accountNonLocked", source = "accountNonLocked"),
            @Mapping(target = "credentialsNonExpired", source = "credentialsNonExpired")
    })
    JwtAuthorizedUserDto mapToJwtUserDto(AuthorizedUser user);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "password", source = "hashedPassword"),
            @Mapping(target = "authorities", source = "roles"),
            @Mapping(target = "enabled", source = "loginEnabled"),
            @Mapping(target = "lastPasswordResetDate", source = "lastPasswordResetDate"),
            @Mapping(target = "accountNonExpired", source = "accountNonExpired"),
            @Mapping(target = "accountNonLocked", source = "accountNonLocked"),
            @Mapping(target = "credentialsNonExpired", source = "credentialsNonExpired")
    })
    AuthorizedUser mapToAuthorizedUser(Person person);
}
