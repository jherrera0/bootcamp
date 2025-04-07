package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class BootcampDescriptionEmptyException extends RuntimeException {
    public BootcampDescriptionEmptyException() {
        super(ConstExceptions.BOOTCAMP_DESCRIPTION_EMPTY_EXCEPTION);
    }
}
