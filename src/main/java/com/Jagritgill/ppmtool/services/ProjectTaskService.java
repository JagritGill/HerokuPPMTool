package com.Jagritgill.ppmtool.services;

import com.Jagritgill.ppmtool.domain.Backlog;
import com.Jagritgill.ppmtool.domain.Project;
import com.Jagritgill.ppmtool.domain.ProjectTask;
import com.Jagritgill.ppmtool.exceptions.ProjectNotFoundException;
import com.Jagritgill.ppmtool.repositories.BacklogRepository;
import com.Jagritgill.ppmtool.repositories.ProjectRepository;
import com.Jagritgill.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {


    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username){

        //Exceptions: Project not found


            //PTs to be added to a specific project, project != null, BL exists
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();//backlogRepository.findByProjectIdentifier(projectIdentifier);

            //set the bl to pt
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
            Integer BacklogSequence = backlog.getPTSequence();
            // Update the BL SEQUENCE
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);

            //Add Sequence to Project Task
            projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority null

            //INITIAL status when status is null
            if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }
            //Fix bug with priority in Spring Boot Server, needs to check null first
            if(projectTask.getPriority()== null || projectTask.getPriority()==0){ //In the future we need projectTask.getPriority()== 0 to handle the form
                projectTask.setPriority(3);
            }

            return projectTaskRepository.save(projectTask);



    }

    public Iterable<ProjectTask>findBacklogById(String id, String username){

        projectService.findProjectByIdentifier(id, username);
        return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
    }

    public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username){

        //make sure we are searching on the right backlog
        projectService.findProjectByIdentifier(backlog_id, username);

        //make sure that our task exists
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

        if(projectTask == null){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        //make sure that backlog/project id in the path corresponds to the right
        if(!projectTask.getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project '"+backlog_id);
        }
        return projectTask;
    }



    public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);

        projectTask = updatedTask;

        return projectTaskRepository.save(projectTask);

    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id, username);
//        trying to make this work with out using java code
//        Backlog backlog = projectTask.getBacklog();
//        List<ProjectTask> pts =projectTask.getBacklog().getProjectTasks();
//        ((List<?>) pts).remove(projectTask);
//        backlogRepository.save(backlog);

        projectTaskRepository.delete(projectTask);
    }
    //Update project task

    //find existing project task

    //replace it with updated task

    //Save update



}
