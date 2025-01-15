package gr.hua.dit.ds.funding.service;

import com.github.javafaker.Faker;
import gr.hua.dit.ds.funding.entity.Project;
import gr.hua.dit.ds.funding.entity.Role;
import gr.hua.dit.ds.funding.entity.User;
import gr.hua.dit.ds.funding.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Service to populate database with initial data.
 */
@Service
public class InitialDataService {

    private static final int LAST_PROJECT_ID = 10;

    private final UserRepository userRepository;

    private final ProjectRepository projectRepository;

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public InitialDataService(UserRepository userRepository,
                              RoleRepository roleRepository,
                              ProjectRepository projectRepository,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private void createUsersAndRoles() {
        final List<String> rolesToCreate = List.of("ROLE_ADMIN", "ROLE_CREATOR", "ROLE_SUPPORTER");
        for (final String roleName : rolesToCreate) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                roleRepository.save(new Role(roleName));
                return null;
            });
        }

        this.userRepository.findByUsername("creator").orElseGet(() -> {
            User user = new User("creator", "creator@hua.gr", this.passwordEncoder.encode("creator"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_CREATOR").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_SUPPORTER").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });
        this.userRepository.findByUsername("supporter").orElseGet(() -> {
            User user = new User("supporter", "supporter@hua.gr", this.passwordEncoder.encode("supporter"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_CREATOR").orElseThrow());
            roles.add(this.roleRepository.findByName("ROLE_SUPPORTER").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });
        this.userRepository.findByUsername("admin").orElseGet(() -> {
            User user = new User("admin", "admin@hua.gr", this.passwordEncoder.encode("admin"));
            Set<Role> roles = new HashSet<>();
            roles.add(this.roleRepository.findByName("ROLE_ADMIN").orElseThrow());
            user.setRoles(roles);
            userRepository.save(user);
            return null;
        });
    }

    private void createProjects() {
        for (int i = 1; i <= LAST_PROJECT_ID; i++) {
            final Faker faker = new Faker(new Random(i));
            final String title = faker.company().name();
            final String description = faker.lorem().sentence();
            final Boolean approved = faker.bool().bool();
            final Integer fundingGoal = faker.number().numberBetween(10000, 100000);
            final Integer currentFunding = faker.number().numberBetween(0, fundingGoal);

            this.projectRepository.findByTitle(title).orElseGet(() -> {
                Project project = new Project();
                project.setTitle(title);
                project.setDescription(description);
                project.setApproved(approved);
                project.setFunding_goal(fundingGoal);
                project.setCurrent_funding(currentFunding);
                this.projectRepository.save(project);
                return null;
            });
        }
    }


    @PostConstruct
    public void setup() {
        this.createUsersAndRoles();
        this.createProjects();
    }
}
