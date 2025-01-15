package gr.hua.dit.ds.funding.rest;

import gr.hua.dit.ds.funding.dao.ProjectDAO;
import gr.hua.dit.ds.funding.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectRestController {

    @Autowired
    private ProjectDAO projectDao;

    @GetMapping("")
    public List<Project> getProjects() {
        return projectDao.getProjects();
    }

    @PostMapping("")
    public Project saveProject(@RequestBody Project project) {
        return projectDao.saveProject(project);
    }
}