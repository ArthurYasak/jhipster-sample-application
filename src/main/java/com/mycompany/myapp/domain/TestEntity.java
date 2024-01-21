package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestEntity.
 */
@Entity
@Table(name = "test_entity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "test_points")
    private String testPoints;

    @Column(name = "result")
    private Integer result;

    @JsonIgnoreProperties(value = { "materialsFile", "themeFileCreator", "testEntity" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ThemeFile themeFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testEntities" }, allowSetters = true)
    private TestCreator testCreator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testEntities" }, allowSetters = true)
    private TestLoader testLoader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testEntities" }, allowSetters = true)
    private Tester tester;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_test_entity__test_user",
        joinColumns = @JoinColumn(name = "test_entity_id"),
        inverseJoinColumns = @JoinColumn(name = "test_user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "reportSender", "statisticGenerator", "testEntities" }, allowSetters = true)
    private Set<TestUser> testUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestEntity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return this.question;
    }

    public TestEntity question(String question) {
        this.setQuestion(question);
        return this;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTestPoints() {
        return this.testPoints;
    }

    public TestEntity testPoints(String testPoints) {
        this.setTestPoints(testPoints);
        return this;
    }

    public void setTestPoints(String testPoints) {
        this.testPoints = testPoints;
    }

    public Integer getResult() {
        return this.result;
    }

    public TestEntity result(Integer result) {
        this.setResult(result);
        return this;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ThemeFile getThemeFile() {
        return this.themeFile;
    }

    public void setThemeFile(ThemeFile themeFile) {
        this.themeFile = themeFile;
    }

    public TestEntity themeFile(ThemeFile themeFile) {
        this.setThemeFile(themeFile);
        return this;
    }

    public TestCreator getTestCreator() {
        return this.testCreator;
    }

    public void setTestCreator(TestCreator testCreator) {
        this.testCreator = testCreator;
    }

    public TestEntity testCreator(TestCreator testCreator) {
        this.setTestCreator(testCreator);
        return this;
    }

    public TestLoader getTestLoader() {
        return this.testLoader;
    }

    public void setTestLoader(TestLoader testLoader) {
        this.testLoader = testLoader;
    }

    public TestEntity testLoader(TestLoader testLoader) {
        this.setTestLoader(testLoader);
        return this;
    }

    public Tester getTester() {
        return this.tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public TestEntity tester(Tester tester) {
        this.setTester(tester);
        return this;
    }

    public Set<TestUser> getTestUsers() {
        return this.testUsers;
    }

    public void setTestUsers(Set<TestUser> testUsers) {
        this.testUsers = testUsers;
    }

    public TestEntity testUsers(Set<TestUser> testUsers) {
        this.setTestUsers(testUsers);
        return this;
    }

    public TestEntity addTestUser(TestUser testUser) {
        this.testUsers.add(testUser);
        return this;
    }

    public TestEntity removeTestUser(TestUser testUser) {
        this.testUsers.remove(testUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestEntity)) {
            return false;
        }
        return getId() != null && getId().equals(((TestEntity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestEntity{" +
            "id=" + getId() +
            ", question='" + getQuestion() + "'" +
            ", testPoints='" + getTestPoints() + "'" +
            ", result=" + getResult() +
            "}";
    }
}
