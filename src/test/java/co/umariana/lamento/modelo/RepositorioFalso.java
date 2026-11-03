package co.umariana.lamento.modelo;

import co.umariana.lamento.persistencia.Repositorio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** Repositorio de mentira (en memoria) para probar el modelo sin depender del archivo .txt. */
class RepositorioFalso implements Repositorio {

    List<Lamento> guardados = new ArrayList<>();
    int vecesGuardado = 0;
    boolean fallar = false;

    @Override
    public List<Lamento> cargar() throws IOException {
        if (fallar) throw new IOException("fallo simulado");
        return new ArrayList<>(guardados);
    }

    @Override
    public void guardar(List<Lamento> lamentos) throws IOException {
        if (fallar) throw new IOException("fallo simulado");
        guardados = new ArrayList<>(lamentos);
        vecesGuardado++;
    }
}
