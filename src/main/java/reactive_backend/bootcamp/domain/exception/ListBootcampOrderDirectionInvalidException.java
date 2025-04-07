package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class ListBootcampOrderDirectionInvalidException extends RuntimeException {
    public ListBootcampOrderDirectionInvalidException() {
        super(ConstExceptions.LIST_BOOTCAMP_ORDER_DIRECTION_INVALID);
    }
}
