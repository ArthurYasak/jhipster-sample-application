package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MaterialsFile.
 */
@Entity
@Table(name = "materials_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialsFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "materials")
    private String materials;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "materialsFiles" }, allowSetters = true)
    private MaterialsFileLoader materialsFileLoader;

    @JsonIgnoreProperties(value = { "materialsFile", "themeFileCreator", "testEntity" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "materialsFile")
    private ThemeFile themeFile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialsFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaterials() {
        return this.materials;
    }

    public MaterialsFile materials(String materials) {
        this.setMaterials(materials);
        return this;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public MaterialsFileLoader getMaterialsFileLoader() {
        return this.materialsFileLoader;
    }

    public void setMaterialsFileLoader(MaterialsFileLoader materialsFileLoader) {
        this.materialsFileLoader = materialsFileLoader;
    }

    public MaterialsFile materialsFileLoader(MaterialsFileLoader materialsFileLoader) {
        this.setMaterialsFileLoader(materialsFileLoader);
        return this;
    }

    public ThemeFile getThemeFile() {
        return this.themeFile;
    }

    public void setThemeFile(ThemeFile themeFile) {
        if (this.themeFile != null) {
            this.themeFile.setMaterialsFile(null);
        }
        if (themeFile != null) {
            themeFile.setMaterialsFile(this);
        }
        this.themeFile = themeFile;
    }

    public MaterialsFile themeFile(ThemeFile themeFile) {
        this.setThemeFile(themeFile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialsFile)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialsFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialsFile{" +
            "id=" + getId() +
            ", materials='" + getMaterials() + "'" +
            "}";
    }
}
