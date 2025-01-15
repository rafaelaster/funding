package gr.hua.dit.ds.funding.entity;

import jakarta.persistence.*;

@Entity
public class Project {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private Boolean approved;

    @Column
    private Integer funding_goal;

    @Column
    private Integer current_funding;

    public Project() {
    }

    public Project(String title, String description, Boolean approved, Integer funding_goal, Integer current_funding) {
        this.title = title;
        this.description = description;
        this.approved = approved;
        this.funding_goal = funding_goal;
        this.current_funding = current_funding;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getApproved() {
        return approved;
    }

    public Integer getFunding_goal() {
        return funding_goal;
    }

    public Integer getCurrent_funding() {
        return current_funding;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public void setFunding_goal(Integer funding_goal) {
        this.funding_goal = funding_goal;
    }

    public void setCurrent_funding(Integer current_funding) {
        this.current_funding = current_funding;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", approved=" + approved +
                ", funding_goal=" + funding_goal +
                ", current_funding=" + current_funding +
                '}';
    }
}
