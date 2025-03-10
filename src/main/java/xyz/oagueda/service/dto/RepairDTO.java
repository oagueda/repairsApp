package xyz.oagueda.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import xyz.oagueda.domain.enumeration.Status;

/**
 * A DTO for the {@link xyz.oagueda.domain.Repair} entity.
 */
@Schema(description = "Repair entity.\n@author oagueda.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RepairDTO implements Serializable {

    private Long id;

    @Lob
    private String description;

    @Lob
    private String observations;

    @Lob
    private String internalObservations;

    @NotNull
    private Status status;

    private Instant closedDate;

    private String budget;

    @Lob
    private String workDone;

    @Lob
    private String usedMaterial;

    @Lob
    private String customerMaterial;

    private Boolean importantData;

    private String total;

    private DeviceDTO device;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getInternalObservations() {
        return internalObservations;
    }

    public void setInternalObservations(String internalObservations) {
        this.internalObservations = internalObservations;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Instant closedDate) {
        this.closedDate = closedDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getWorkDone() {
        return workDone;
    }

    public void setWorkDone(String workDone) {
        this.workDone = workDone;
    }

    public String getUsedMaterial() {
        return usedMaterial;
    }

    public void setUsedMaterial(String usedMaterial) {
        this.usedMaterial = usedMaterial;
    }

    public String getCustomerMaterial() {
        return customerMaterial;
    }

    public void setCustomerMaterial(String customerMaterial) {
        this.customerMaterial = customerMaterial;
    }

    public Boolean getImportantData() {
        return importantData;
    }

    public void setImportantData(Boolean importantData) {
        this.importantData = importantData;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public DeviceDTO getDevice() {
        return device;
    }

    public void setDevice(DeviceDTO device) {
        this.device = device;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RepairDTO)) {
            return false;
        }

        RepairDTO repairDTO = (RepairDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, repairDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RepairDTO{" +
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
            ", device=" + getDevice() +
            "}";
    }
}
