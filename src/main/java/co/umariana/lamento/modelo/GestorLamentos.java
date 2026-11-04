package co.umariana.lamento.modelo;

import co.umariana.lamento.estructura.Lista;
import co.umariana.lamento.persistencia.Repositorio;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
        try {
            for (Lamento lamento : repositorio.cargar()) {
                lista.agregar(lamento);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void agregar(Lamento lamento) {
        lamento.validar();
        if (buscar(lamento.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un lamento con el código " + lamento.getCodigo());
        }
        lista.agregar(lamento);
        guardar();
    }

    @Override
    public Lamento buscar(String codigo) {
        if (codigo == null) {
            return null;
        }
        for (Lamento lamento : lista) {
            if (lamento.getCodigo().equals(codigo)) {
                return lamento;
            }
        }
        return null;
    }

    @Override
    public boolean eliminar(String codigo) {
        int indice = indiceDe(codigo);
        if (indice < 0) {
            return false;
        }
        lista.eliminar(indice);
        guardar();
        return true;
    }

    @Override
    public List<Lamento> listar() {
        List<Lamento> copia = new ArrayList<>();
        for (Lamento lamento : lista) {
            copia.add(lamento);
        }
        return copia;
    }

    @Override
    public List<Lamento> ranking() {
        List<Lamento> copia = listar();
        copia.sort(Comparator.comparingInt(Lamento::getIndignacion).reversed()
                .thenComparing(Lamento::getCodigo));
        return copia;
    }

    @Override
    public Map<String, Double> promedioPorCategoria() {
        Map<String, Integer> sumas = new TreeMap<>();
        Map<String, Integer> cuentas = new TreeMap<>();
        for (Lamento lamento : lista) {
            sumas.merge(lamento.getCategoria(), lamento.getIndignacion(), Integer::sum);
            cuentas.merge(lamento.getCategoria(), 1, Integer::sum);
        }
        Map<String, Double> promedios = new TreeMap<>();
        for (Map.Entry<String, Integer> entrada : sumas.entrySet()) {
            promedios.put(entrada.getKey(), (double) entrada.getValue() / cuentas.get(entrada.getKey()));
        }
        return promedios;
    }

    @Override
    public int total() {
        return lista.tamano();
    }

    @Override
    public Lamento actual() {
        return lista.actual();
    }

    @Override
    public Lamento siguiente() {
        if (lista.estaVacia()) {
            return null;
        }
        lista.avanzar();
        return lista.actual();
    }

    @Override
    public Lamento anterior() {
        if (lista.estaVacia()) {
            return null;
        }
        lista.retroceder();
        return lista.actual();
    }

    // ---------- Auxiliares privados ----------

    /** Posición del lamento con ese código en la lista, o -1 si no está. */
    private int indiceDe(String codigo) {
        if (codigo == null) {
            return -1;
        }
        for (int i = 0; i < lista.tamano(); i++) {
            if (lista.obtener(i).getCodigo().equals(codigo)) {
                return i;
            }
        }
        return -1;
    }

    /** Guarda TODOS los lamentos con el repositorio; si falla, relanza como UncheckedIOException. */
    private void guardar() {
        try {
            repositorio.guardar(listar());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}