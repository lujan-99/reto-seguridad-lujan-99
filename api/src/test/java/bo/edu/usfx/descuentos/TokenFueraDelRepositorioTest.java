package bo.edu.usfx.descuentos;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * R5: el token de administracion solo llega por la variable ADMIN_TOKEN. Con la configuracion versionada
 * (sin la variable) el endpoint queda deshabilitado; si alguien vuelve a escribir un token en
 * application.properties, esta prueba falla.
 */
@SpringBootTest
@AutoConfigureMockMvc
class TokenFueraDelRepositorioTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void laConfiguracionVersionadaNoTraeTokenDeAdministracion() throws Exception {
        mvc.perform(get("/api/admin/estadisticas").header("X-Admin-Token", "cualquier-valor-0123456789"))
                .andExpect(status().isForbidden());
    }
}
