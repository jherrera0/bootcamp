package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class ListBootcampPageCurrentInvalidException extends RuntimeException {
    public ListBootcampPageCurrentInvalidException() {
        super(ConstExceptions.LIST_BOOTCAMP_PAGE_CURRENT_INVALID);
    }
}
