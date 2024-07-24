package com.example.test.service;

import org.springframework.stereotype.Service;

import com.example.test.domain.Company;
import com.example.test.domain.Job;
import com.example.test.repository.JobRepository;
import com.example.test.domain.Skill;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillService skillService;
    private final CompanyService companyService;

    public JobService(JobRepository jobRepository, SkillService skillService, CompanyService companyService) {
        this.jobRepository = jobRepository;
        this.skillService = skillService;
        this.companyService = companyService;
    }

    public Job create(Job job) {
        List<Long> ids = job.getSkills().stream().map(Skill::getId).collect(Collectors.toList());

        List<Skill> skills = this.skillService.findSkillByIdIn(ids);
        Company company = this.companyService.getCompanyById(job.getCompany().getId());
        job.setCompany(company);
        job.setSkills(skills);

        return this.jobRepository.save(job);
    }

    public Boolean findJobByNameAndCompany(String name, Company company) {
        return this.jobRepository.existsByNameAndCompany(name, company);
    }

}
