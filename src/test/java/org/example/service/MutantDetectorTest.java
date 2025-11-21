package org.example.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MutantDetectorTest {

    @Autowired
    private MutantDetector mutantDetector;

    // ==================== TESTS DE MUTANTES ====================

    @Test
    void testMutantWithHorizontalAndVertical() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante con secuencias horizontal y vertical");
    }

    @Test
    void testMutantWithHorizontalSequence() {
        String[] dna = {
                "AAAATG",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante con secuencia horizontal");
    }

    @Test
    void testMutantWithDiagonalSequence() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante con secuencia diagonal");
    }

    @Test
    void testMutantWithDiagonalDown() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante con diagonal descendente");
    }

    @Test
    void testMutantWithDiagonalUp() {
        String[] dna = {
                "ATGCGA",
                "CTGTGC",
                "TTATGT",
                "AGATGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante con diagonal ascendente");
    }

    @Test
    void testMutantWithMultipleSequences() {
        String[] dna = {
                "AAAATG",
                "AAAAGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante con múltiples secuencias");
    }

    // ==================== TESTS DE HUMANOS ====================

    @Test
    void testHumanWithNoSequences() {
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "No debería detectar mutante sin secuencias");
    }

    @Test
    void testHumanWithOnlyOneSequence() {
        String[] dna = {
                "AAAA",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "No debería detectar mutante con solo una secuencia");
    }

    @Test
    void testHumanWithOneVerticalSequence() {
        String[] dna = {
                "ATGCGA",
                "CTGTGC",
                "TTATGT",
                "ATATGG",
                "CCCCTA",
                "TCACTG"
        };
        assertFalse(mutantDetector.isMutant(dna), "No debería detectar mutante con solo una secuencia vertical");
    }

    // ==================== TESTS DE VALIDACIÓN ====================

    @Test
    void testNullDnaArray() {
        assertFalse(mutantDetector.isMutant(null), "Debería retornar false para DNA nulo");
    }

    @Test
    void testEmptyDnaArray() {
        String[] dna = {};
        assertFalse(mutantDetector.isMutant(dna), "Debería retornar false para DNA vacío");
    }

    @Test
    void testInvalidDnaNonSquare() {
        String[] dna = {
                "ATGC",
                "CAG",
                "TTAT"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debería retornar false para matriz no cuadrada");
    }

    @Test
    void testInvalidDnaCharacters() {
        String[] dna = {
                "ATGX",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debería retornar false para caracteres inválidos");
    }

    @Test
    void testInvalidDnaWithNumbers() {
        String[] dna = {
                "ATG1",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debería retornar false para DNA con números");
    }

    @Test
    void testInvalidDnaWithNullRow() {
        String[] dna = {
                "ATGC",
                null,
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Debería retornar false cuando una fila es null");
    }

    // ==================== TESTS ADICIONALES ====================

    @Test
    void testSmallMatrixMutant() {
        String[] dna = {
                "AAAA",
                "AAAA",
                "TTTT",
                "CCCC"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante en matriz 4x4");
    }

    @Test
    void testLargeMatrixMutant() {
        String[] dna = {
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGAGAA",
                "CCCCTACCCC",
                "TCACTGTCAC",
                "ATGCGAATGC",
                "CAGTGCCAGT",
                "TTATGTTTAT",
                "AGAAGGAGAA"
        };
        assertTrue(mutantDetector.isMutant(dna), "Debería detectar mutante en matriz 10x10");
    }
}
