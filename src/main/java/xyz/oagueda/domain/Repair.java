package xyz.oagueda.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import xyz.oagueda.domain.enumeration.Status;

/**
 * Repair entity.
 * @author oagueda.
 */
@Entity
@Table(name = "repair")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Repair extends AbstractAuditingEntity<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Lob
    @Column(name = "observations")
    private String observations;

    @Lob
    @Column(name = "internal_observations")
    private String internalObservations;

    /**
     * The status property is an enumeration
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "closed_date")
    private Instant closedDate;

    @Column(name = "budget")
    private String budget;

    @Lob
    @Column(name = "work_done")
    private String workDone;

    @Lob
    @Column(name = "used_material")
    private String usedMaterial;

    @Lob
    @Column(name = "customer_material")
    private String customerMaterial;

    @Column(name = "important_data")
    private Boolean importantData;

    @Column(name = "total")
    private String total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pattern", "repairs", "customer" }, allowSetters = true)
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Repair id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Repair description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservations() {
        return this.observations;
    }

    public Repair observations(String observations) {
        this.setObservations(observations);
        return this;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getInternalObservations() {
        return this.internalObservations;
    }

    public Repair internalObservations(String internalObservations) {
        this.setInternalObservations(internalObservations);
        return this;
    }

    public void setInternalObservations(String internalObservations) {
        this.internalObservations = internalObservations;
    }

    public Status getStatus() {
        return this.status;
    }

    public Repair status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getClosedDate() {
        return this.closedDate;
    }

    public Repair closedDate(Instant closedDate) {
        this.setClosedDate(closedDate);
        return this;
    }

    public void setClosedDate(Instant closedDate) {
        this.closedDate = closedDate;
    }

    public String getBudget() {
        return this.budget;
    }

    public Repair budget(String budget) {
        this.setBudget(budget);
        return this;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getWorkDone() {
        return this.workDone;
    }

    public Repair workDone(String workDone) {
        this.setWorkDone(workDone);
        return this;
    }

    public void setWorkDone(String workDone) {
        this.workDone = workDone;
    }

    public String getUsedMaterial() {
        return this.usedMaterial;
    }

    public Repair usedMaterial(String usedMaterial) {
        this.setUsedMaterial(usedMaterial);
        return this;
    }

    public void setUsedMaterial(String usedMaterial) {
        this.usedMaterial = usedMaterial;
    }

    public String getCustomerMaterial() {
        return this.customerMaterial;
    }

    public Repair customerMaterial(String customerMaterial) {
        this.setCustomerMaterial(customerMaterial);
        return this;
    }

    public void setCustomerMaterial(String customerMaterial) {
        this.customerMaterial = customerMaterial;
    }

    public Boolean getImportantData() {
        return this.importantData;
    }

    public Repair importantData(Boolean importantData) {
        this.setImportantData(importantData);
        return this;
    }

    public void setImportantData(Boolean importantData) {
        this.importantData = importantData;
    }

    public String getTotal() {
        return this.total;
    }

    public Repair total(String total) {
        this.setTotal(total);
        return this;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Device getDevice() {
        return this.device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Repair device(Device device) {
        this.setDevice(device);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Repair)) {
            return false;
        }
        return getId() != null && getId().equals(((Repair) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Repair{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", observations='" + getObservations() + "'" +
            ", internalObservations='" + getInternalObservations() + "'" +
            ", status='" + getStatus() + "'" +
            ", closedDate='" + getClosedDate() + "'" +
            ", budget='" + getBudget() + "'" +
            ", workDone='" + getWorkDone() + "'" +
            ", usedMaterial='" + getUsedMaterial() + "'" +
            ", customerMaterial='" + getCustomerMaterial() + "'" +
            ", importantData='" + getImportantData() + "'" +
            ", total='" + getTotal() + "'" +
            "}";
    }
}
