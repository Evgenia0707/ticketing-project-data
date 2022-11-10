package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return projectMapper.convertToDto(project);
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> list = projectRepository.findAll(Sort.by("projectCode"));
        return list.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(dto);
        projectRepository.save(project);
//        projectRepository.save(projectMapper.convertToEntity(dto));//same in 1 line
    }

    @Override
    public void update(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        Project convertedProject = projectMapper.convertToEntity(dto);
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());
        projectRepository.save(convertedProject);
    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);
//    want my project called after I deleted the project +  add this id to new code
        project.setProjectCode(project.getProjectCode() + "-" + project.getId());//SP00-1(was SP00)
        projectRepository.save(project);
        taskService.deleteByProject(projectMapper.convertToDto(project));
    }

    @Override
    public void complete(String projectCode) {//need set all status when comp proj
        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
        taskService.completeByProject(projectMapper.convertToDto(project));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        //give manager name - go to DB - ask for all projects under his name
        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");//hwo is login (DTO)
        User user = userMapper.convertToEntity(currentUserDTO);
        List<Project> list = projectRepository.findAllByAssignedManager(user);
        return list.stream().map(project -> {
                    ProjectDTO obj = projectMapper.convertToDto(project);
                    obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
                    obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
                    return obj;
                }
        ).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {
        List<Project> projects = projectRepository.
                findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE, userMapper.convertToEntity(assignedManager));
        return projects.stream().map(projectMapper::convertToDto).collect(Collectors.toList());
    }
}