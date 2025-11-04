package app.entities;

import app.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString

@Entity
@Table(name = "skills")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String skillName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;


    @Column(nullable = false)
    private String description;


    @ManyToMany(mappedBy = "skills")
    private List<Candidate> candidates = new ArrayList<>();

    public Skill(String skillName, Category category, String description) {
        this.skillName = skillName;
        this.category = category;
        this.description = description;
    }
}
