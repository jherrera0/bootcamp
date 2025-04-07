package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class BootcampInvalidAbilitiesSizeException extends RuntimeException {
    public BootcampInvalidAbilitiesSizeException() {
        super(ConstExceptions.BOOTCAMP_INVALID_ABILITIES_SIZE_EXCEPTION);
    }

}
