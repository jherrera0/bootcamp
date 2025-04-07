package reactive_backend.bootcamp.domain.util;

public class ConstExceptions {
    public static final String BOOTCAMP_NAME_EMPTY_EXCEPTION = "Bootcamp name is empty";
    public static final String BOOTCAMP_DESCRIPTION_EMPTY_EXCEPTION = "Bootcamp description is empty";
    public static final String BOOTCAMP_INVALID_ABILITIES_SIZE_EXCEPTION =
            "Bootcamp invalid abilities size must be between 1 and 4";
    public static final String BOOTCAMP_ABILITIES_DUPLICATED_EXCEPTION = "Bootcamp abilities are duplicated";
    public static final String ABILITIES_NOT_FOUND = "Abilities not found";

    private ConstExceptions() {
    }
}
