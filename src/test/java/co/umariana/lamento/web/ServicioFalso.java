package co.umariana.lamento.web;

import co.umariana.lamento.modelo.Lamento;
import co.umariana.lamento.modelo.ServicioLamentos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Servicio de mentira que anota lo que le piden: permite probar los servlets sin el resto del equipo. */
class ServicioFalso implements ServicioLamentos {

    final List<Lamento> datos = new ArrayList<>();
    final List<Lamento> ranking = new ArrayList<>();
    final Map<String, Double> promedios = new TreeMap<>();
    final List<String> llamadas = new ArrayList<>();
    RuntimeException fallarAlAgregar = null;
    Lamento cursorActual = null;
    Lamento cursorSiguiente = null;
    Lamento cursorAnterior = null;

    @Override public void agregar(Lamento l) {
        llamadas.add("agregar");
        if (fallarAlAgregar != null) throw fallarAlAgregar;
        datos.add(l);
    }
    @Override public Lamento buscar(String codigo) { llamadas.add("buscar"); return null; }
    @Override public boolean eliminar(String codigo) { llamadas.add("eliminar:" + codigo); return true; }
    @Override public List<Lamento> listar() { llamadas.add("listar"); return datos; }
    @Override public List<Lamento> ranking() { llamadas.add("ranking"); return ranking; }
    @Override public Map<String, Double> promedioPorCategoria() { llamadas.add("promedios"); return promedios; }
    @Override public int total() { return datos.size(); }
    @Override public Lamento actual() { llamadas.add("actual"); return cursorActual; }
    @Override public Lamento siguiente() { llamadas.add("siguiente"); return cursorSiguiente; }
    @Override public Lamento anterior() { llamadas.add("anterior"); return cursorAnterior; }
}
