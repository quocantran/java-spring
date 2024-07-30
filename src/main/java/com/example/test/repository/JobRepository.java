package com.example.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.test.domain.Company;
import com.example.test.domain.Job;
import com.example.test.domain.Skill;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

    Job save(Job job);

    Job findByName(String name);

    Job findByCompany(Company company);

    Job findById(long id);

    Boolean existsById(long id);

    Boolean existsByName(String name);

    Boolean existsByCompany(Company company);

    Boolean existsByNameAndCompany(String name, Company company);

    List<Job> findBySkillsIn(List<Skill> skills);

}
