package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.TestEntity;
import com.mycompany.myapp.repository.TestEntityRepository;
import com.mycompany.myapp.service.TestEntityService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.TestEntity}.
 */
@Service
@Transactional
public class TestEntityServiceImpl implements TestEntityService {

    private final Logger log = LoggerFactory.getLogger(TestEntityServiceImpl.class);

    private final TestEntityRepository testEntityRepository;

    public TestEntityServiceImpl(TestEntityRepository testEntityRepository) {
        this.testEntityRepository = testEntityRepository;
    }

    @Override
    public TestEntity save(TestEntity testEntity) {
        log.debug("Request to save TestEntity : {}", testEntity);
        return testEntityRepository.save(testEntity);
    }

    @Override
    public TestEntity update(TestEntity testEntity) {
        log.debug("Request to update TestEntity : {}", testEntity);
        return testEntityRepository.save(testEntity);
    }

    @Override
    public Optional<TestEntity> partialUpdate(TestEntity testEntity) {
        log.debug("Request to partially update TestEntity : {}", testEntity);

        return testEntityRepository
            .findById(testEntity.getId())
            .map(existingTestEntity -> {
                if (testEntity.getQuestion() != null) {
                    existingTestEntity.setQuestion(testEntity.getQuestion());
                }
                if (testEntity.getTestPoints() != null) {
                    existingTestEntity.setTestPoints(testEntity.getTestPoints());
                }
                if (testEntity.getResult() != null) {
                    existingTestEntity.setResult(testEntity.getResult());
                }

                return existingTestEntity;
            })
            .map(testEntityRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TestEntity> findAll(Pageable pageable) {
        log.debug("Request to get all TestEntities");
        return testEntityRepository.findAll(pageable);
    }

    public Page<TestEntity> findAllWithEagerRelationships(Pageable pageable) {
        return testEntityRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestEntity> findOne(Long id) {
        log.debug("Request to get TestEntity : {}", id);
        return testEntityRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestEntity : {}", id);
        testEntityRepository.deleteById(id);
    }
}
