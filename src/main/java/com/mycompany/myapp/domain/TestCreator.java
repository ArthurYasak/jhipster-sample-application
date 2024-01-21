package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestCreator.
 */
@Entity
@Table(name = "test_creator")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "tests_amount")
    private Integer testsAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testCreator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "themeFile", "testCreator", "testLoader", "tester", "testUsers" }, allowSetters = true)
    private Set<TestEntity> testEntities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCreator id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTestsAmount() {
        return this.testsAmount;
    }

    public TestCreator testsAmount(Integer testsAmount) {
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
            this.testEntities.forEach(i -> i.setTestCreator(null));
        }
        if (testEntities != null) {
            testEntities.forEach(i -> i.setTestCreator(this));
        }
        this.testEntities = testEntities;
    }

    public TestCreator testEntities(Set<TestEntity> testEntities) {
        this.setTestEntities(testEntities);
        return this;
    }

    public TestCreator addTestEntity(TestEntity testEntity) {
        this.testEntities.add(testEntity);
        testEntity.setTestCreator(this);
        return this;
    }

    public TestCreator removeTestEntity(TestEntity testEntity) {
        this.testEntities.remove(testEntity);
        testEntity.setTestCreator(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCreator)) {
            return false;
        }
        return getId() != null && getId().equals(((TestCreator) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCreator{" +
            "id=" + getId() +
            ", testsAmount=" + getTestsAmount() +
            "}";
    }
}
