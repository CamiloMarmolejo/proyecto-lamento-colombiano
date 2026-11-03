package co.umariana.lamento.persistencia;

import co.umariana.lamento.modelo.Lamento;

import java.io.IOException;
import java.util.List;

/** Contrato de la persistencia. NO se modifica. {@link RepositorioTxt} es la implementación. */
public interface Repositorio {

    /** Lee todos los lamentos guardados. Si todavía no hay nada guardado, devuelve una lista vacía. */
    List<Lamento> cargar() throws IOException;

    /** Guarda TODOS los lamentos, reemplazando lo que hubiera antes. */
    void guardar(List<Lamento> lamentos) throws IOException;
}
