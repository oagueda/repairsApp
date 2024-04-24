package xyz.oagueda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import xyz.oagueda.domain.enumeration.Type;

/**
 * Device entity.
 * @author oagueda.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The type property is an enumeration
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "serial_number")
    private String serialNumber;

    @NotNull
    @Column(name = "warranty", nullable = false)
    private Boolean warranty;

    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "has_pattern", nullable = false)
    private Boolean hasPattern;

    @Column(name = "sim_pin_code")
    private String simPinCode;

    @Lob
    @Column(name = "notes")
    private String notes;

    /**
     * Field for logical deletion
     * should be false by default
     */
    @NotNull
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @JsonIgnoreProperties(value = { "device" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Pattern pattern;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "device" }, allowSetters = true)
    private Set<Repair> repairs = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "devices" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Device id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return this.type;
    }

    public Device type(Type type) {
        this.setType(type);
        return this;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getBrand() {
        return this.brand;
    }

    public Device brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return this.model;
    }

    public Device model(String model) {
        this.setModel(model);
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public Device serialNumber(String serialNumber) {
        this.setSerialNumber(serialNumber);
        return this;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getWarranty() {
        return this.warranty;
    }

    public Device warranty(Boolean warranty) {
        this.setWarranty(warranty);
        return this;
    }

    public void setWarranty(Boolean warranty) {
        this.warranty = warranty;
    }

    public String getPassword() {
        return this.password;
    }

    public Device password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasPattern() {
        return this.hasPattern;
    }

    public Device hasPattern(Boolean hasPattern) {
        this.setHasPattern(hasPattern);
        return this;
    }

    public void setHasPattern(Boolean hasPattern) {
        this.hasPattern = hasPattern;
    }

    public String getSimPinCode() {
        return this.simPinCode;
    }

    public Device simPinCode(String simPinCode) {
        this.setSimPinCode(simPinCode);
        return this;
    }

    public void setSimPinCode(String simPinCode) {
        this.simPinCode = simPinCode;
    }

    public String getNotes() {
        return this.notes;
    }

    public Device notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Device deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Device pattern(Pattern pattern) {
        this.setPattern(pattern);
        return this;
    }

    public Set<Repair> getRepairs() {
        return this.repairs;
    }

    public void setRepairs(Set<Repair> repairs) {
        if (this.repairs != null) {
            this.repairs.forEach(i -> i.setDevice(null));
        }
        if (repairs != null) {
            repairs.forEach(i -> i.setDevice(this));
        }
        this.repairs = repairs;
    }

    public Device repairs(Set<Repair> repairs) {
        this.setRepairs(repairs);
        return this;
    }

    public Device addRepair(Repair repair) {
        this.repairs.add(repair);
        repair.setDevice(this);
        return this;
    }

    public Device removeRepair(Repair repair) {
        this.repairs.remove(repair);
        repair.setDevice(null);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Device customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return getId() != null && getId().equals(((Device) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", brand='" + getBrand() + "'" +
            ", model='" + getModel() + "'" +
            ", serialNumber='" + getSerialNumber() + "'" +
            ", warranty='" + getWarranty() + "'" +
            ", password='" + getPassword() + "'" +
            ", hasPattern='" + getHasPattern() + "'" +
            ", simPinCode='" + getSimPinCode() + "'" +
            ", notes='" + getNotes() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
