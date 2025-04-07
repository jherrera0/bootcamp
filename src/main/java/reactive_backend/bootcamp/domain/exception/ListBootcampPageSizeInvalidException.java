package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class ListBootcampPageSizeInvalidException extends RuntimeException {
    public ListBootcampPageSizeInvalidException() {
        super(ConstExceptions.LIST_BOOTCAMP_PAGE_SIZE_INVALID);
    }
}
