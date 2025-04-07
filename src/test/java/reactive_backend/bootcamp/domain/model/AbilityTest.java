package reactive_backend.bootcamp.domain.model;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbilityTest {

    @Test
    void abilityConstructorSetsAllFields() {
        List<Technology> technologies = Collections.singletonList(new Technology());
        Ability ability = new Ability(1, "Coding", "Ability to write code", technologies);

        assertEquals(1, ability.getId());
        assertEquals("Coding", ability.getName());
        assertEquals("Ability to write code", ability.getDescription());
        assertEquals(technologies, ability.getTechnologies());
    }

    @Test
    void abilityDefaultConstructorSetsFieldsToNull() {
        Ability ability = new Ability();

        assertNull(ability.getId());
        assertNull(ability.getName());
        assertNull(ability.getDescription());
        assertNull(ability.getTechnologies());
    }

    @Test
    void setIdUpdatesId() {
        Ability ability = new Ability();
        ability.setId(2);

        assertEquals(2, ability.getId());
    }

    @Test
    void setNameUpdatesName() {
        Ability ability = new Ability();
        ability.setName("Testing");

        assertEquals("Testing", ability.getName());
    }

    @Test
    void setDescriptionUpdatesDescription() {
        Ability ability = new Ability();
        ability.setDescription("Ability to test code");

        assertEquals("Ability to test code", ability.getDescription());
    }

    @Test
    void setTechnologiesUpdatesTechnologies() {
        List<Technology> technologies = Collections.singletonList(new Technology());
        Ability ability = new Ability();
        ability.setTechnologies(technologies);

        assertEquals(technologies, ability.getTechnologies());
    }
}