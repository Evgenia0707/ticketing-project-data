package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final MapperUtil mapperUtil;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper, MapperUtil mapperUtil) {
        this.roleRepository = roleRepository;

        this.roleMapper = roleMapper;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //Controller called me and requesting all RoleDTO, so it can show in the drop-down in the UI
        //I need to make a call to DB and get all the Rolls from Table
        //Go to Repository and find a Service which give me roles from DB
        //and give it to UI
        //how I will call any service here? dependency injection

        List<Role> roleList = roleRepository.findAll();// from Jpa - findAll- derived query->assign

        //I have Role entries from DB
        //I need to convert this Role entire to DTO
        //I need to use ModelMapper
        //I already created class RoleMapper and there are () for me that will make this conversion
        //need injection

        return roleList.stream().map(role -> mapperUtil.convert(role, new RoleDTO())).collect(Collectors.toList());//get role (entity) and change to new role (dto) object
//        return roleList.stream().map(role -> mapperUtil.convert(role, RoleDTO.class)).collect(Collectors.toList());// need convert Entities to DTO - use model mapper(create mapper)
    }

    @Override
    public RoleDTO findById(Long id) {
        return roleMapper.convertToDto(roleRepository.findById(id).get());
    }
}
