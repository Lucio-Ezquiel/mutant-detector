package org.example.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    private static final Pattern DNA_PATTERN = Pattern.compile("^[ATCG]+$");

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("La secuencia de ADN no puede ser nula o vacía")
                    .addConstraintViolation();
            return false;
        }

        int n = dna.length;

        for (int i = 0; i < n; i++) {
            String row = dna[i];

            // Validar que la fila no sea nula
            if (row == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                String.format("La fila %d no puede ser nula", i))
                        .addConstraintViolation();
                return false;
            }

            // Validar que la matriz sea NxN
            if (row.length() != n) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                String.format("La matriz debe ser NxN. La fila %d tiene longitud %d pero se esperaba %d",
                                        i, row.length(), n))
                        .addConstraintViolation();
                return false;
            }

            // Validar que solo contenga caracteres A, T, C, G
            if (!DNA_PATTERN.matcher(row).matches()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                String.format("La fila %d contiene caracteres inválidos. Solo se permiten A, T, C, G", i))
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}
