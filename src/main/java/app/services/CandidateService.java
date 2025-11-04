package app.services;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.entities.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CandidateService {
    private CandidateDAO candidateDAO;
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    public CandidateService(CandidateDAO candidateDAO) {
        this.candidateDAO = candidateDAO;
    }

    public static Candidate toEntity(CandidateDTO candidateDTO) {
        Candidate candidate = new Candidate();
        candidate.setId(candidateDTO.getId());
        candidate.setName(candidateDTO.getCandidateName());
        candidate.setPhone(candidateDTO.getPhone());
        candidate.setEducationBackground(candidateDTO.getEducationBackground());
        return candidate;
    }

    public static CandidateDTO toDTO(Candidate candidate) {
        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setId(candidate.getId());
        candidateDTO.setCandidateName(candidate.getName());
        candidateDTO.setPhone(candidate.getPhone());
        candidateDTO.setEducationBackground(candidate.getEducationBackground());

        if (candidate.getSkills() != null && !candidate.getSkills().isEmpty()) {
            candidateDTO.setSkills(
                    candidate.getSkills().stream()
                            .map(skill -> new SkillDTO(
                                    skill.getId(),
                                    skill.getSkillName(),
                                    skill.getCategory(),
                                    skill.getDescription()
                            ))
                            .collect(Collectors.toList())
            );
        } else {
            candidateDTO.setSkills(new ArrayList<>());
        }

        return candidateDTO;
    }

    public static List<CandidateDTO> toDTOList(List<Candidate> candidates) {
        return candidates.stream()
                .map(CandidateService::toDTO)
                .collect(Collectors.toList());
    }

    public static List<Candidate>  toEntityList(List<CandidateDTO> candidateDTOs) {
        return candidateDTOs.stream()
                .map(CandidateService::toEntity)
                .collect(Collectors.toList());
    }

    public Candidate createCandidate(CandidateDTO dto) {
        try(EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Candidate candidate = toEntity(dto);

            if(dto.getSkills() != null && !dto.getSkills().isEmpty()) {
                List<Skill> skills = new ArrayList<>();
                for(SkillDTO skillDTO : dto.getSkills()) {
                    Skill skill = em.find(Skill.class, skillDTO.getId());
                    if(skill == null) {
                        throw new RuntimeException("Skill not found");
                    }
                    skills.add(skill);
                }
                candidate.setSkills(skills);
            }
            em.persist(candidate);
            em.getTransaction().commit();
            return candidate;
        }
    }
}
