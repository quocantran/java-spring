package com.example.test.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.test.core.error.BadRequestException;
import com.example.test.domain.Skill;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.service.SkillService;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {
    private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("")
    public ResponseEntity<ResponsePaginationDTO> getAllSkills(@Filter Specification<Skill> spec, Pageable pageable) {
        ResponsePaginationDTO res = this.skillService.getAllSkills(spec, pageable);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/create")
    public ResponseEntity<Skill> postMethodName(@Valid @RequestBody Skill skill) throws BadRequestException {

        Skill curr = this.skillService.findSkillByName(skill.getName());
        if (curr != null) {
            throw new BadRequestException("Skill already exists");
        }

        Skill entity = this.skillService.create(skill);

        return new ResponseEntity<Skill>(entity, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill entity) throws BadRequestException {
        Skill skill = this.skillService.findSkillById(entity.getId());
        if (skill == null) {
            throw new BadRequestException("Skill not found");
        }
        Skill updatedSkill = this.skillService.update(entity);
        return new ResponseEntity<Skill>(updatedSkill, HttpStatus.OK);
    }

}
