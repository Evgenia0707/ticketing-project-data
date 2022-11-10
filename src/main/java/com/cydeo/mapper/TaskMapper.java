package com.cydeo.mapper;

import com.cydeo.dto.RoleDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Role;
import com.cydeo.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    private final ModelMapper modelMapper;//doing conversion It is a 3rd party library

    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Task convertToEntity(TaskDTO dto){//give me TaskDto I give you Entity
        return modelMapper.map(dto, Task.class);//(obj source, obj destination Type)//model give me chance use  map (use get/setter for each)
    }

    public TaskDTO convertToDto(Task entity){
        return modelMapper.map(entity, TaskDTO.class);
    }
}
