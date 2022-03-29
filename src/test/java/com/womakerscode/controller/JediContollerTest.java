package com.womakerscode.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.womakerscode.model.Jedi;
import com.womakerscode.service.JediService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Teste do delete com erro - deletar um id ja deletado
// Teste do PUT com uma versao igual da ja existente - deve retornar um conflito


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JediContollerTest {

    @MockBean
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /jedi/1 - SUCCESS")
    public void testGetJediByIdWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);

        // execucao
        mockMvc.perform(get("/jedi/{id}", 1))

                // asserts
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("GET /jedi/1 - NOT FOUND")
    public void testGetJediByIdNotFound() throws Exception {

        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(get("/jedi/{1}", 1))
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("POST /jedi- SUCCESS")
    public void testCreateJediWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 10, 1);
        Mockito.doReturn(mockJedi).when(jediService).save(any());

        // execucao
        mockMvc.perform(post("/jedi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockJedi)))
                // asserts
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(10)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("POST /jedi- FAIL")
    public void testCreateJediFail() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 10, 1);
        Mockito.doReturn(null).when(jediService).save(any());

        // execucao
        mockMvc.perform(post("/jedi")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockJedi)))
                // asserts
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("PUT /jedi- SUCCESS")
    public void testUpdateJediWithSuccess() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 55, 1);
        Mockito.doReturn(true).when(jediService).update(eq(1), any());

        // execucao
        mockMvc.perform(put("/jedi/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockJedi)))
                // asserts
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("HanSolo")))
                .andExpect(jsonPath("$.strength", is(55)))
                .andExpect(jsonPath("$.version", is(1)));
    }

    @Test
    @DisplayName("PUT /jedi- NOT FOUND")
    public void testUpdateJediNotFound() throws Exception {

        // cenario
        Jedi mockJedi = new Jedi(1, "HanSolo", 55, 1);
        Mockito.doReturn(false).when(jediService).update(eq(1), any());

        // execucao
        mockMvc.perform(put("/jedi/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mockJedi)))
                // asserts
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /jedi- SUCCESS")
    public void testDeleteJediWithSuccess() throws Exception {

        // cenario
        Mockito.doReturn(true).when(jediService).delete(eq(1));

        // execucao
        mockMvc.perform(delete("/jedi/{id}", 1))
                // asserts
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /jedi- NOT FOUND")
    public void testDeleteJediNotFound() throws Exception {

        // cenario
        Mockito.doReturn(false).when(jediService).delete(eq(1));

        // execucao
        mockMvc.perform(delete("/jedi/{id}", 1))
                // asserts
                .andExpect(status().isNotFound());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
