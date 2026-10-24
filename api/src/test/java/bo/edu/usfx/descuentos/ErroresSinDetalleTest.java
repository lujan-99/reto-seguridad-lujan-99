package bo.edu.usfx.descuentos;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** R3: ningun error devuelve clases, trazas ni mensajes internos; el detalle queda en el registro del servidor. */
@SpringBootTest
@AutoConfigureMockMvc
class ErroresSinDetalleTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void unCuerpoIlegibleDa400SinDetalleInterno() throws Exception {
        mvc.perform(post("/api/descuento").contentType(MediaType.APPLICATION_JSON).content("{esto no es json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("La solicitud no es valida")))
                .andExpect(jsonPath("$.traza").doesNotExist())
                .andExpect(content().string(not(containsString("Exception"))))
                .andExpect(content().string(not(containsString("springframework"))));
    }

    @Test
    void unaRutaInexistenteNoFiltraInformacion() throws Exception {
        mvc.perform(get("/api/no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("No encontrado")))
                .andExpect(jsonPath("$.trace").doesNotExist());
    }

    @Test
    void unMetodoNoPermitidoDa405ConMensajeGenerico() throws Exception {
        mvc.perform(get("/api/descuento"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().string(not(containsString("Request method"))));
    }

    @Test
    void unaReglaDeNegocioSigueExplicandoQueEstaMal() throws Exception {
        mvc.perform(post("/api/descuento").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"precio\":100,\"porcentaje\":150}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isString())
                .andExpect(jsonPath("$.traza").doesNotExist());
    }
}
