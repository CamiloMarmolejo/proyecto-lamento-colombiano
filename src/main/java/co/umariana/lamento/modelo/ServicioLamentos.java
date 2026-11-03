package co.umariana.lamento.modelo;

import java.util.List;
import java.util.Map;

/**
 * Lo que la aplicación web necesita del modelo. NO se modifica.
 * {@link GestorLamentos} es la implementación (tarea de MODELO).
 */
public interface ServicioLamentos {

    /** Valida el lamento, rechaza códigos repetidos y guarda el cambio en el archivo. */
    void agregar(Lamento lamento);

    /** Devuelve el lamento con ese código, o {@code null} si no existe. */
    Lamento buscar(String codigo);

    /** Elimina el lamento con ese código y guarda el cambio. Devuelve {@code false} si no existía. */
    boolean eliminar(String codigo);

    /** Todos los lamentos, en el orden de la lista. */
    List<Lamento> listar();

    /** Lista NUEVA ordenada de mayor a menor indignación (empate: por código). No altera el orden de la lista. */
    List<Lamento> ranking();

    /** Promedio de indignación por categoría, ordenado alfabéticamente por categoría. */
    Map<String, Double> promedioPorCategoria();

    /** Cantidad de lamentos guardados. */
    int total();

    /** Lamento donde está el cursor (el reproductor), o {@code null} si no hay lamentos. */
    Lamento actual();

    /** Mueve el cursor al siguiente lamento y lo devuelve ({@code null} si no hay lamentos). */
    Lamento siguiente();

    /** Mueve el cursor al lamento anterior y lo devuelve ({@code null} si no hay lamentos). */
    Lamento anterior();
}
