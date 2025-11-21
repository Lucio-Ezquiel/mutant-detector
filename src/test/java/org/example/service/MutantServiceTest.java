package org.example.service;

import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MutantServiceTest {

    @Autowired
    private MutantService mutantService;

    @Autowired
    private DnaRecordRepository repository;

    @Test
    void testAnalyzeMutantDna() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        boolean result = mutantService.analyzeDna(dna);
        assertTrue(result, "Debería retornar true para DNA mutante");

        // Verificar que se guardó en la base de datos
        long count = repository.count();
        assertTrue(count > 0, "Debería haber guardado el registro en la BD");
    }

    @Test
    void testAnalyzeHumanDna() {
        String[] dna = {
                "ATGCGA",
                "CTGTGC",
                "TTATGT",
                "ATATGG",
                "CCCCTA",
                "TCACTG"
        };

        boolean result = mutantService.analyzeDna(dna);
        assertFalse(result, "Debería retornar false para DNA humano");
    }

    @Test
    void testDeduplicationWithSameDna() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        // Primera verificación
        mutantService.analyzeDna(dna);
        long countAfterFirst = repository.count();

        // Segunda verificación con el mismo DNA
        mutantService.analyzeDna(dna);
        long countAfterSecond = repository.count();

        assertEquals(countAfterFirst, countAfterSecond,
                "No debería crear duplicados en la BD para el mismo DNA");
    }

    @Test
    void testHashCalculationIsConsistent() {
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        // Analizar dos veces
        mutantService.analyzeDna(dna);
        mutantService.analyzeDna(dna);

        // Debería haber solo un registro
        long count = repository.count();
        assertEquals(1, count, "Debería haber solo un registro para el mismo DNA");
    }

    @Test
    void testDifferentDnasCreateDifferentRecords() {
        String[] dna1 = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };

        String[] dna2 = {
                "ATGCGA",
                "CTGTGC",
                "TTATGT",
                "ATATGG",
                "CCCCTA",
                "TCACTG"
        };

        mutantService.analyzeDna(dna1);
        mutantService.analyzeDna(dna2);

        long count = repository.count();
        assertEquals(2, count, "Debería crear registros diferentes para DNAs diferentes");
    }
}
