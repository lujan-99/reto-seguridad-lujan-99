package bo.edu.usfx.descuentos;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/** R1: toda respuesta de la API, tambien las de error, lleva las cabeceras de seguridad. */
@SpringBootTest
@AutoConfigureMockMvc
class CabecerasSeguridadTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void todasLasRespuestasLlevanLasCabecerasDeSeguridad() throws Exception {
        mvc.perform(get("/api/info"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("Content-Security-Policy", containsString("default-src 'none'")))
                .andExpect(header().string("Referrer-Policy", "no-referrer"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(header().string("Strict-Transport-Security", containsString("max-age=")));
    }

    @Test
    void lasCabecerasTambienAparecenEnLosErrores() throws Exception {
        mvc.perform(get("/api/no-existe"))
                .andExpect(status().isNotFound())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"));
    }
}
