package com.example.test.service;

import org.springframework.stereotype.Service;

import com.example.test.domain.Subscriber;
import com.example.test.domain.response.ResponseEmailJob;
import com.example.test.repository.JobRepository;
import com.example.test.repository.SkillRepository;
import com.example.test.repository.SubscriberRepository;
import com.example.test.domain.Job;
import com.example.test.domain.Skill;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final MailService mailService;
    private final JobRepository jobRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            MailService mailService, JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.mailService = mailService;
        this.jobRepository = jobRepository;

    }

    public Subscriber create(Subscriber entity) {
        List<Skill> skills = entity.getSkills();

        if (skills != null) {
            List<Long> skillIds = skills.stream().map(Skill::getId).collect(Collectors.toList());
            List<Skill> skillsInDb = skillRepository.findByIdIn(skillIds);
            entity.setSkills(skillsInDb);
        }

        return subscriberRepository.save(entity);
    }

    public Boolean findByUserEmail(String email) {
        return subscriberRepository.existsByEmail(email);
    }

    public void sendEmailToSubscribers() {

        List<Subscriber> subscribers = this.subscriberRepository.findAll();
        if (subscribers != null) {
            for (Subscriber subscriber : subscribers) {
                List<Skill> skills = subscriber.getSkills();
                if (skills != null && skills.size() > 0) {
                    List<Job> jobs = this.jobRepository.findBySkillsIn(skills);
                    if (jobs != null && jobs.size() > 0) {
                        List<ResponseEmailJob> responseEmailJobs = jobs.stream()
                                .map(job -> job.convertJobToResponseEmailJob(job))
                                .collect(Collectors.toList());
                        this.mailService.sendEmailFromTemplateSync(subscriber.getEmail(),
                                "Cơ hội việc làm dành cho bạn!", "job", subscriber.getName(), responseEmailJobs);
                    }
                }
            }
        }
    }
}
