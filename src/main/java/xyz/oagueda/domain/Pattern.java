package xyz.oagueda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The hasPattern entity is used to
 * represent classic Android pattern lock.
 * @author oagueda.
 */
@Entity
@Table(name = "pattern")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pattern implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @JsonIgnoreProperties(value = { "pattern", "repairs", "customer" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "pattern")
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pattern id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Pattern code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        if (this.device != null) {
            this.device.setPattern(null);
        }
        if (device != null) {
            device.setPattern(this);
        }
        this.device = device;
    }

    public Pattern device(Device device) {
        this.setDevice(device);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pattern)) {
            return false;
        }
        return getId() != null && getId().equals(((Pattern) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pattern{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
