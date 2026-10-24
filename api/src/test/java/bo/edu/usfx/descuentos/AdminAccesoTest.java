package bo.edu.usfx.descuentos;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** R4: probar el verificador no basta; hay que probar que el endpoint lo usa. */
@SpringBootTest(properties = "app.admin-token=token-de-prueba-0123456789")
@AutoConfigureMockMvc
class AdminAccesoTest {

    private static final String TOKEN = "token-de-prueba-0123456789";

    @Autowired
    private MockMvc mvc;

    @Test
    void adminSinTokenDa401() throws Exception {
        mvc.perform(get("/api/admin/estadisticas"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("No autorizado")));
    }

    @Test
    void adminConTokenIncorrectoDa401() throws Exception {
        mvc.perform(get("/api/admin/estadisticas").header("X-Admin-Token", "otro-token-0123456789"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void adminConTokenCorrectoCuentaLosCalculos() throws Exception {
        mvc.perform(post("/api/descuento").contentType(MediaType.APPLICATION_JSON)
                .content("{\"precio\":100,\"porcentaje\":10}"));
        mvc.perform(get("/api/admin/estadisticas").header("X-Admin-Token", TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.calculos").isNumber());
    }
}
