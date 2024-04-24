package xyz.oagueda.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;
import xyz.oagueda.domain.enumeration.Status;

/**
 * Criteria class for the {@link xyz.oagueda.domain.Repair} entity. This class is used
 * in {@link xyz.oagueda.web.rest.RepairResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /repairs?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RepairCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Status
     */
    public static class StatusFilter extends Filter<Status> {

        public StatusFilter() {}

        public StatusFilter(StatusFilter filter) {
            super(filter);
        }

        @Override
        public StatusFilter copy() {
            return new StatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StatusFilter status;

    private InstantFilter closedDate;

    private StringFilter budget;

    private BooleanFilter importantData;

    private StringFilter total;

    private LongFilter deviceId;

    private Boolean distinct;

    public RepairCriteria() {}

    public RepairCriteria(RepairCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StatusFilter::copy).orElse(null);
        this.closedDate = other.optionalClosedDate().map(InstantFilter::copy).orElse(null);
        this.budget = other.optionalBudget().map(StringFilter::copy).orElse(null);
        this.importantData = other.optionalImportantData().map(BooleanFilter::copy).orElse(null);
        this.total = other.optionalTotal().map(StringFilter::copy).orElse(null);
        this.deviceId = other.optionalDeviceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public RepairCriteria copy() {
        return new RepairCriteria(this);
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

    public StatusFilter getStatus() {
        return status;
    }

    public Optional<StatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StatusFilter status() {
        if (status == null) {
            setStatus(new StatusFilter());
        }
        return status;
    }

    public void setStatus(StatusFilter status) {
        this.status = status;
    }

    public InstantFilter getClosedDate() {
        return closedDate;
    }

    public Optional<InstantFilter> optionalClosedDate() {
        return Optional.ofNullable(closedDate);
    }

    public InstantFilter closedDate() {
        if (closedDate == null) {
            setClosedDate(new InstantFilter());
        }
        return closedDate;
    }

    public void setClosedDate(InstantFilter closedDate) {
        this.closedDate = closedDate;
    }

    public StringFilter getBudget() {
        return budget;
    }

    public Optional<StringFilter> optionalBudget() {
        return Optional.ofNullable(budget);
    }

    public StringFilter budget() {
        if (budget == null) {
            setBudget(new StringFilter());
        }
        return budget;
    }

    public void setBudget(StringFilter budget) {
        this.budget = budget;
    }

    public BooleanFilter getImportantData() {
        return importantData;
    }

    public Optional<BooleanFilter> optionalImportantData() {
        return Optional.ofNullable(importantData);
    }

    public BooleanFilter importantData() {
        if (importantData == null) {
            setImportantData(new BooleanFilter());
        }
        return importantData;
    }

    public void setImportantData(BooleanFilter importantData) {
        this.importantData = importantData;
    }

    public StringFilter getTotal() {
        return total;
    }

    public Optional<StringFilter> optionalTotal() {
        return Optional.ofNullable(total);
    }

    public StringFilter total() {
        if (total == null) {
            setTotal(new StringFilter());
        }
        return total;
    }

    public void setTotal(StringFilter total) {
        this.total = total;
    }

    public LongFilter getDeviceId() {
        return deviceId;
    }

    public Optional<LongFilter> optionalDeviceId() {
        return Optional.ofNullable(deviceId);
    }

    public LongFilter deviceId() {
        if (deviceId == null) {
            setDeviceId(new LongFilter());
        }
        return deviceId;
    }

    public void setDeviceId(LongFilter deviceId) {
        this.deviceId = deviceId;
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
        final RepairCriteria that = (RepairCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(closedDate, that.closedDate) &&
            Objects.equals(budget, that.budget) &&
            Objects.equals(importantData, that.importantData) &&
            Objects.equals(total, that.total) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, closedDate, budget, importantData, total, deviceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RepairCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalClosedDate().map(f -> "closedDate=" + f + ", ").orElse("") +
            optionalBudget().map(f -> "budget=" + f + ", ").orElse("") +
            optionalImportantData().map(f -> "importantData=" + f + ", ").orElse("") +
            optionalTotal().map(f -> "total=" + f + ", ").orElse("") +
            optionalDeviceId().map(f -> "deviceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
