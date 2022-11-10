package com.cydeo.mapper;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    private final ModelMapper modelMapper;//need create ()s //convert DTo <-> Entity

    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public Project convertToEntity(ProjectDTO dto){//give me RoleDto I give you Entity
        return modelMapper.map(dto, Project.class);//(obj source, obj destination Type)
    }

    public ProjectDTO convertToDto(Project entity){//ret proj DTO
        return modelMapper.map(entity, ProjectDTO.class);
    }
}

