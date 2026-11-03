package co.umariana.lamento.modelo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LamentoTest {

    private static Lamento lamento(String codigo, String titulo, String categoria, int indignacion,
                                   double duracion, String descripcion, String archivo) {
        return new Lamento(codigo, titulo, categoria, indignacion, duracion, descripcion, archivo);
    }

    private static Lamento valido() {
        return lamento("L001", "Jueputa nivel 1", "Insulto", 2, 1.32, "Insulto tranquilo", "hp_1.mp3");
    }

    @Test
    @DisplayName("un lamento correcto es válido")
    void valido_noLanza() {
        assertDoesNotThrow(() -> valido().validar());
    }

    @Test
    @DisplayName("la descripción puede estar vacía")
    void descripcionVaciaEsValida() {
        assertDoesNotThrow(() -> lamento("L1", "T", "C", 5, 1, "", "a.mp3").validar());
    }

    @Test
    @DisplayName("los límites de la indignación (1 y 10) son válidos")
    void limitesValidos() {
        assertDoesNotThrow(() -> lamento("L1", "T", "C", 1, 1, "", "a.mp3").validar());
        assertDoesNotThrow(() -> lamento("L1", "T", "C", 10, 1, "", "a.mp3").validar());
    }

    @Test
    @DisplayName("la indignación fuera de 1..10 es inválida")
    void indignacionFueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 0, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 11, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", -3, 1, "", "a.mp3").validar());
    }

    @Test
    @DisplayName("la duración debe ser mayor que 0")
    void duracionInvalida() {
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, 0, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, -1.5, "", "a.mp3").validar());
    }

    @Test
    @DisplayName("código, título, categoría y archivo no pueden ser nulos ni estar en blanco")
    void textosObligatorios() {
        assertThrows(IllegalArgumentException.class, () -> lamento(null, "T", "C", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("  ", "T", "C", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "", "C", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", null, "C", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", " ", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, 1, "", "").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, 1, "", null).validar());
    }

    @Test
    @DisplayName("la descripción no puede ser nula")
    void descripcionNula() {
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, 1, null, "a.mp3").validar());
    }

    @Test
    @DisplayName("ningún texto puede contener ';' (separa los campos del .txt)")
    void sinPuntoYComa() {
        assertThrows(IllegalArgumentException.class, () -> lamento("L;1", "T", "C", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T;x", "C", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C;x", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, 1, "a;b", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, 1, "", "a;.mp3").validar());
    }

    @Test
    @DisplayName("ningún texto puede contener saltos de línea")
    void sinSaltosDeLinea() {
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T\nx", "C", 5, 1, "", "a.mp3").validar());
        assertThrows(IllegalArgumentException.class, () -> lamento("L1", "T", "C", 5, 1, "uno\r\ndos", "a.mp3").validar());
    }

    @Test
    @DisplayName("el mensaje de error dice qué está mal")
    void mensajeDelError() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> lamento("L1", "T", "C", 11, 1, "", "a.mp3").validar());
        assertNotNull(e.getMessage());
        assertFalse(e.getMessage().isBlank());
    }
}
