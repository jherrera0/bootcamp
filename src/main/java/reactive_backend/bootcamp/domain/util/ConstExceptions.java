package reactive_backend.bootcamp.domain.util;

public class ConstExceptions {
    public static final String BOOTCAMP_NAME_EMPTY_EXCEPTION = "Bootcamp name is empty";
    public static final String BOOTCAMP_DESCRIPTION_EMPTY_EXCEPTION = "Bootcamp description is empty";
    public static final String BOOTCAMP_INVALID_ABILITIES_SIZE_EXCEPTION =
            "Bootcamp invalid abilities size must be between 1 and 4";
    public static final String BOOTCAMP_ABILITIES_DUPLICATED_EXCEPTION = "Bootcamp abilities are duplicated";
    public static final String ABILITIES_NOT_FOUND = "Abilities not found";
    public static final String LIST_BOOTCAMP_ORDER_DIRECTION_INVALID =
            "List bootcamp order direction invalid must be asc or desc";
    public static final String LIST_BOOTCAMP_PAGE_SIZE_INVALID =
            "List bootcamp page size invalid must be greater than 0";
    public static final String LIST_BOOTCAMP_PAGE_CURRENT_INVALID =
            "List bootcamp page current invalid must be greater than 0";
    public static final String LIST_BOOTCAMP_PAGE_INVALID =
            "List bootcamp page invalid must be greater than 0 and less than total pages";
    public static final String LIST_BOOTCAMP_SORT_FIELD_INVALID =
            "List bootcamp sort field invalid must be name or abilitiesSize";

    private ConstExceptions() {
    }
}
