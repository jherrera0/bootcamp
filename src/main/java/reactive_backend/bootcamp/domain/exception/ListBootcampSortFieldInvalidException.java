package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class ListBootcampSortFieldInvalidException extends RuntimeException {
    public ListBootcampSortFieldInvalidException() {
        super(ConstExceptions.LIST_BOOTCAMP_SORT_FIELD_INVALID);
    }
}
