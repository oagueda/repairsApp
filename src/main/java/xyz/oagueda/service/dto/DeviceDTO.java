package xyz.oagueda.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import xyz.oagueda.domain.enumeration.Type;

/**
 * A DTO for the {@link xyz.oagueda.domain.Device} entity.
 */
@Schema(description = "Device entity.\n@author oagueda.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceDTO implements Serializable {

    private Long id;

    @NotNull
    private Type type;

    private String brand;

    private String model;

    private String serialNumber;

    @NotNull
    private Boolean warranty;

    private String password;

    @NotNull
    private Boolean hasPattern;

    private String simPinCode;

    @Lob
    private String notes;

    @NotNull
    private Boolean deleted;

    private PatternDTO pattern;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Boolean getWarranty() {
        return warranty;
    }

    public void setWarranty(Boolean warranty) {
        this.warranty = warranty;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasPattern() {
        return hasPattern;
    }

    public void setHasPattern(Boolean hasPattern) {
        this.hasPattern = hasPattern;
    }

    public String getSimPinCode() {
        return simPinCode;
    }

    public void setSimPinCode(String simPinCode) {
        this.simPinCode = simPinCode;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PatternDTO getPattern() {
        return pattern;
    }

    public void setPattern(PatternDTO pattern) {
        this.pattern = pattern;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceDTO)) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, deviceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceDTO{" +
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
            ", pattern=" + getPattern() +
            ", customer=" + getCustomer() +
            "}";
    }
}
