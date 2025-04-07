package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class ListBootcampPageInvalidException extends RuntimeException {
    public ListBootcampPageInvalidException() {
        super(ConstExceptions.LIST_BOOTCAMP_PAGE_INVALID);
    }
}
