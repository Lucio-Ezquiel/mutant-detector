package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final DnaRecordRepository repository;

    /**
     * Obtiene las estadísticas de verificaciones de ADN.
     * Calcula la cantidad de mutantes, humanos y el ratio entre ellos.
     *
     * @return StatsResponse con las estadísticas calculadas
     */
    public StatsResponse getStats() {
        long countMutant = repository.countByIsMutant(true);
        long countHuman = repository.countByIsMutant(false);

        double ratio = 0.0;
        if (countHuman > 0) {
            ratio = (double) countMutant / countHuman;
        } else if (countMutant > 0) {
            ratio = 1.0;
        }

        return new StatsResponse(countMutant, countHuman, ratio);
    }
}
