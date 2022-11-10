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

        List<User> userList = userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);//deleted fild need be false for return all users(give all users if not deleted)
//                findAll(Sort.by("firstName"));//return is sort - assign
        //go db grab user -> convert
        //findAll (impl-Select * from table) - give info from DB -> need write new query(differ busin logic)

        return userList.stream().map(userMapper::convertToDto).collect(Collectors.toList());//userMap - inject
    }

    @Override
    public UserDTO findByUserName(String username) {//find obj in DB
        User user = userRepository.findByUserNameAndIsDeleted(username, false);//findByUserName - need new (bec will see all delet also)
        return userMapper.convertToDto(user); //return DTO
    }

    @Override
    public void save(UserDTO user) {//coming from UI - DTO - need DB (entity) use mapper
    userRepository.save(userMapper.convertToEntity(user));//get DTO convert to Entity (only 1 obj)
    }

//    @Override//we dont hard delete- dont use ()
//    public void deleteByUserName(String username) {
//    userRepository.deleteByUserName(username);//add (public void delete(String username)) del in UI not DB
//    }

    @Override
    public UserDTO update(UserDTO user) {//(UserDTO user) updated obj -- user coming from UI

        //(mistake - save second obj , create another primary key) no i
//        userRepository.save(userMapper.convertToEntity(user));//get user- convert-save
//        return findByUserName(user.getUserName());

        //find current user - go to DB //get user from db + update user form UI
        //                                       //admin@admin.com23--null(dont have it in DB) -- null.getId
        User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(), false);//has id //findByUserName(need new with condit where delet) UserEntity
        //Map update user DTO to Entity obj - save id DB - first covert to Entity
        User convertedUser = userMapper.convertToEntity(user);// no id
//        user1.setUserName(user.getUserName());//for convertDTO-Entity if don't have mapper

        //set id to the converted object
        convertedUser.setId(user1.getId());
        //save updated user in the DB
        userRepository.save(convertedUser);

        return findByUserName(user.getUserName());//created () with return userDto.
        //we have ()findByUserName - use it -- User user = userRepository.findByUserName(username);
        //        return userMapper.convertToDto(user); //return DTO
    }

    @Override
    public void delete(String username) {//also need update Controller
        //go to DB and get that user with username
                //change the isDeleted field to true
        //save the object in the DB
        User user = userRepository.findByUserNameAndIsDeleted(username, false);

        //check if assign task- if true - delete - if not - do nothing

        if (checkIfUserCanBeDeleted(user)){
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());//create - harold...com-2 if delete
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

    private boolean checkIfUserCanBeDeleted(User user){// user Entity (can pass DTO or Entiry- private() not connect with Controller don have get/send info to Controller)

        //want to know if user manager or employ(have same unopleted project/task)
        switch (user.getRole().getDescription()){

            case "Manager"://check uncompl proj
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDto(user));   //creat() nonComp
                //need sent dto obj to projService
                //get nonComlp proj -
                return projectDTOList.size() == 0;//if 0 - delete(nothing uncompleted)

            case "Employee":
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(userMapper.convertToDto(user));   //creat() nonComp
                //need sent dto obj to projService
                //get nonComlp proj -
                return taskDTOList.size() == 0;//if 0 - delete(nothing uncompleted)

            default:
                return true;
        }
    }




}
