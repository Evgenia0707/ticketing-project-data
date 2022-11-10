package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {//convert DTO <-> Entity

    private ModelMapper modelMapper;

    public RoleMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Role convertToEntity(RoleDTO dto){//give me RoleDto I give you Entity
        return modelMapper.map(dto, Role.class);//(obj source, obj destination Type)
    }

    public RoleDTO convertToDto(Role entity){//convert Entity to DTO
      return modelMapper.map(entity, RoleDTO.class);
    }
}
