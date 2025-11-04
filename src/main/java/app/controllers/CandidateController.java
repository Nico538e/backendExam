package app.controllers;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.daos.SkillDAO;
import app.dtos.CandidateDTO;
import app.dtos.SkillDTO;
import app.entities.Candidate;
import app.entities.Skill;
import app.services.CandidateService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CandidateController implements IController<Candidate, Integer> {
    private final CandidateDAO candidateDAO;
    private final CandidateService service;
    private final SkillDAO skillDAO;
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public CandidateController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.candidateDAO = new CandidateDAO(emf);
        this.skillDAO = new SkillDAO(emf);
        this.service = new CandidateService(candidateDAO);
    }

    @Override
    public void getById(Context ctx) {
        int id  = Integer.parseInt(ctx.pathParam("id"));
        Candidate candidate = candidateDAO.getById(id);
        CandidateDTO dto = service.toDTO(candidate);
        if(dto == null){
            ctx.status(404).json(Map.of("message", "Candidate not found"));
        }else {
            ctx.json(dto);
        }

    }

    @Override
    public void getAll(Context ctx) {
        String category = ctx.queryParam("category");

        List<Candidate> candidates;

        if(category != null && !category.isBlank()){
            candidates = candidateDAO.getAllCandidatesBySkillCategory(category);
        } else {
            candidates = candidateDAO.getAll();
        }
        ctx.json(service.toDTOList(candidates));
    }

    @Override
    public void create(Context ctx) {
        CandidateDTO dto = ctx.bodyAsClass(CandidateDTO.class);
        Candidate candidate = service.createCandidate(dto);

        ctx.status(201).json(service.toDTO(candidate));
    }

    @Override
    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        CandidateDTO dto = ctx.bodyAsClass(CandidateDTO.class);

        Candidate candidate = candidateDAO.getById(id);
        if(candidate == null){
            ctx.status(404).result("Candidate not found");
            return;
        }

        candidate.setPhone(dto.getPhone());
        candidate.setName(dto.getCandidateName());
        candidate.setEducationBackground(dto.getEducationBackground());

        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {
            List<Skill> updatedSkills = new ArrayList<>();
            for (SkillDTO skillDTO : dto.getSkills()) {
                Skill skill = skillDAO.getById(skillDTO.getId());
                if (skill == null) {
                    ctx.status(404).result("Skill not found");
                    return;
                }
                updatedSkills.add(skill);
            }
            candidate.setSkills(updatedSkills);
        }
        candidateDAO.update(candidate);
        ctx.status(200).json(service.toDTO(candidate));
    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Candidate candidate = candidateDAO.getById(id);

        if(candidate == null){
            ctx.status(404).json(Map.of("message", "Candidate not found"));
            return;

        }
        candidateDAO.delete(id);
        ctx.status(200).json(Map.of("message", "Candidate deleted"));
    }

    public void attachSkillToCandidate(Context ctx){
        int candidateId = Integer.parseInt(ctx.pathParam("candidateId"));
        int skillId = Integer.parseInt(ctx.pathParam("skillId"));

        Candidate candidate = candidateDAO.getById(candidateId);
        if(candidate == null){
            ctx.status(404).result("Candidate not found");

        }else {
            candidateDAO.attachSkillToCandidate(candidateId, skillId);
            ctx.status(200).json(Map.of("message", "Skill attached"));
        }

    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }

    @Override
    public Candidate validateEntity(Context ctx) {
        return null;
    }
}
