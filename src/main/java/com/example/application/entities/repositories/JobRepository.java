package com.example.application.entities.repositories;

import com.example.application.entities.Job;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class JobRepository {

    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    public JobRepository() {
        entityManagerFactory = Persistence.createEntityManagerFactory("job_pu");
        this.entityManager = entityManagerFactory.createEntityManager();
    }

    public Job save(Job job) {
        entityManager.getTransaction().begin();
        entityManager.persist(job);
        entityManager.getTransaction().commit();
        return job;
    }

    public List<Job> saveAll(Iterable<Job> jobs) {
        for (Job job : jobs) {
            entityManager.getTransaction().begin();
            entityManager.persist(job);
            entityManager.getTransaction().commit();
        }
        return (List<Job>) jobs;
    }

    public List<Job> findByTitle(String title) {
        //For single result replace getResultList() with getSingleResult():
        return entityManager.createQuery("from Job as job where job.title = :titleValue", Job.class)
                .setParameter("titleValue", title)
                .getResultList();
    }

    public Job findById(Long id) {
        return entityManager.find(Job.class, id);
    }

    public List<Job> findAll() {
        return entityManager.createQuery("SELECT j FROM Job j", Job.class).getResultList();
    }

    public List<Job> findAllById(Iterable<? extends Long> ids) {
        List<Job> jobs = new ArrayList<>();

        List<Long> idList = new ArrayList<>();
        ids.forEach(idList::add);

        for (Job job : findAll()) {
            if (idList.contains(job.getId()))
                jobs.add(job);
        }
        return jobs;
    }

    public long jobCount() {
        return findAll().size();
    }

    public boolean existsById(Long id) {
        return Objects.nonNull(findById(id));
    }

    public Job updateJobById(Long id, Job job) {
        Job jobToUpdate = entityManager.find(Job.class, id);

        if (jobToUpdate != null) {
            entityManager.getTransaction().begin();
            jobToUpdate.setJobCardBulletPoints(job.getJobCardBulletPoints());
            jobToUpdate.setJsonJob(job.getJsonJob());
            jobToUpdate.setAdvertiserName(job.getAdvertiserName());
            jobToUpdate.setAdvertiserId(job.getAdvertiserId());
            jobToUpdate.setArea(job.getArea());
            jobToUpdate.setAreaId(job.getAreaId());
            jobToUpdate.setLocation(job.getLocation());
            jobToUpdate.setLocationId(job.getLocationId());
            jobToUpdate.setLogoLink(job.getLogoLink());
            jobToUpdate.setCompany(job.getCompany());
            jobToUpdate.setCompanyId(job.getCompanyId());
            jobToUpdate.setJobClassification(job.getJobClassification());
            jobToUpdate.setJobClassificationId(job.getJobClassificationId());
            jobToUpdate.setPremium(job.isPremium());
            jobToUpdate.setJobId(job.getJobId());
            jobToUpdate.setListingDateTime(job.getListingDateTime());
            jobToUpdate.setSalary(job.getSalary());
            jobToUpdate.setTeaser(job.getTeaser());
            jobToUpdate.setTitle(job.getTitle());
            jobToUpdate.setJobSubClassification(job.getJobSubClassification());
            jobToUpdate.setJobSubClassificationId(job.getJobSubClassificationId());
            jobToUpdate.setWorkType(job.getWorkType());

            entityManager.getTransaction().commit();
            return jobToUpdate;
        }
        return null;
    }

    public Job updateJob(Job job) {
        return updateJobById(job.getId(), job);
    }

    public void delete(Job job) {
        if (Objects.nonNull(job)) {
            entityManager.getTransaction().begin();
            entityManager.remove(job);
            entityManager.getTransaction().commit();
        }
    }

    public void deleteById(Long id) {
        Job foundJob = entityManager.find(Job.class, id);
        if (Objects.nonNull(foundJob)) {
            entityManager.getTransaction().begin();
            entityManager.remove(foundJob);
            entityManager.getTransaction().commit();
        }
    }

    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    public void deleteAll(Iterable<? extends Job> jobs) {
        jobs.forEach(this::delete);
    }

    public void deleteAll() {
//        entityManager.getTransaction().begin();
//        entityManager.createQuery("TRUNCATE TABLE jobsscraping").executeUpdate();
//        entityManager.getTransaction().commit();
        findAll().forEach(this::delete);
    }

    public void close() {
        entityManager.close();
        entityManagerFactory.close();
    }

//    public void resetAutoIncrement(){
//        entityManager.createQuery("DBCC CHECKIDENT('jobsscraping', RESEED, 0)").executeUpdate();
//    }
}
