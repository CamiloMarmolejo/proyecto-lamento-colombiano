package co.umariana.lamento.modelo;

import co.umariana.lamento.estructura.Lista;
import co.umariana.lamento.persistencia.Repositorio;

import java.util.List;
import java.util.Map;

/**
 * Reglas de negocio del Lamentómetro (tarea de MODELO).
 *
 * <p>Guarda los lamentos en una {@link Lista} (la estructura de datos del equipo) y, después de
 * cada cambio, los guarda con el {@link Repositorio}. No debe usar {@code ArrayList} ni otra
 * colección de Java para guardar los datos: eso lo hace la {@code Lista}.
 */
public class GestorLamentos implements ServicioLamentos {

    private final Lista<Lamento> lista;
    private final Repositorio repositorio;

    /**
     * Al crearse, carga con el repositorio los lamentos ya guardados y los agrega a la lista.
     * Si el repositorio lanza {@link java.io.IOException}, se relanza como {@link java.io.UncheckedIOException}.
     */
    public GestorLamentos(Lista<Lamento> lista, Repositorio repositorio) {
        this.lista = lista;
        this.repositorio = repositorio;
        // TODO (MODELO): cargar los lamentos del repositorio y agregarlos a la lista.
    }

    @Override
    public void agregar(Lamento lamento) {
        // TODO (MODELO): validar (lamento.validar()), rechazar código repetido
        //  (IllegalArgumentException), agregar al final de la lista y guardar con el repositorio.
        throw new UnsupportedOperationException("TODO: agregar()");
    }

    @Override
    public Lamento buscar(String codigo) {
        // TODO (MODELO)
        throw new UnsupportedOperationException("TODO: buscar()");
    }

    @Override
    public boolean eliminar(String codigo) {
        // TODO (MODELO): si existe, eliminarlo de la lista, guardar y devolver true.
        throw new UnsupportedOperationException("TODO: eliminar()");
    }

    @Override
    public List<Lamento> listar() {
        // TODO (MODELO): devolver los lamentos en el orden de la lista.
        throw new UnsupportedOperationException("TODO: listar()");
    }

    @Override
    public List<Lamento> ranking() {
        // TODO (MODELO)
        throw new UnsupportedOperationException("TODO: ranking()");
    }

    @Override
    public Map<String, Double> promedioPorCategoria() {
        // TODO (MODELO)
        throw new UnsupportedOperationException("TODO: promedioPorCategoria()");
    }

    @Override
    public int total() {
        // TODO (MODELO)
        throw new UnsupportedOperationException("TODO: total()");
    }

    @Override
    public Lamento actual() {
        // TODO (MODELO): usar el cursor de la lista.
        throw new UnsupportedOperationException("TODO: actual()");
    }

    @Override
    public Lamento siguiente() {
        // TODO (MODELO)
        throw new UnsupportedOperationException("TODO: siguiente()");
    }

    @Override
    public Lamento anterior() {
        // TODO (MODELO)
        throw new UnsupportedOperationException("TODO: anterior()");
    }
}
