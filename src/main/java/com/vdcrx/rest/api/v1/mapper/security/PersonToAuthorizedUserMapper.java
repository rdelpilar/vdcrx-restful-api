package com.vdcrx.rest.api.v1.mapper.security;

import com.vdcrx.rest.api.v1.mapper.entities.RoleToAuthorizedUserMapper;
import com.vdcrx.rest.domain.entities.Person;
import com.vdcrx.rest.security.user_details.AuthorizedUser;
import org.mapstruct.*;

/**
 * Person to authorized user mapper
 *
 * @author Ranel del Pilar
 */

@Mapper(componentModel = "spring",
        uses = RoleToAuthorizedUserMapper.class,
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PersonToAuthorizedUserMapper {

    @Mappings({
            @Mapping(target = "uuid", source = "uuid"),
            @Mapping(target = "username", source = "username"),
            @Mapping(target = "firstName", source = "firstName"),
            @Mapping(target = "lastName", source = "lastName"),
            @Mapping(target = "password", source = "hashedPassword"),
            @Mapping(target = "email", source = "email"),
            @Mapping(target = "authorities", source = "roles"),
            @Mapping(target = "enabled", source = "loginEnabled"),
            @Mapping(target = "lastPasswordResetDate", source = "lastPasswordResetDate"),
            @Mapping(target = "accountNonExpired", source = "accountNonExpired"),
            @Mapping(target = "accountNonLocked", source = "accountNonLocked"),
            @Mapping(target = "credentialsNonExpired", source = "credentialsNonExpired")
    })
    AuthorizedUser mapToAuthorizedUser(Person person);
}
