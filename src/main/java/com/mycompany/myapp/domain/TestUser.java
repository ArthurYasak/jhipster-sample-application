package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestUser.
 */
@Entity
@Table(name = "test_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "marks")
    private Float marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testUsers" }, allowSetters = true)
    private ReportSender reportSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testUsers" }, allowSetters = true)
    private StatisticGenerator statisticGenerator;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "testUsers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "themeFile", "testCreator", "testLoader", "tester", "testUsers" }, allowSetters = true)
    private Set<TestEntity> testEntities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getMarks() {
        return this.marks;
    }

    public TestUser marks(Float marks) {
        this.setMarks(marks);
        return this;
    }

    public void setMarks(Float marks) {
        this.marks = marks;
    }

    public ReportSender getReportSender() {
        return this.reportSender;
    }

    public void setReportSender(ReportSender reportSender) {
        this.reportSender = reportSender;
    }

    public TestUser reportSender(ReportSender reportSender) {
        this.setReportSender(reportSender);
        return this;
    }

    public StatisticGenerator getStatisticGenerator() {
        return this.statisticGenerator;
    }

    public void setStatisticGenerator(StatisticGenerator statisticGenerator) {
        this.statisticGenerator = statisticGenerator;
    }

    public TestUser statisticGenerator(StatisticGenerator statisticGenerator) {
        this.setStatisticGenerator(statisticGenerator);
        return this;
    }

    public Set<TestEntity> getTestEntities() {
        return this.testEntities;
    }

    public void setTestEntities(Set<TestEntity> testEntities) {
        if (this.testEntities != null) {
            this.testEntities.forEach(i -> i.removeTestUser(this));
        }
        if (testEntities != null) {
            testEntities.forEach(i -> i.addTestUser(this));
        }
        this.testEntities = testEntities;
    }

    public TestUser testEntities(Set<TestEntity> testEntities) {
        this.setTestEntities(testEntities);
        return this;
    }

    public TestUser addTestEntity(TestEntity testEntity) {
        this.testEntities.add(testEntity);
        testEntity.getTestUsers().add(this);
        return this;
    }

    public TestUser removeTestEntity(TestEntity testEntity) {
        this.testEntities.remove(testEntity);
        testEntity.getTestUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestUser)) {
            return false;
        }
        return getId() != null && getId().equals(((TestUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestUser{" +
            "id=" + getId() +
            ", marks=" + getMarks() +
            "}";
    }
}
