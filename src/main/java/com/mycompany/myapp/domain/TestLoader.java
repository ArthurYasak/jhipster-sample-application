package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestLoader.
 */
@Entity
@Table(name = "test_loader")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestLoader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tests_amount")
    private Integer testsAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testLoader")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "themeFile", "testCreator", "testLoader", "tester", "testUsers" }, allowSetters = true)
    private Set<TestEntity> testEntities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestLoader id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTestsAmount() {
        return this.testsAmount;
    }

    public TestLoader testsAmount(Integer testsAmount) {
        this.setTestsAmount(testsAmount);
        return this;
    }

    public void setTestsAmount(Integer testsAmount) {
        this.testsAmount = testsAmount;
    }

    public Set<TestEntity> getTestEntities() {
        return this.testEntities;
    }

    public void setTestEntities(Set<TestEntity> testEntities) {
        if (this.testEntities != null) {
            this.testEntities.forEach(i -> i.setTestLoader(null));
        }
        if (testEntities != null) {
            testEntities.forEach(i -> i.setTestLoader(this));
        }
        this.testEntities = testEntities;
    }

    public TestLoader testEntities(Set<TestEntity> testEntities) {
        this.setTestEntities(testEntities);
        return this;
    }

    public TestLoader addTestEntity(TestEntity testEntity) {
        this.testEntities.add(testEntity);
        testEntity.setTestLoader(this);
        return this;
    }

    public TestLoader removeTestEntity(TestEntity testEntity) {
        this.testEntities.remove(testEntity);
        testEntity.setTestLoader(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestLoader)) {
            return false;
        }
        return getId() != null && getId().equals(((TestLoader) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestLoader{" +
            "id=" + getId() +
            ", testsAmount=" + getTestsAmount() +
            "}";
    }
}
