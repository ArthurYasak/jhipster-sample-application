package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tester.
 */
@Entity
@Table(name = "tester")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tester implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "hold_tests")
    private Integer holdTests;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tester")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "themeFile", "testCreator", "testLoader", "tester", "testUsers" }, allowSetters = true)
    private Set<TestEntity> testEntities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tester id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHoldTests() {
        return this.holdTests;
    }

    public Tester holdTests(Integer holdTests) {
        this.setHoldTests(holdTests);
        return this;
    }

    public void setHoldTests(Integer holdTests) {
        this.holdTests = holdTests;
    }

    public Set<TestEntity> getTestEntities() {
        return this.testEntities;
    }

    public void setTestEntities(Set<TestEntity> testEntities) {
        if (this.testEntities != null) {
            this.testEntities.forEach(i -> i.setTester(null));
        }
        if (testEntities != null) {
            testEntities.forEach(i -> i.setTester(this));
        }
        this.testEntities = testEntities;
    }

    public Tester testEntities(Set<TestEntity> testEntities) {
        this.setTestEntities(testEntities);
        return this;
    }

    public Tester addTestEntity(TestEntity testEntity) {
        this.testEntities.add(testEntity);
        testEntity.setTester(this);
        return this;
    }

    public Tester removeTestEntity(TestEntity testEntity) {
        this.testEntities.remove(testEntity);
        testEntity.setTester(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tester)) {
            return false;
        }
        return getId() != null && getId().equals(((Tester) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tester{" +
            "id=" + getId() +
            ", holdTests=" + getHoldTests() +
            "}";
    }
}
