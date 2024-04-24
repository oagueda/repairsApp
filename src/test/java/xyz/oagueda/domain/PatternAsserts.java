package xyz.oagueda.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PatternAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPatternAllPropertiesEquals(Pattern expected, Pattern actual) {
        assertPatternAutoGeneratedPropertiesEquals(expected, actual);
        assertPatternAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPatternAllUpdatablePropertiesEquals(Pattern expected, Pattern actual) {
        assertPatternUpdatableFieldsEquals(expected, actual);
        assertPatternUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPatternAutoGeneratedPropertiesEquals(Pattern expected, Pattern actual) {
        assertThat(expected)
            .as("Verify Pattern auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPatternUpdatableFieldsEquals(Pattern expected, Pattern actual) {
        assertThat(expected)
            .as("Verify Pattern relevant properties")
            .satisfies(e -> assertThat(e.getCode()).as("check code").isEqualTo(actual.getCode()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPatternUpdatableRelationshipsEquals(Pattern expected, Pattern actual) {}
}
