package co.umariana.lamento.persistencia;

import co.umariana.lamento.modelo.Lamento;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
        List<Lamento> lamentos = new ArrayList<>();
        if (!Files.exists(archivo)) {
            return lamentos;
        }
        for (String linea : Files.readAllLines(archivo, StandardCharsets.UTF_8)) {
            if (linea.isBlank()) {
                continue;
            }
            Lamento lamento = interpretar(linea);
            if (lamento != null) {
                lamentos.add(lamento);
            }
        }
        return lamentos;
    }

    @Override
    public void guardar(List<Lamento> lamentos) throws IOException {
        // Con una ruta relativa como "lamentos.txt" no hay carpeta padre: getParent() devuelve null.
        Path carpeta = archivo.getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }
        List<String> lineas = new ArrayList<>();
        for (Lamento lamento : lamentos) {
            lineas.add(aLinea(lamento));
        }
        Files.write(archivo, lineas, StandardCharsets.UTF_8);
    }

    /** Convierte una línea del archivo en un lamento, o devuelve {@code null} si está dañada. */
    private static Lamento interpretar(String linea) {
        String[] campos = linea.split(";", -1); // -1 conserva los campos vacíos al final
        if (campos.length != 7) {
            return null;
        }
        try {
            return new Lamento(campos[0], campos[1], campos[2], Integer.parseInt(campos[3]),
                    Double.parseDouble(campos[4]), campos[5], campos[6]);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Escribe un lamento en el formato del archivo. */
    private static String aLinea(Lamento lamento) {
        return String.join(";",
                lamento.getCodigo(),
                lamento.getTitulo(),
                lamento.getCategoria(),
                String.valueOf(lamento.getIndignacion()),
                String.valueOf(lamento.getDuracionSeg()),
                lamento.getDescripcion(),
                lamento.getArchivo());
    }
}
