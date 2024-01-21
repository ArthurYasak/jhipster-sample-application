package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StatisticGenerator.
 */
@Entity
@Table(name = "statistic_generator")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StatisticGenerator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "generated_reports_amount")
    private Integer generatedReportsAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "statisticGenerator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reportSender", "statisticGenerator", "testEntities" }, allowSetters = true)
    private Set<TestUser> testUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StatisticGenerator id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGeneratedReportsAmount() {
        return this.generatedReportsAmount;
    }

    public StatisticGenerator generatedReportsAmount(Integer generatedReportsAmount) {
        this.setGeneratedReportsAmount(generatedReportsAmount);
        return this;
    }

    public void setGeneratedReportsAmount(Integer generatedReportsAmount) {
        this.generatedReportsAmount = generatedReportsAmount;
    }

    public Set<TestUser> getTestUsers() {
        return this.testUsers;
    }

    public void setTestUsers(Set<TestUser> testUsers) {
        if (this.testUsers != null) {
            this.testUsers.forEach(i -> i.setStatisticGenerator(null));
        }
        if (testUsers != null) {
            testUsers.forEach(i -> i.setStatisticGenerator(this));
        }
        this.testUsers = testUsers;
    }

    public StatisticGenerator testUsers(Set<TestUser> testUsers) {
        this.setTestUsers(testUsers);
        return this;
    }

    public StatisticGenerator addTestUser(TestUser testUser) {
        this.testUsers.add(testUser);
        testUser.setStatisticGenerator(this);
        return this;
    }

    public StatisticGenerator removeTestUser(TestUser testUser) {
        this.testUsers.remove(testUser);
        testUser.setStatisticGenerator(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StatisticGenerator)) {
            return false;
        }
        return getId() != null && getId().equals(((StatisticGenerator) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StatisticGenerator{" +
            "id=" + getId() +
            ", generatedReportsAmount=" + getGeneratedReportsAmount() +
            "}";
    }
}
