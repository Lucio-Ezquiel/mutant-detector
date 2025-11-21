package org.example.repository;

import org.example.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long> {

    /**
     * Busca un registro de ADN por su hash SHA-256
     * @param dnaHash Hash SHA-256 de la secuencia de ADN
     * @return Optional con el registro si existe
     */
    Optional<DnaRecord> findByDnaHash(String dnaHash);

    /**
     * Cuenta la cantidad de registros según si son mutantes o no
     * @param isMutant true para contar mutantes, false para humanos
     * @return Cantidad de registros
     */
    long countByIsMutant(boolean isMutant);
}
