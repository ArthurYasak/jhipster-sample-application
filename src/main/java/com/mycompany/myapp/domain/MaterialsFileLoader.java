package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MaterialsFileLoader.
 */
@Entity
@Table(name = "materials_file_loader")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterialsFileLoader implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "files_amount")
    private Integer filesAmount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "materialsFileLoader")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "materialsFileLoader", "themeFile" }, allowSetters = true)
    private Set<MaterialsFile> materialsFiles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MaterialsFileLoader id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFilesAmount() {
        return this.filesAmount;
    }

    public MaterialsFileLoader filesAmount(Integer filesAmount) {
        this.setFilesAmount(filesAmount);
        return this;
    }

    public void setFilesAmount(Integer filesAmount) {
        this.filesAmount = filesAmount;
    }

    public Set<MaterialsFile> getMaterialsFiles() {
        return this.materialsFiles;
    }

    public void setMaterialsFiles(Set<MaterialsFile> materialsFiles) {
        if (this.materialsFiles != null) {
            this.materialsFiles.forEach(i -> i.setMaterialsFileLoader(null));
        }
        if (materialsFiles != null) {
            materialsFiles.forEach(i -> i.setMaterialsFileLoader(this));
        }
        this.materialsFiles = materialsFiles;
    }

    public MaterialsFileLoader materialsFiles(Set<MaterialsFile> materialsFiles) {
        this.setMaterialsFiles(materialsFiles);
        return this;
    }

    public MaterialsFileLoader addMaterialsFile(MaterialsFile materialsFile) {
        this.materialsFiles.add(materialsFile);
        materialsFile.setMaterialsFileLoader(this);
        return this;
    }

    public MaterialsFileLoader removeMaterialsFile(MaterialsFile materialsFile) {
        this.materialsFiles.remove(materialsFile);
        materialsFile.setMaterialsFileLoader(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterialsFileLoader)) {
            return false;
        }
        return getId() != null && getId().equals(((MaterialsFileLoader) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterialsFileLoader{" +
            "id=" + getId() +
            ", filesAmount=" + getFilesAmount() +
            "}";
    }
}
