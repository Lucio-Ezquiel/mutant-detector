package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "API para detección de mutantes mediante análisis de ADN")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    @PostMapping("/mutant")
    @Operation(
            summary = "Verificar si un ADN es mutante",
            description = "Analiza una secuencia de ADN y determina si corresponde a un mutante. " +
                    "Retorna 200-OK si es mutante, 403-Forbidden si no lo es."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "El ADN corresponde a un mutante"
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "El ADN no corresponde a un mutante (humano)"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "La secuencia de ADN es inválida",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<Void> checkMutant(@Validated @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyzeDna(request.getDna());

        return isMutant
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @GetMapping("/stats")
    @Operation(
            summary = "Obtener estadísticas de verificaciones",
            description = "Retorna las estadísticas de las verificaciones de ADN: " +
                    "cantidad de mutantes, humanos y el ratio entre ellos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatsResponse.class)
                    )
            )
    })
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = statsService.getStats();
        return ResponseEntity.ok(stats);
    }
}
