package com.company.service;

import com.company.domain.TestEntity;
import com.company.repository.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by AlexandruD on 15-May-17.
 */
@Component
public class TestEntityService {

    private TestEntityRepository repo;

    public TestEntityService(@Autowired TestEntityRepository repo) {
        this.repo = repo;
    }

    public TestEntity addTestEntity(TestEntity testEntity) {
        repo.save(testEntity);
        return testEntity;
    }

    public void removeTestEntity(Integer id) {
        repo.delete(id);
    }

    public void updateTestEntity(Integer id, TestEntity update) {
        TestEntity c = repo.findOne(id);
        if(c == null) {
            return;
        }
        if(update.getString() != null) {
            c.setString(update.getString());
            repo.save(c);
        }
    }

    public TestEntity getCaca(Integer id) {
        return repo.findOne(id);
    }

    public Iterable<TestEntity> getAllCaca() {
        return repo.findAll();
    }

}
