package com.cydeo.converter;

import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
//import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationPropertiesBinding//new version Spring don't need for convert
public class UserDtoConverter implements Converter<String, UserDTO> {//convert String to Object

    UserService userService;

    public UserDtoConverter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDTO convert(String source) {

        if (source == null || source.equals("")) {
            return null;
        }

        return userService.findByUserName(source);

    }

}
