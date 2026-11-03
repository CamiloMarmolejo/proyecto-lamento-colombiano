package co.umariana.lamento.persistencia;

import co.umariana.lamento.modelo.Lamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RepositorioTxtTest {

    @TempDir
    Path carpeta;

    private Path archivo;
    private RepositorioTxt repo;

    @BeforeEach
    void preparar() {
        archivo = carpeta.resolve("lamentos.txt");
        repo = new RepositorioTxt(archivo);
    }

    private static Lamento l(String codigo, int indignacion, double duracion) {
        return new Lamento(codigo, "Título " + codigo, "Insulto", indignacion, duracion, "Descripción " + codigo, codigo + ".mp3");
    }

    @Test
    @DisplayName("si el archivo no existe, cargar devuelve una lista vacía")
    void archivoInexistente() throws IOException {
        assertTrue(repo.cargar().isEmpty());
    }

    @Test
    @DisplayName("guardar escribe una línea por lamento con el formato pedido")
    void formatoDelArchivo() throws IOException {
        repo.guardar(List.of(
                new Lamento("L001", "Jueputa nivel 1", "Insulto", 2, 1.32, "Insulto tranquilo", "hp_1.mp3"),
                new Lamento("L002", "Otro", "Tráfico", 7, 5.0, "", "hp_2.mp3")));
        List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
        assertEquals(List.of(
                "L001;Jueputa nivel 1;Insulto;2;1.32;Insulto tranquilo;hp_1.mp3",
                "L002;Otro;Tráfico;7;5.0;;hp_2.mp3"), lineas);
    }

    @Test
    @DisplayName("lo que se guarda se puede cargar igual (ida y vuelta)")
    void idaYVuelta() throws IOException {
        repo.guardar(List.of(l("A", 3, 1.25), l("B", 10, 12.5), l("C", 1, 0.5)));
        List<Lamento> cargados = repo.cargar();
        assertEquals(3, cargados.size());
        Lamento b = cargados.get(1);
        assertEquals("B", b.getCodigo());
        assertEquals("Título B", b.getTitulo());
        assertEquals("Insulto", b.getCategoria());
        assertEquals(10, b.getIndignacion());
        assertEquals(12.5, b.getDuracionSeg(), 0.0001);
        assertEquals("Descripción B", b.getDescripcion());
        assertEquals("B.mp3", b.getArchivo());
        assertEquals("A", cargados.get(0).getCodigo(), "debe conservar el orden");
        assertEquals("C", cargados.get(2).getCodigo());
    }

    @Test
    @DisplayName("conserva tildes y ñ (UTF-8)")
    void utf8() throws IOException {
        repo.guardar(List.of(new Lamento("L1", "¡Qué rabia, niño!", "Fútbol", 5, 1.0, "Año nuevo, vida nueva", "a.mp3")));
        Lamento c = repo.cargar().get(0);
        assertEquals("¡Qué rabia, niño!", c.getTitulo());
        assertEquals("Fútbol", c.getCategoria());
        assertEquals("Año nuevo, vida nueva", c.getDescripcion());
    }

    @Test
    @DisplayName("guardar reemplaza el contenido anterior (no agrega al final)")
    void guardarReemplaza() throws IOException {
        repo.guardar(List.of(l("A", 1, 1), l("B", 2, 1), l("C", 3, 1)));
        repo.guardar(List.of(l("Z", 9, 1)));
        List<Lamento> cargados = repo.cargar();
        assertEquals(1, cargados.size());
        assertEquals("Z", cargados.get(0).getCodigo());
    }

    @Test
    @DisplayName("guardar una lista vacía deja el archivo vacío")
    void guardarVacia() throws IOException {
        repo.guardar(List.of(l("A", 1, 1)));
        repo.guardar(List.of());
        assertTrue(Files.exists(archivo));
        assertTrue(repo.cargar().isEmpty());
    }

    @Test
    @DisplayName("guardar crea las carpetas que falten")
    void creaCarpetas() throws IOException {
        Path profundo = carpeta.resolve("datos").resolve("2026").resolve("lamentos.txt");
        new RepositorioTxt(profundo).guardar(List.of(l("A", 1, 1)));
        assertTrue(Files.exists(profundo));
        assertEquals(1, new RepositorioTxt(profundo).cargar().size());
    }

    @Test
    @DisplayName("cargar ignora las líneas en blanco")
    void ignoraLineasEnBlanco() throws IOException {
        Files.writeString(archivo, "\nL1;T;C;3;1.5;d;a.mp3\n\n   \nL2;T2;C;4;2.5;;b.mp3\n", StandardCharsets.UTF_8);
        List<Lamento> cargados = repo.cargar();
        assertEquals(2, cargados.size());
        assertEquals("L2", cargados.get(1).getCodigo());
        assertEquals("", cargados.get(1).getDescripcion());
    }

    @Test
    @DisplayName("cargar ignora las líneas dañadas y sigue con las demás")
    void ignoraLineasDanadas() throws IOException {
        Files.writeString(archivo, String.join("\n",
                "L1;T;C;3;1.5;d;a.mp3",
                "esta linea no tiene el formato",
                "L2;T;C;no-es-numero;1.5;d;b.mp3",
                "L3;T;C;3;tampoco;d;c.mp3",
                "L4;T;C;3;1.5;d",
                "L5;T;C;3;1.5;d;e.mp3;sobra",
                "L6;T;C;8;2.0;d;f.mp3") + "\n", StandardCharsets.UTF_8);
        List<Lamento> cargados = repo.cargar();
        assertEquals(List.of("L1", "L6"), cargados.stream().map(Lamento::getCodigo).toList());
    }
}
