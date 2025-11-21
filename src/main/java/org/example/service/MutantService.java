package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.entity.DnaRecord;
import org.example.exception.DnaHashCalculationException;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRecordRepository repository;

    /**
     * Analiza una secuencia de ADN y determina si es mutante.
     * Guarda el resultado en la base de datos evitando duplicados mediante hash.
     *
     * @param dna Array de strings representando la secuencia de ADN
     * @return true si es mutante, false en caso contrario
     */
    @Transactional
    public boolean analyzeDna(String[] dna) {
        // Calcular hash del ADN para deduplicación
        String dnaHash = calculateDnaHash(dna);

        // Verificar si ya existe en la base de datos
        Optional<DnaRecord> existingRecord = repository.findByDnaHash(dnaHash);
        if (existingRecord.isPresent()) {
            // Retornar resultado cacheado
            return existingRecord.get().isMutant();
        }

        // Analizar el ADN
        boolean isMutant = mutantDetector.isMutant(dna);

        // Guardar el resultado en la base de datos
        DnaRecord record = new DnaRecord(dnaHash, isMutant);
        repository.save(record);

        return isMutant;
    }

    /**
     * Calcula el hash SHA-256 de una secuencia de ADN.
     * Esto permite identificar de forma única cada secuencia y evitar duplicados.
     *
     * @param dna Array de strings representando la secuencia de ADN
     * @return Hash SHA-256 en formato hexadecimal
     * @throws DnaHashCalculationException si ocurre un error al calcular el hash
     */
    private String calculateDnaHash(String[] dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String dnaString = String.join("", dna);
            byte[] hashBytes = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));

            // Convertir bytes a representación hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error al calcular el hash del ADN", e);
        }
    }
}
