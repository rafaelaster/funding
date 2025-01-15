package gr.hua.dit.ds.funding.dao;

import gr.hua.dit.ds.funding.entity.Project;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDAOImpl implements ProjectDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Project> getProjects() {
        TypedQuery<Project> query = entityManager.createQuery("from Project", Project.class);
        return query.getResultList();
    }

    @Override
    public Project getProject(Integer project_id) {
        return entityManager.find(Project.class, project_id);
    }

    @Override
    @Transactional
    public Project saveProject(Project project) {
        System.out.println("project " + project.getId());
        if (project.getId() == null) {
            entityManager.persist(project);
        } else {
            entityManager.merge(project);
        }
        return project;
    }

    @Override
    @Transactional
    public void deleteProject(Integer project_id) {
        System.out.println("Deleting project with id: " + project_id);
        entityManager.remove(entityManager.find(Project.class, project_id));
    }
}
