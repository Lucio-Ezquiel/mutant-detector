package org.example.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
@Documented
public @interface ValidDnaSequence {

    String message() default "La secuencia de ADN es inválida. Debe ser una matriz NxN con solo caracteres A, T, C, G";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
