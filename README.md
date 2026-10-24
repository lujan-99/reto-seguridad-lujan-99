# reto-seguridad — Práctica 10, Parte B (COM450)

La misma aplicación que `descuentos-segura`, pero con **8 defectos de seguridad sembrados**: seis en el código y la
configuración, dos en el pipeline que debía detectarlos. **Las pruebas funcionales pasan (casi todas)**: el reto es
demostrar que un pipeline en verde no es una aplicación segura.

No se te dice dónde están. Se encuentran con las herramientas de la Parte A:

```bash
scripts/buscar-secretos.sh                      # ¿qué hay en el repositorio?
scripts/auditoria-seguridad.sh http://localhost:8080   # ¿qué le pasa a la API en ejecución?
```

y leyendo el informe de ZAP, la salida de Trivy y los jobs de `.github/workflows/seguridad.yml`.

Reglas del reto (detalle en la guía, Parte B):

- Un commit por defecto, con mensaje convencional (`fix(seguridad): …`).
- No se borran pruebas ni controles; no se baja ninguna puerta (umbral, severidad, `exit-code`) para "ponerlo en verde".
- No se usa `--no-verify`.
- Para cada defecto: síntoma, categoría OWASP, corrección y la prueba o el control que ahora lo vigila.

Estructura: igual que `descuentos-segura/` (ver su README), sin los archivos de prueba de seguridad de la API.
