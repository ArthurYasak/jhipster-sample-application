package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportSender.
 */
@Entity
@Table(name = "report_sender")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportSender implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "email_list")
    private String emailList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reportSender")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reportSender", "statisticGenerator", "testEntities" }, allowSetters = true)
    private Set<TestUser> testUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportSender id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmailList() {
        return this.emailList;
    }

    public ReportSender emailList(String emailList) {
        this.setEmailList(emailList);
        return this;
    }

    public void setEmailList(String emailList) {
        this.emailList = emailList;
    }

    public Set<TestUser> getTestUsers() {
        return this.testUsers;
    }

    public void setTestUsers(Set<TestUser> testUsers) {
        if (this.testUsers != null) {
            this.testUsers.forEach(i -> i.setReportSender(null));
        }
        if (testUsers != null) {
            testUsers.forEach(i -> i.setReportSender(this));
        }
        this.testUsers = testUsers;
    }

    public ReportSender testUsers(Set<TestUser> testUsers) {
        this.setTestUsers(testUsers);
        return this;
    }

    public ReportSender addTestUser(TestUser testUser) {
        this.testUsers.add(testUser);
        testUser.setReportSender(this);
        return this;
    }

    public ReportSender removeTestUser(TestUser testUser) {
        this.testUsers.remove(testUser);
        testUser.setReportSender(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportSender)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportSender) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportSender{" +
            "id=" + getId() +
            ", emailList='" + getEmailList() + "'" +
            "}";
    }
}
