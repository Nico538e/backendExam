package app.dtos;

import app.enums.Category;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class SkillDTO {
    private Integer id;

    private String skillName;

    private Category category;

    private String description;
}
