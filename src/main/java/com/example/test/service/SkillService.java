package com.example.test.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.test.domain.Skill;
import com.example.test.domain.User;
import com.example.test.domain.response.ResponseMetaDTO;
import com.example.test.domain.response.ResponsePaginationDTO;
import com.example.test.domain.response.ResponseUserDTO;
import com.example.test.repository.SkillRepository;
import java.util.List;

@Service
public class SkillService {

    private SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill create(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public ResponsePaginationDTO getAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> skill = this.skillRepository.findAll(spec, pageable);

        ResponsePaginationDTO resultPaginationDTO = new ResponsePaginationDTO();

        ResponseMetaDTO meta = new ResponseMetaDTO();

        meta.setPage(skill.getNumber() + 1);
        meta.setPageSize(skill.getSize());

        meta.setPages(skill.getTotalPages());
        meta.setTotal(skill.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        resultPaginationDTO.setResult(skill.getContent());
        return resultPaginationDTO;
    }

    public Skill findSkillByName(String name) {
        return this.skillRepository.findByName(name);
    }

    public List<Skill> findSkillByIdIn(List<Long> skill) {
        return this.skillRepository.findByIdIn(skill);
    }

}
