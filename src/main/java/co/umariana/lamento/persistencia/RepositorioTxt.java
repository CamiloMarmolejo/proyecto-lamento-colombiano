package co.umariana.lamento.persistencia;

import co.umariana.lamento.modelo.Lamento;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Guarda los lamentos en un archivo de texto (tarea de PERSISTENCIA).
 *
 * <p>Formato: UNA línea por lamento, con los campos separados por {@code ;} y en este orden:
 * <pre>codigo;titulo;categoria;indignacion;duracionSeg;descripcion;archivo</pre>
 * Ejemplo:
 * <pre>L001;Jueputa nivel 1;Insulto;2;1.32;Insulto tranquilo de todos los días;hp_1.mp3</pre>
 * <ul>
 *   <li>Codificación UTF-8 (para las tildes y la ñ).</li>
 *   <li>{@code duracionSeg} se escribe con punto decimal.</li>
 *   <li>{@code cargar}: si el archivo no existe devuelve una lista vacía; ignora las líneas en
 *       blanco y las líneas dañadas (cantidad de campos distinta de 7 o números inválidos).</li>
 *   <li>{@code guardar}: reemplaza todo el contenido del archivo y crea las carpetas que falten.
 *       Una lista vacía deja el archivo vacío.</li>
 * </ul>
 */
public class RepositorioTxt implements Repositorio {

    private final Path archivo;

    public RepositorioTxt(Path archivo) {
        this.archivo = archivo;
    }

    @Override
    public List<Lamento> cargar() throws IOException {
        // TODO (PERSISTENCIA)
        throw new UnsupportedOperationException("TODO: cargar()");
    }

    @Override
    public void guardar(List<Lamento> lamentos) throws IOException {
        // TODO (PERSISTENCIA)
        throw new UnsupportedOperationException("TODO: guardar()");
    }
}
