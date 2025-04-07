package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

public class BootcampAbilitiesDuplicatedException extends RuntimeException {
    public BootcampAbilitiesDuplicatedException() {
        super(ConstExceptions.BOOTCAMP_ABILITIES_DUPLICATED_EXCEPTION);
    }
}
