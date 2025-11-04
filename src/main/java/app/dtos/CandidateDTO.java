package app.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CandidateDTO {
    private Integer id;

    private String candidateName;

    private int phone;

    private String educationBackground;

    private List<SkillDTO> skills;
}
