package com.vdcrx.rest.api.v1.mapper.security;

import com.vdcrx.rest.api.v1.model.dto.jwt.JwtAuthorizedUserDto;
import com.vdcrx.rest.security.user_details.AuthorizedUser;
import org.mapstruct.*;

/**
 * AuthorizedUser to JwtAuthorizedUser DTO mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorizedUserToJwtMapper {

    @Mappings({
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "authorities", source = "authorities"),
            @Mapping(target = "enabled", source = "enabled"),
            @Mapping(target = "lastPasswordResetDate", source = "lastPasswordResetDate"),
            @Mapping(target = "accountNonExpired", source = "accountNonExpired"),
            @Mapping(target = "accountNonLocked", source = "accountNonLocked"),
            @Mapping(target = "credentialsNonExpired", source = "credentialsNonExpired")
    })
    JwtAuthorizedUserDto mapToJwtUserDto(AuthorizedUser user);
}
