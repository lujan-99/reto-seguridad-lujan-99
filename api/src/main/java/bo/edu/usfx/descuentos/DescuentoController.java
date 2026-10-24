package bo.edu.usfx.descuentos;

import java.util.Arrays;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DescuentoController {

    private final CalculadoraDescuentos calculadora;
    private final Estadisticas estadisticas;

    public DescuentoController(CalculadoraDescuentos calculadora, Estadisticas estadisticas) {
        this.calculadora = calculadora;
        this.estadisticas = estadisticas;
    }

    @PostMapping("/descuento")
    public RespuestaDescuento calcular(@RequestBody SolicitudDescuento solicitud) {
        double precioFinal = calculadora.calcularPrecioFinal(solicitud.precio(), solicitud.porcentaje());
        estadisticas.registrarCalculo();
        return new RespuestaDescuento(solicitud.precio(), solicitud.porcentaje(), precioFinal);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", e.getMessage()));
    }

    // Para depurar mas rapido: que el cliente vea que paso.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> cualquierError(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.toString(), "traza", Arrays.toString(e.getStackTrace())));
    }
}
