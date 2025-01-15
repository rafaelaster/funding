package gr.hua.dit.ds.funding.dao;

import gr.hua.dit.ds.funding.entity.Project;

import java.util.List;

public interface ProjectDAO {

    public List<Project> getProjects();
    public Project getProject(Integer project_id);
    public Project saveProject(Project project);
    public void deleteProject(Integer project_id);
}
