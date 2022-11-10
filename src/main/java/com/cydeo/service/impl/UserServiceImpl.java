package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;


    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);//deleted need be false for return all users(give all users if not deleted)

        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());//userMap - inject
    }

    @Override
    public UserDTO findByUserName(String username) {//find obj in DB
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        return userMapper.convertToDto(user); //return DTO
    }

    @Override
    public void save(UserDTO user) {//coming from UI - DTO - need DB (entity) use mapper
        userRepository.save(userMapper.convertToEntity(user));
    }

//    @Override//we dont hard delete- dont use ()
//    public void deleteByUserName(String username) {
//    userRepository.deleteByUserName(username);
//    }

    @Override
    public UserDTO update(UserDTO user) {

        //find current user - go to DB
        //                                       //admin@admin.com23--null(dont have it in DB) -- null.getId
        User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(), false);
        //Map update user DTO to Entity obj - save id DB - first covert to Entity
        User convertedUser = userMapper.convertToEntity(user);
        //set id to the converted object
        convertedUser.setId(user1.getId());
        //save updated user in the DB
        userRepository.save(convertedUser);
        return findByUserName(user.getUserName());
    }

    @Override
    public void delete(String username) {//also need update Controller
        //go to DB and get that user with username/save the object in the DB
        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        //go to DB bring all users with specif role(manager)
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);
        //convert
        return users.stream().map(userMapper::convertToDto).collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user) {
        //want to know if user manager or employ
        switch (user.getRole().getDescription()) {
            case "Manager"://check uncompleted proj
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));                  //need sent dto obj to projService
                //get nonComlp proj -
                return projectDTOList.size() == 0;//if 0 - delete(all completed)

            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));
                //need sent dto obj to projService
                return taskDTOList.size() == 0;

            default:
                return true;
        }
    }

}
