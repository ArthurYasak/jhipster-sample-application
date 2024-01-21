package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ThemeFileCreator.
 */
@Entity
@Table(name = "theme_file_creator")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ThemeFileCreator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "files_amount")
    private Integer filesAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "themeFileCreator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materialsFile", "themeFileCreator", "testEntity" }, allowSetters = true)
    private Set<ThemeFile> themeFiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ThemeFileCreator id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFilesAmount() {
        return this.filesAmount;
    }

    public ThemeFileCreator filesAmount(Integer filesAmount) {
        this.setFilesAmount(filesAmount);
        return this;
    }

    public void setFilesAmount(Integer filesAmount) {
        this.filesAmount = filesAmount;
    }

    public Set<ThemeFile> getThemeFiles() {
        return this.themeFiles;
    }

    public void setThemeFiles(Set<ThemeFile> themeFiles) {
        if (this.themeFiles != null) {
            this.themeFiles.forEach(i -> i.setThemeFileCreator(null));
        }
        if (themeFiles != null) {
            themeFiles.forEach(i -> i.setThemeFileCreator(this));
        }
        this.themeFiles = themeFiles;
    }

    public ThemeFileCreator themeFiles(Set<ThemeFile> themeFiles) {
        this.setThemeFiles(themeFiles);
        return this;
    }

    public ThemeFileCreator addThemeFile(ThemeFile themeFile) {
        this.themeFiles.add(themeFile);
        themeFile.setThemeFileCreator(this);
        return this;
    }

    public ThemeFileCreator removeThemeFile(ThemeFile themeFile) {
        this.themeFiles.remove(themeFile);
        themeFile.setThemeFileCreator(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ThemeFileCreator)) {
            return false;
        }
        return getId() != null && getId().equals(((ThemeFileCreator) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ThemeFileCreator{" +
            "id=" + getId() +
            ", filesAmount=" + getFilesAmount() +
            "}";
    }
}
