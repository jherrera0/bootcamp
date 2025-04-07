package reactive_backend.bootcamp.domain.exception;

import reactive_backend.bootcamp.domain.util.ConstExceptions;

import java.util.List;

public class BootcampAbilitiesNotFoundException extends RuntimeException {
    public BootcampAbilitiesNotFoundException(List<Integer> notFoundTechnologies) {
        super(ConstExceptions.ABILITIES_NOT_FOUND + notFoundTechnologies);
    }
}
