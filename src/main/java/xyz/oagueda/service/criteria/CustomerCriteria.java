package xyz.oagueda.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link xyz.oagueda.domain.Customer} entity. This class is used
 * in {@link xyz.oagueda.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter nif;

    private StringFilter address;

    private StringFilter city;

    private StringFilter zipCode;

    private StringFilter phone1;

    private StringFilter phone2;

    private StringFilter email;

    private BooleanFilter deleted;

    private LongFilter deviceId;

    private Boolean distinct;

    private String filter;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.nif = other.optionalNif().map(StringFilter::copy).orElse(null);
        this.address = other.optionalAddress().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.zipCode = other.optionalZipCode().map(StringFilter::copy).orElse(null);
        this.phone1 = other.optionalPhone1().map(StringFilter::copy).orElse(null);
        this.phone2 = other.optionalPhone2().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.deleted = other.optionalDeleted().map(BooleanFilter::copy).orElse(null);
        this.deviceId = other.optionalDeviceId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
        this.filter = other.filter;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getNif() {
        return nif;
    }

    public Optional<StringFilter> optionalNif() {
        return Optional.ofNullable(nif);
    }

    public StringFilter nif() {
        if (nif == null) {
            setNif(new StringFilter());
        }
        return nif;
    }

    public void setNif(StringFilter nif) {
        this.nif = nif;
    }

    public StringFilter getAddress() {
        return address;
    }

    public Optional<StringFilter> optionalAddress() {
        return Optional.ofNullable(address);
    }

    public StringFilter address() {
        if (address == null) {
            setAddress(new StringFilter());
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getZipCode() {
        return zipCode;
    }

    public Optional<StringFilter> optionalZipCode() {
        return Optional.ofNullable(zipCode);
    }

    public StringFilter zipCode() {
        if (zipCode == null) {
            setZipCode(new StringFilter());
        }
        return zipCode;
    }

    public void setZipCode(StringFilter zipCode) {
        this.zipCode = zipCode;
    }

    public StringFilter getPhone1() {
        return phone1;
    }

    public Optional<StringFilter> optionalPhone1() {
        return Optional.ofNullable(phone1);
    }

    public StringFilter phone1() {
        if (phone1 == null) {
            setPhone1(new StringFilter());
        }
        return phone1;
    }

    public void setPhone1(StringFilter phone1) {
        this.phone1 = phone1;
    }

    public StringFilter getPhone2() {
        return phone2;
    }

    public Optional<StringFilter> optionalPhone2() {
        return Optional.ofNullable(phone2);
    }

    public StringFilter phone2() {
        if (phone2 == null) {
            setPhone2(new StringFilter());
        }
        return phone2;
    }

    public void setPhone2(StringFilter phone2) {
        this.phone2 = phone2;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
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

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(nif, that.nif) &&
            Objects.equals(address, that.address) &&
            Objects.equals(city, that.city) &&
            Objects.equals(zipCode, that.zipCode) &&
            Objects.equals(phone1, that.phone1) &&
            Objects.equals(phone2, that.phone2) &&
            Objects.equals(email, that.email) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(deviceId, that.deviceId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nif, address, city, zipCode, phone1, phone2, email, deleted, deviceId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalNif().map(f -> "nif=" + f + ", ").orElse("") +
            optionalAddress().map(f -> "address=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalZipCode().map(f -> "zipCode=" + f + ", ").orElse("") +
            optionalPhone1().map(f -> "phone1=" + f + ", ").orElse("") +
            optionalPhone2().map(f -> "phone2=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalDeleted().map(f -> "deleted=" + f + ", ").orElse("") +
            optionalDeviceId().map(f -> "deviceId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
