//package com.example.application.entities;
//
////import com.example.application.entities.repositories.JobRepository;
//import com.example.application.entities.repositories.JobRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Persistence;
//import java.util.Objects;
//
//@Service
//public class JobTest {
////    @Autowired
//    private JobRepository jobRepo = new JobRepository();
//
//    public JobTest() {
//    }
//
//    public void runTestConnection() {
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("job_pu");
//        print("entityManagerFactory isNull: " + isNull(entityManagerFactory));
//        print("entityManagerFactory isOpen: " + entityManagerFactory.isOpen());
//        print("entityManagerFactory properties: " + entityManagerFactory.getProperties());
//
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        print("entityManager isNull: "+isNull(entityManager));
//        print("entityManager isOpen: " + entityManager.isOpen());
//        print("entityManager properties: " + entityManager.getProperties());
//
//        EntityTransaction entityTransaction = entityManager.getTransaction();
//        print("entityTransaction isNull: "+isNull(entityTransaction));
//        print("entityTransaction isActive: " + entityTransaction.isActive());
//        print("entityTransaction : " + entityTransaction.toString());
//
//        entityManager.close();
//        entityManagerFactory.close();
//    }
//
//    private boolean isNull(Object object) {
//        return Objects.isNull(object);
//    }
//
//    private void print(String message) {
//        System.out.println(message);
//    }
//}
