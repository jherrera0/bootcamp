package reactive_backend.bootcamp.domain.model;

import java.util.List;

public class Bootcamp {
    private Integer id;
    private String name;
    private String description;
    private List<Ability> abilities;

    public Bootcamp() {
    }

    public Bootcamp(Integer id, String name, String description, List<Ability> abilities) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.abilities = abilities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }


}
