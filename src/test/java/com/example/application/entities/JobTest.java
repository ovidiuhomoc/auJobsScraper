package com.example.application.entities;

import com.example.application.entities.repositories.JobRepository;

import org.junit.Assert;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class JobTest {

    //    @Autowired
    private static final JobRepository jobRepo = new JobRepository();
    private Long firstJobId;
    private Long secondJobId;

    @BeforeAll
    public static void setUpClass() {
        System.out.println();
        System.out.println(" --------------------  @BeforeAll  -------------------- ");
        System.out.println();
        //backup also DB content
    }

    @AfterAll
    public static void tearDownClass() {
        System.out.println();
        System.out.println(" --------------------  @AfterAll  -------------------- ");
        System.out.println();
        //restore to DB content
        //
        jobRepo.close();
    }

    @BeforeEach
    public void setUp() {
        System.out.println();
        System.out.println(" --------------------  @BeforeEach  -------------------- ");
        System.out.println();

        jobRepo.deleteAll();

        Job job1 = Job.builder()
                .title("Java Developer")
                .company("Google")
                .jobId(12345)
                .jobCardBulletPoints(List.of("Bullet point 1", "Bullet point 2"))
                .platformOrigin(JobPlatformOrigin.Seek)
                .build();

        Job job2 = Job.builder()
                .title("C++ Developer")
                .company("Microsoft")
                .jobId(54321)
                .jobCardBulletPoints(List.of("Bullet point 4", "Bullet point 5"))
                .platformOrigin(JobPlatformOrigin.LinkedIn)
                .build();


        Assert.assertNull(job1.getId());
        Assert.assertNull(job2.getId());
        jobRepo.save(job1);
        jobRepo.save(job2);
        Assert.assertNotNull(job1.getId());
        Assert.assertNotNull(job2.getId());

        firstJobId = job1.getId();
        secondJobId = job2.getId();
    }

    @Test
    public void testFindJobById() {
        Job job = jobRepo.findById(firstJobId);
        Assert.assertNotNull(job);
        Assert.assertEquals("Google", job.getCompany());
    }

    @Test
    public void testFindByTitle() {
        List<Job> jobs = jobRepo.findByTitle("Java Developer");
        Assert.assertNotNull(jobs);
        Assert.assertEquals(1, jobs.size());
        Assert.assertEquals("Google", jobs.get(0).getCompany());
    }

    @Test
    public void testFindAllJobs() {
        List<Job> jobs = jobRepo.findAll();
        Assert.assertNotNull(jobs);
        Assert.assertEquals(2, jobs.size());
        for (Job job : jobs) {
            System.out.println(job.toString());
        }
    }

    @Test
    public void testFindAllById() {
        List<Job> jobs = jobRepo.findAllById(List.of(firstJobId, secondJobId));
        Assert.assertNotNull(jobs);
        Assert.assertEquals(2, jobs.size());
        Assert.assertEquals("Google", jobs.get(0).getCompany());
        Assert.assertEquals("Microsoft", jobs.get(1).getCompany());
    }

    @Test
    public void testSaveAll() {
        Job job3 = Job.builder()
                .title("Junior Java Developer")
                .company("NAB")
                .jobId(69)
                .platformOrigin(JobPlatformOrigin.Seek)
                .build();

        Job job4 = Job.builder()
                .title("Full Stack Java Developer")
                .company("Facebook")
                .jobId(100)
                .jobCardBulletPoints(List.of("Super working space", "Great team"))
                .platformOrigin(JobPlatformOrigin.LinkedIn)
                .build();

        jobRepo.saveAll(List.of(job3, job4));
        List<Job> jobs = jobRepo.findAll();
        Assert.assertNotNull(jobs);
        Assert.assertEquals(4, jobs.size());
        Assert.assertEquals("NAB", jobs.get(2).getCompany());
        Assert.assertEquals("Facebook", jobs.get(3).getCompany());
    }

    @Test
    public void testJobCount() {
        long count = jobRepo.jobCount();
        Assert.assertEquals(2, count);
    }

    @Test
    public void testExistsById() {
        Assert.assertTrue(jobRepo.existsById(firstJobId));
        Assert.assertFalse(jobRepo.existsById(secondJobId + 1));
    }

    @Test
    public void testUpdateJob() {
        Job foundJob = jobRepo.findById(firstJobId);
        Assert.assertNotNull(foundJob);
        foundJob.setCompany("Apple");
        jobRepo.updateJob(foundJob);

        Job updatedJob = jobRepo.findById(firstJobId);
        Assert.assertEquals("Apple", updatedJob.getCompany());
    }

    @Test
    public void testUpdateJobById() {
        Job foundJob = jobRepo.findById(firstJobId);
        Assert.assertNotNull(foundJob);
        foundJob.setCompany("FujiFilm");
        jobRepo.updateJobById(firstJobId, foundJob);

        Job updatedJob = jobRepo.findById(firstJobId);
        Assert.assertEquals("FujiFilm", updatedJob.getCompany());
    }

    @Test
    public void testDeleteJob() {
        Job foundJob = jobRepo.findById(firstJobId);
        Assert.assertNotNull(foundJob);
        jobRepo.delete(foundJob);

        Job deletedJob = jobRepo.findById(firstJobId);
        Assert.assertNull(deletedJob);
    }

    @Test
    public void testDeleteJobById() {
        Job foundJob = jobRepo.findById(firstJobId);
        Assert.assertNotNull(foundJob);
        jobRepo.deleteById(firstJobId);

        Job deletedJob = jobRepo.findById(firstJobId);
        Assert.assertNull(deletedJob);
    }

    @Test
    public void testDeleteAll() {
        jobRepo.deleteAll();
        List<Job> jobs = jobRepo.findAll();
        Assert.assertNotNull(jobs);
        Assert.assertEquals(0, jobs.size());
    }

    @Test
    public void testDeleteAllById() {
        jobRepo.deleteAllById(List.of(firstJobId, secondJobId));
        List<Job> jobs = jobRepo.findAll();
        Assert.assertNotNull(jobs);
        Assert.assertEquals(0, jobs.size());
    }

    @Test
    public void testDeleteAllJobs() {
        List<Job> jobs = jobRepo.findAll();
        Assert.assertNotNull(jobs);
        Assert.assertNotEquals(0, jobs.size());

        jobRepo.deleteAll(jobs);
        jobs = jobRepo.findAll();
        Assert.assertEquals(0, jobs.size());
    }
}