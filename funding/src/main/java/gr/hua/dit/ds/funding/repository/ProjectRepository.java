package gr.hua.dit.ds.funding.repository;

import gr.hua.dit.ds.funding.entity.Project;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@RepositoryRestResource(path = "project")
@Hidden
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByTitle(String title);
}
