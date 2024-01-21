package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ThemeFile.
 */
@Entity
@Table(name = "theme_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThemeFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "theme")
    private String theme;

    @JsonIgnoreProperties(value = { "materialsFileLoader", "themeFile" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private MaterialsFile materialsFile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "themeFiles" }, allowSetters = true)
    private ThemeFileCreator themeFileCreator;

    @JsonIgnoreProperties(value = { "themeFile", "testCreator", "testLoader", "tester", "testUsers" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "themeFile")
    private TestEntity testEntity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ThemeFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheme() {
        return this.theme;
    }

    public ThemeFile theme(String theme) {
        this.setTheme(theme);
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public MaterialsFile getMaterialsFile() {
        return this.materialsFile;
    }

    public void setMaterialsFile(MaterialsFile materialsFile) {
        this.materialsFile = materialsFile;
    }

    public ThemeFile materialsFile(MaterialsFile materialsFile) {
        this.setMaterialsFile(materialsFile);
        return this;
    }

    public ThemeFileCreator getThemeFileCreator() {
        return this.themeFileCreator;
    }

    public void setThemeFileCreator(ThemeFileCreator themeFileCreator) {
        this.themeFileCreator = themeFileCreator;
    }

    public ThemeFile themeFileCreator(ThemeFileCreator themeFileCreator) {
        this.setThemeFileCreator(themeFileCreator);
        return this;
    }

    public TestEntity getTestEntity() {
        return this.testEntity;
    }

    public void setTestEntity(TestEntity testEntity) {
        if (this.testEntity != null) {
            this.testEntity.setThemeFile(null);
        }
        if (testEntity != null) {
            testEntity.setThemeFile(this);
        }
        this.testEntity = testEntity;
    }

    public ThemeFile testEntity(TestEntity testEntity) {
        this.setTestEntity(testEntity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeFile)) {
            return false;
        }
        return getId() != null && getId().equals(((ThemeFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeFile{" +
            "id=" + getId() +
            ", theme='" + getTheme() + "'" +
            "}";
    }
}
