package org.example.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MutantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // ==================== TESTS POST /mutant ====================

    @Test
    void testMutantEndpoint_ReturnOk() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isOk());
    }

    @Test
    void testHumanEndpoint_ReturnForbidden() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGCGA","CTGTGC","TTATGT","ATATGG","CCCCTA","TCACTG"]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isForbidden());
    }

    @Test
    void testInvalidDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGX","CAGT"]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testEmptyDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": []
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNullDna_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": null
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testNonSquareMatrix_ReturnBadRequest() throws Exception {
        String dnaJson = """
            {
                "dna": ["ATGC","CAG","TTAT"]
            }
            """;

        mockMvc.perform(post("/mutant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dnaJson))
                .andExpect(status().isBadRequest());
    }

    // ==================== TESTS GET /stats ====================

    @Test
    void testStatsEndpoint_ReturnOk() throws Exception {
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna").exists())
                .andExpect(jsonPath("$.count_human_dna").exists())
                .andExpect(jsonPath("$.ratio").exists());
    }

    @Test
    void testStatsEndpoint_ReturnValidJson() throws Exception {
        // Primero verificar algunos DNAs
        String mutantDna = """
            {
                "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
            }
            """;
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mutantDna));

        String humanDna = """
            {
                "dna": ["ATGCGA","CTGTGC","TTATGT","ATATGG","CCCCTA","TCACTG"]
            }
            """;
        mockMvc.perform(post("/mutant")
                .contentType(MediaType.APPLICATION_JSON)
                .content(humanDna));

        // Verificar stats
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.count_mutant_dna").isNumber())
                .andExpect(jsonPath("$.count_human_dna").isNumber())
                .andExpect(jsonPath("$.ratio").isNumber());
    }
}
