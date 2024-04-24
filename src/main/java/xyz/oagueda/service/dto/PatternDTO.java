package xyz.oagueda.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link xyz.oagueda.domain.Pattern} entity.
 */
@Schema(description = "The hasPattern entity is used to\nrepresent classic Android pattern lock.\n@author oagueda.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PatternDTO implements Serializable {

    private Long id;

    private String code;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatternDTO)) {
            return false;
        }

        PatternDTO patternDTO = (PatternDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patternDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatternDTO{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            "}";
    }
}
