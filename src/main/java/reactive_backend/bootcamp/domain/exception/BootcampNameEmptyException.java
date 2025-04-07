package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class BootcampNameEmptyException extends RuntimeException {
    public BootcampNameEmptyException() {
        super(ConstExceptions.BOOTCAMP_NAME_EMPTY_EXCEPTION);
    }
}
