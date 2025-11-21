package org.example.service;

import org.example.dto.StatsResponse;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private DnaRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void testGetStatsWithNoData() {
        StatsResponse stats = statsService.getStats();

        assertEquals(0, stats.getCount_mutant_dna(), "Debería tener 0 mutantes");
        assertEquals(0, stats.getCount_human_dna(), "Debería tener 0 humanos");
        assertEquals(0.0, stats.getRatio(), "Ratio debería ser 0.0");
    }

    @Test
    void testGetStatsWithMutantsOnly() {
        repository.save(new DnaRecord("hash1", true));
        repository.save(new DnaRecord("hash2", true));
        repository.save(new DnaRecord("hash3", true));

        StatsResponse stats = statsService.getStats();

        assertEquals(3, stats.getCount_mutant_dna(), "Debería tener 3 mutantes");
        assertEquals(0, stats.getCount_human_dna(), "Debería tener 0 humanos");
        assertEquals(1.0, stats.getRatio(), "Ratio debería ser 1.0 cuando solo hay mutantes");
    }

    @Test
    void testGetStatsWithHumansOnly() {
        repository.save(new DnaRecord("hash1", false));
        repository.save(new DnaRecord("hash2", false));

        StatsResponse stats = statsService.getStats();

        assertEquals(0, stats.getCount_mutant_dna(), "Debería tener 0 mutantes");
        assertEquals(2, stats.getCount_human_dna(), "Debería tener 2 humanos");
        assertEquals(0.0, stats.getRatio(), "Ratio debería ser 0.0 cuando solo hay humanos");
    }

    @Test
    void testGetStatsWithMixedData() {
        repository.save(new DnaRecord("hash1", true));
        repository.save(new DnaRecord("hash2", true));
        repository.save(new DnaRecord("hash3", true));
        repository.save(new DnaRecord("hash4", true));
        repository.save(new DnaRecord("hash5", false));
        repository.save(new DnaRecord("hash6", false));
        repository.save(new DnaRecord("hash7", false));
        repository.save(new DnaRecord("hash8", false));
        repository.save(new DnaRecord("hash9", false));
        repository.save(new DnaRecord("hash10", false));

        StatsResponse stats = statsService.getStats();

        assertEquals(4, stats.getCount_mutant_dna(), "Debería tener 4 mutantes");
        assertEquals(6, stats.getCount_human_dna(), "Debería tener 6 humanos");
        assertEquals(0.666, stats.getRatio(), 0.01, "Ratio debería ser aproximadamente 0.666");
    }

    @Test
    void testGetStatsRatioCalculation() {
        // 40 mutantes, 100 humanos -> ratio = 0.4
        for (int i = 0; i < 40; i++) {
            repository.save(new DnaRecord("mutant_" + i, true));
        }
        for (int i = 0; i < 100; i++) {
            repository.save(new DnaRecord("human_" + i, false));
        }

        StatsResponse stats = statsService.getStats();

        assertEquals(40, stats.getCount_mutant_dna());
        assertEquals(100, stats.getCount_human_dna());
        assertEquals(0.4, stats.getRatio(), 0.001, "Ratio debería ser 0.4");
    }

    @Test
    void testGetStatsAfterMultipleOperations() {
        repository.save(new DnaRecord("hash1", true));
        repository.save(new DnaRecord("hash2", false));

        StatsResponse stats1 = statsService.getStats();
        assertEquals(1, stats1.getCount_mutant_dna());
        assertEquals(1, stats1.getCount_human_dna());

        repository.save(new DnaRecord("hash3", true));
        repository.save(new DnaRecord("hash4", true));

        StatsResponse stats2 = statsService.getStats();
        assertEquals(3, stats2.getCount_mutant_dna());
        assertEquals(1, stats2.getCount_human_dna());
    }
}
