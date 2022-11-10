package com.cydeo.converter;

import com.cydeo.dto.RoleDTO;
import com.cydeo.service.RoleService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding//need for converting classes
public class RoleDtoConverter implements Converter<String, RoleDTO> {///convert String to Object

    private final RoleService roleService;//RoleDtoConverter depend on roleService
    //circular reference/dependency error - need add @Lazy - will break circle
    public RoleDtoConverter(@Lazy RoleService roleService) {//do when ask (role save in UI) inject rolserv bean to roledto until ask
        this.roleService = roleService;
    }

    @Override
    public RoleDTO convert(String source) {

        if (source == null || source.equals("")) {  //  Select  -> ""
            return null;
        }

        return roleService.findById(Long.parseLong(source));//find obj- need impl (coming like 1, 2,3 not String admin...)
//go db - string
    }

}

