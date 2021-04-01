package com.Jagritgill.ppmtool.web;

import com.Jagritgill.ppmtool.domain.Project;
import com.Jagritgill.ppmtool.services.MapValidationErrorService;
import com.Jagritgill.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/project")   //endpoint
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    //Posting to the server
    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {
        //this calls the method from MapValidationErrorService called errorMap
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        // if it contains errors the return errorMap
        if(errorMap!=null) return errorMap;

        // this saves it to the database
        Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
        return new ResponseEntity<Project>(project, HttpStatus.CREATED);
    }

    //Getting from the server
    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){

        Project project = projectService.findProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }
    // this one gets all of the projects
    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
        projectService.deleteProjectByIdentifier(projectId, principal.getName());

        return new ResponseEntity<String>("Project with ID: '"+projectId+"'was deleted successfully",HttpStatus.OK);
    }
}
