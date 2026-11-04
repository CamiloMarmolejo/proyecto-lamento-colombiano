package co.umariana.lamento.modelo;

/**
 * Un lamento del Lamentómetro: un audio con su título, categoría y nivel de
 * indignación.
 *
 * <p>
 * La clase ya tiene sus atributos, constructor y getters. Lo que falta es
 * {@link #validar()}
 * (tarea de MODELO).
 */
public class Lamento {

    private final String codigo;
    private final String titulo;
    private final String categoria;
    private final int indignacion;
    private final double duracionSeg;
    private final String descripcion;
    private final String archivo;

    public Lamento(String codigo, String titulo, String categoria, int indignacion,
            double duracionSeg, String descripcion, String archivo) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.categoria = categoria;
        this.indignacion = indignacion;
        this.duracionSeg = duracionSeg;
        this.descripcion = descripcion;
        this.archivo = archivo;
    }

    /**
     * Revisa que el lamento sea válido. Si no lo es, lanza
     * {@link IllegalArgumentException}
     * con un mensaje que explique el problema. Reglas:
     * <ul>
     * <li>{@code codigo}, {@code titulo}, {@code categoria} y {@code archivo} no
     * pueden ser nulos ni estar en blanco.</li>
     * <li>{@code indignacion} debe estar entre 1 y 10 (incluidos).</li>
     * <li>{@code duracionSeg} debe ser mayor que 0.</li>
     * <li>{@code descripcion} puede estar vacía, pero no ser nula.</li>
     * <li>Ningún texto puede contener el carácter {@code ;} (separa los campos del
     * archivo .txt)
     * ni saltos de línea.</li>
     * </ul>
     */
    public void validar() {
        textoObligatorio(codigo, "El código");
        textoObligatorio(titulo, "El título");
        textoObligatorio(categoria, "La categoría");
        textoObligatorio(archivo, "El archivo");

        if (descripcion == null) {
            throw new IllegalArgumentException("La descripción no puede ser nula");
        }
        sinSeparadores(descripcion, "La descripción");

        if (indignacion < 1 || indignacion > 10) {
            throw new IllegalArgumentException("La indignación debe estar entre 1 y 10");
        }
        // TODO 1: la duración debe ser mayor que 0
    }

    private static void textoObligatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " es obligatorio");
        }
        sinSeparadores(valor, campo);
    }

    private static void sinSeparadores(String valor, String campo) {
        // TODO 2: lanzar IllegalArgumentException si contiene ';' o saltos de línea (\n
        // y \r)
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getIndignacion() {
        return indignacion;
    }

    public double getDuracionSeg() {
        return duracionSeg;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getArchivo() {
        return archivo;
    }

    @Override
    public String toString() {
        return codigo + " - " + titulo + " (" + categoria + ", indignación " + indignacion + ")";
    }
}
