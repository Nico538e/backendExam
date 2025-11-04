package app.services;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.daos.SkillDAO;
import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Skill;
import app.entities.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SkillService {
    private SkillDAO skillDAO;
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    public SkillService(SkillDAO skillDAO) {
        this.skillDAO = skillDAO;
    }

    public static Skill toEntity(SkillDTO skillDTO) {
        Skill skill = new Skill();
        skill.setId(skillDTO.getId());
        skill.setSkillName(skillDTO.getSkillName());
        skill.setDescription(skillDTO.getDescription());
        skill.setCategory(skillDTO.getCategory());
        return skill;
    }

    public static SkillDTO toDTO(Skill skill) {
        SkillDTO skillDTO = new SkillDTO();
        skillDTO.setId(skill.getId());
        skillDTO.setSkillName(skill.getSkillName());
        skillDTO.setCategory(skill.getCategory());
        skillDTO.setDescription(skill.getDescription());
        return skillDTO;
    }

    public static List<SkillDTO> toDTOList(List<Skill> skills) {
        return skills.stream()
                .map(SkillService::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Skill> toEntityList(List<SkillDTO> skillDTOs) {
        return skillDTOs.stream()
                .map(SkillService::toEntity)
                .collect(Collectors.toList());
    }

    public Skill createSkill(SkillDTO skillDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // DTO -> Entity
            Skill skill = toEntity(skillDTO);

            // Persistér og commit
            em.persist(skill);
            em.getTransaction().commit();

            return skill;
        }
    }
}
