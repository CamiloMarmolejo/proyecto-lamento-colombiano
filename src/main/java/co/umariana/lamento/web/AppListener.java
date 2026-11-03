package co.umariana.lamento.web;

import co.umariana.lamento.estructura.ListaCircular;
import co.umariana.lamento.modelo.GestorLamentos;
import co.umariana.lamento.modelo.Lamento;
import co.umariana.lamento.persistencia.RepositorioTxt;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.nio.file.Path;

/** Arma el modelo al arrancar el servidor (lista + archivo + gestor). NO se modifica. */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent evento) {
        Path archivo = Path.of(System.getProperty("lamentos.archivo", "lamentos.txt"));
        try {
            Contexto.usar(new GestorLamentos(new ListaCircular<Lamento>(), new RepositorioTxt(archivo)));
            evento.getServletContext().log("Lamentómetro listo. Datos en: " + archivo.toAbsolutePath());
        } catch (Throwable e) {
            // Mientras el equipo no termine sus clases, la aplicación arranca igual pero sin modelo.
            evento.getServletContext().log("El modelo todavía no está disponible: " + e);
            Contexto.usar(null);
        }
    }
}
