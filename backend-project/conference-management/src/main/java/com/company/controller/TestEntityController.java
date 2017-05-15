package com.company.controller;

import com.company.domain.TestEntity;
import com.company.service.TestEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by AlexandruD on 15-May-17.
 */
@RestController
@RequestMapping("/testEntity")
public class TestEntityController {

    private TestEntityService service;

    public TestEntityController(@Autowired TestEntityService service) {
        this.service = service;
    }

    @RequestMapping(method=RequestMethod.GET)
    public ResponseEntity<Iterable<TestEntity>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(service.getAllCaca());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<TestEntity> getTestEntity(@PathVariable("id") Integer id) {
        TestEntity c = service.getCaca(id);
        if(c != null)
            return ResponseEntity.status(HttpStatus.OK).body(c);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<TestEntity> createTestEntity(@RequestBody TestEntity testEntity) {
        TestEntity c = service.addTestEntity(testEntity);
        if(c.getId() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(c);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteTestEntity(@PathVariable("id") Integer id) {
        service.removeTestEntity(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
