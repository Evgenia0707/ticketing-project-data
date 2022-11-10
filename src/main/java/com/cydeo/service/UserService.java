package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserService {

    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);//for update info in the user interface by userName (in UI )
    void save(UserDTO user);
//    void deleteByUserName(String username);
    UserDTO update(UserDTO user); //return user after update

    void delete(String username);//keep info in DB

    List<UserDTO> listAllByRole(String role);





}
