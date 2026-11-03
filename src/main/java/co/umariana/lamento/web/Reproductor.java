package co.umariana.lamento.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * GET /reproductor  (tarea de SERVLETS)
 *
 * <p>Contrato (las pruebas lo revisan):
 * <ul>
 *   <li>Parámetro {@code accion}: {@code "siguiente"} usa {@code Contexto.servicio().siguiente()},
 *       {@code "anterior"} usa {@code anterior()}; sin parámetro (o con otro valor) usa {@code actual()}.</li>
 *   <li>Pone el lamento resultante (puede ser {@code null}) en el atributo {@code "lamento"} y la cantidad
 *       total en {@code "total"}.</li>
 *   <li>Reenvía a {@code /WEB-INF/vistas/reproductor.jsp}, que debe mostrar el lamento, un reproductor
 *       {@code <audio>} (los archivos están en {@code media/}) y los botones Anterior / Siguiente.</li>
 * </ul>
 */
@WebServlet("/reproductor")
public class Reproductor extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO (SERVLETS)
        throw new UnsupportedOperationException("TODO: Reproductor.doGet()");
    }
}
