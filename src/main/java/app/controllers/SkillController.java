package app.controllers;

import app.config.HibernateConfig;
import app.daos.CandidateDAO;
import app.daos.SkillDAO;
import app.dtos.SkillDTO;
import app.entities.Skill;
import app.services.SkillService;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Map;

public class SkillController implements IController<Skill, Integer> {
    private final CandidateDAO candidateDAO;
    private final SkillService service;
    private final SkillDAO skillDAO;
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

    public SkillController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.candidateDAO = new CandidateDAO(emf);
        this.skillDAO = new SkillDAO(emf);
        this.service = new SkillService(skillDAO);
    }

    @Override
    public void getById(Context ctx) {
        int id  = Integer.parseInt(ctx.pathParam("id"));
        Skill skill = skillDAO.getById(id);
        SkillDTO dto = service.toDTO(skill);
        if(dto == null){
            ctx.status(404).json(Map.of("message", "Skill not found"));
        }else {
            ctx.json(dto);
        }

    }

    @Override
    public void getAll(Context ctx) {
        List<Skill> skills;

        skills = skillDAO.getAll();

        ctx.json(service.toDTOList(skills));
    }

    @Override
    public void create(Context ctx) {
        SkillDTO dto = ctx.bodyAsClass(SkillDTO.class);
        Skill skill = service.createSkill(dto);

        ctx.status(201).json(service.toDTO(skill));
    }

    @Override
    public void update(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        SkillDTO dto = ctx.bodyAsClass(SkillDTO.class);

        Skill skill = skillDAO.getById(id);
        if(skill == null){
            ctx.status(404).result("Skill not found");
        }

        skill.setCategory(dto.getCategory());
        skill.setSkillName(dto.getSkillName());
        skill.setDescription(dto.getDescription());

        Skill updatedSkill = skillDAO.update(skill);
        ctx.json(service.toDTO(updatedSkill));
    }

    @Override
    public void delete(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Skill skill = skillDAO.getById(id);

        if(skill == null){
            ctx.status(404).json(Map.of("message", "Skill not found"));
            return;

        }
        skillDAO.delete(id);
        ctx.status(200).json(Map.of("message", "Skill deleted"));
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        return false;
    }

    @Override
    public Skill validateEntity(Context ctx) {
        return null;
    }
}
