package xyz.oagueda.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import xyz.oagueda.domain.enumeration.Type;

/**
 * Criteria class for the {@link xyz.oagueda.domain.Device} entity. This class is used
 * in {@link xyz.oagueda.web.rest.DeviceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /devices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DeviceCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Type
     */
    public static class TypeFilter extends Filter<Type> {

        public TypeFilter() {}

        public TypeFilter(TypeFilter filter) {
            super(filter);
        }

        @Override
        public TypeFilter copy() {
            return new TypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TypeFilter type;

    private StringFilter brand;

    private StringFilter model;

    private StringFilter serialNumber;

    private BooleanFilter warranty;

    private StringFilter password;

    private BooleanFilter hasPattern;

    private StringFilter simPinCode;

    private BooleanFilter deleted;

    private LongFilter patternId;

    private LongFilter repairId;

    private LongFilter customerId;

    private Boolean distinct;

    public DeviceCriteria() {}

    public DeviceCriteria(DeviceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.type = other.optionalType().map(TypeFilter::copy).orElse(null);
        this.brand = other.optionalBrand().map(StringFilter::copy).orElse(null);
        this.model = other.optionalModel().map(StringFilter::copy).orElse(null);
        this.serialNumber = other.optionalSerialNumber().map(StringFilter::copy).orElse(null);
        this.warranty = other.optionalWarranty().map(BooleanFilter::copy).orElse(null);
        this.password = other.optionalPassword().map(StringFilter::copy).orElse(null);
        this.hasPattern = other.optionalHasPattern().map(BooleanFilter::copy).orElse(null);
        this.simPinCode = other.optionalSimPinCode().map(StringFilter::copy).orElse(null);
        this.deleted = other.optionalDeleted().map(BooleanFilter::copy).orElse(null);
        this.patternId = other.optionalPatternId().map(LongFilter::copy).orElse(null);
        this.repairId = other.optionalRepairId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DeviceCriteria copy() {
        return new DeviceCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TypeFilter getType() {
        return type;
    }

    public Optional<TypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TypeFilter type() {
        if (type == null) {
            setType(new TypeFilter());
        }
        return type;
    }

    public void setType(TypeFilter type) {
        this.type = type;
    }

    public StringFilter getBrand() {
        return brand;
    }

    public Optional<StringFilter> optionalBrand() {
        return Optional.ofNullable(brand);
    }

    public StringFilter brand() {
        if (brand == null) {
            setBrand(new StringFilter());
        }
        return brand;
    }

    public void setBrand(StringFilter brand) {
        this.brand = brand;
    }

    public StringFilter getModel() {
        return model;
    }

    public Optional<StringFilter> optionalModel() {
        return Optional.ofNullable(model);
    }

    public StringFilter model() {
        if (model == null) {
            setModel(new StringFilter());
        }
        return model;
    }

    public void setModel(StringFilter model) {
        this.model = model;
    }

    public StringFilter getSerialNumber() {
        return serialNumber;
    }

    public Optional<StringFilter> optionalSerialNumber() {
        return Optional.ofNullable(serialNumber);
    }

    public StringFilter serialNumber() {
        if (serialNumber == null) {
            setSerialNumber(new StringFilter());
        }
        return serialNumber;
    }

    public void setSerialNumber(StringFilter serialNumber) {
        this.serialNumber = serialNumber;
    }

    public BooleanFilter getWarranty() {
        return warranty;
    }

    public Optional<BooleanFilter> optionalWarranty() {
        return Optional.ofNullable(warranty);
    }

    public BooleanFilter warranty() {
        if (warranty == null) {
            setWarranty(new BooleanFilter());
        }
        return warranty;
    }

    public void setWarranty(BooleanFilter warranty) {
        this.warranty = warranty;
    }

    public StringFilter getPassword() {
        return password;
    }

    public Optional<StringFilter> optionalPassword() {
        return Optional.ofNullable(password);
    }

    public StringFilter password() {
        if (password == null) {
            setPassword(new StringFilter());
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public BooleanFilter getHasPattern() {
        return hasPattern;
    }

    public Optional<BooleanFilter> optionalHasPattern() {
        return Optional.ofNullable(hasPattern);
    }

    public BooleanFilter hasPattern() {
        if (hasPattern == null) {
            setHasPattern(new BooleanFilter());
        }
        return hasPattern;
    }

    public void setHasPattern(BooleanFilter hasPattern) {
        this.hasPattern = hasPattern;
    }

    public StringFilter getSimPinCode() {
        return simPinCode;
    }

    public Optional<StringFilter> optionalSimPinCode() {
        return Optional.ofNullable(simPinCode);
    }

    public StringFilter simPinCode() {
        if (simPinCode == null) {
            setSimPinCode(new StringFilter());
        }
        return simPinCode;
    }

    public void setSimPinCode(StringFilter simPinCode) {
        this.simPinCode = simPinCode;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public Optional<BooleanFilter> optionalDeleted() {
        return Optional.ofNullable(deleted);
    }

    public BooleanFilter deleted() {
        if (deleted == null) {
            setDeleted(new BooleanFilter());
        }
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public LongFilter getPatternId() {
        return patternId;
    }

    public Optional<LongFilter> optionalPatternId() {
        return Optional.ofNullable(patternId);
    }

    public LongFilter patternId() {
        if (patternId == null) {
            setPatternId(new LongFilter());
        }
        return patternId;
    }

    public void setPatternId(LongFilter patternId) {
        this.patternId = patternId;
    }

    public LongFilter getRepairId() {
        return repairId;
    }

    public Optional<LongFilter> optionalRepairId() {
        return Optional.ofNullable(repairId);
    }

    public LongFilter repairId() {
        if (repairId == null) {
            setRepairId(new LongFilter());
        }
        return repairId;
    }

    public void setRepairId(LongFilter repairId) {
        this.repairId = repairId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeviceCriteria that = (DeviceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(brand, that.brand) &&
            Objects.equals(model, that.model) &&
            Objects.equals(serialNumber, that.serialNumber) &&
            Objects.equals(warranty, that.warranty) &&
            Objects.equals(password, that.password) &&
            Objects.equals(hasPattern, that.hasPattern) &&
            Objects.equals(simPinCode, that.simPinCode) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(patternId, that.patternId) &&
            Objects.equals(repairId, that.repairId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            type,
            brand,
            model,
            serialNumber,
            warranty,
            password,
            hasPattern,
            simPinCode,
            deleted,
            patternId,
            repairId,
            customerId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeviceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalBrand().map(f -> "brand=" + f + ", ").orElse("") +
            optionalModel().map(f -> "model=" + f + ", ").orElse("") +
            optionalSerialNumber().map(f -> "serialNumber=" + f + ", ").orElse("") +
            optionalWarranty().map(f -> "warranty=" + f + ", ").orElse("") +
            optionalPassword().map(f -> "password=" + f + ", ").orElse("") +
            optionalHasPattern().map(f -> "hasPattern=" + f + ", ").orElse("") +
            optionalSimPinCode().map(f -> "simPinCode=" + f + ", ").orElse("") +
            optionalDeleted().map(f -> "deleted=" + f + ", ").orElse("") +
            optionalPatternId().map(f -> "patternId=" + f + ", ").orElse("") +
            optionalRepairId().map(f -> "repairId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
