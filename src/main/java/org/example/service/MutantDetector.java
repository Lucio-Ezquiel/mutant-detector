package org.example.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    /**
     * Determina si una secuencia de ADN corresponde a un mutante.
     * Un humano es mutante si se encuentran más de una secuencia de cuatro letras
     * iguales, de forma oblicua, horizontal o vertical.
     *
     * @param dna Array de strings que representan cada fila de una tabla NxN con la secuencia del ADN
     * @return true si es mutante, false en caso contrario
     */
    public boolean isMutant(String[] dna) {
        if (!isValidDna(dna)) {
            return false;
        }

        final int n = dna.length;
        int sequenceCount = 0;

        // Conversión a char[][] para acceso O(1)
        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        // Single Pass: recorrer una sola vez la matriz
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Verificar horizontal (boundary checking)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early termination
                    }
                }

                // Verificar vertical (boundary checking)
                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early termination
                    }
                }

                // Verificar diagonal descendente (boundary checking)
                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDown(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early termination
                    }
                }

                // Verificar diagonal ascendente (boundary checking)
                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalUp(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;  // Early termination
                    }
                }
            }
        }

        return false;
    }

    /**
     * Valida que el array de ADN sea correcto:
     * - No nulo ni vacío
     * - Matriz NxN
     * - Solo caracteres A, T, C, G
     */
    private boolean isValidDna(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;

        for (String row : dna) {
            if (row == null || row.length() != n) {
                return false;
            }

            for (char base : row.toCharArray()) {
                if (!VALID_BASES.contains(base)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Verifica secuencia horizontal usando comparación directa
     */
    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    /**
     * Verifica secuencia vertical usando comparación directa
     */
    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    /**
     * Verifica secuencia diagonal descendente (\) usando comparación directa
     */
    private boolean checkDiagonalDown(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    /**
     * Verifica secuencia diagonal ascendente (/) usando comparación directa
     */
    private boolean checkDiagonalUp(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }
}
