package co.umariana.lamento.web;

import co.umariana.lamento.modelo.Lamento;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/** Prueba los servlets con una petición/respuesta simuladas y un servicio de mentira. */
class ServletsTest {

    private ServicioFalso servicio;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private RequestDispatcher despachador;

    @BeforeEach
    void preparar() {
        servicio = new ServicioFalso();
        Contexto.usar(servicio);
        req = mock(HttpServletRequest.class);
        resp = mock(HttpServletResponse.class);
        despachador = mock(RequestDispatcher.class);
        when(req.getRequestDispatcher(anyString())).thenReturn(despachador);
    }

    @AfterEach
    void limpiar() {
        Contexto.usar(null);
    }

    private static Lamento l(String codigo, int indignacion) {
        return new Lamento(codigo, "T" + codigo, "Insulto", indignacion, 1.5, "d", codigo + ".mp3");
    }

    private void aplicarFormulario(String codigo, String indignacion, String duracion) {
        when(req.getParameter("codigo")).thenReturn(codigo);
        when(req.getParameter("titulo")).thenReturn("Mi titulo");
        when(req.getParameter("categoria")).thenReturn("Tráfico");
        when(req.getParameter("indignacion")).thenReturn(indignacion);
        when(req.getParameter("duracion")).thenReturn(duracion);
        when(req.getParameter("descripcion")).thenReturn("Una descripción");
        when(req.getParameter("archivo")).thenReturn("x.mp3");
    }

    // ---------- /listar ----------

    @Test
    @DisplayName("/listar pone la lista y los promedios y reenvía a listar.jsp")
    void listar() throws Exception {
        servicio.datos.add(l("A", 3));
        servicio.promedios.put("Insulto", 3.0);

        new ListarLamentos().doGet(req, resp);

        verify(req).setAttribute("lamentos", servicio.datos);
        verify(req).setAttribute("promedios", servicio.promedios);
        verify(req).getRequestDispatcher("/WEB-INF/vistas/listar.jsp");
        verify(despachador).forward(req, resp);
    }

    @Test
    @DisplayName("/listar?orden=ranking muestra el ranking en lugar de la lista")
    void listarRanking() throws Exception {
        servicio.ranking.add(l("B", 9));
        when(req.getParameter("orden")).thenReturn("ranking");

        new ListarLamentos().doGet(req, resp);

        verify(req).setAttribute("lamentos", servicio.ranking);
        assertTrue(servicio.llamadas.contains("ranking"));
        verify(despachador).forward(req, resp);
    }

    @Test
    @DisplayName("/listar con otro valor de 'orden' usa el orden normal")
    void listarOrdenDesconocido() throws Exception {
        when(req.getParameter("orden")).thenReturn("loquesea");

        new ListarLamentos().doGet(req, resp);

        verify(req).setAttribute("lamentos", servicio.datos);
        assertFalse(servicio.llamadas.contains("ranking"));
    }

    // ---------- /registrar ----------

    @Test
    @DisplayName("GET /registrar muestra el formulario")
    void registrarGet() throws Exception {
        new RegistrarLamento().doGet(req, resp);

        verify(req).getRequestDispatcher("/WEB-INF/vistas/formulario.jsp");
        verify(despachador).forward(req, resp);
    }

    @Test
    @DisplayName("POST /registrar con datos correctos agrega el lamento y redirige a listar")
    void registrarOk() throws Exception {
        aplicarFormulario("N1", "7", "2.5");

        new RegistrarLamento().doPost(req, resp);

        
        assertEquals(1, servicio.datos.size());
        Lamento l = servicio.datos.get(0);
        assertEquals("N1", l.getCodigo());
        assertEquals("Mi titulo", l.getTitulo());
        assertEquals("Tráfico", l.getCategoria());
        assertEquals(7, l.getIndignacion());
        assertEquals(2.5, l.getDuracionSeg(), 0.0001);
        assertEquals("Una descripción", l.getDescripcion());
        assertEquals("x.mp3", l.getArchivo());
        verify(resp).sendRedirect("listar");
        verify(despachador, never()).forward(any(), any());
    }

    @Test
    @DisplayName("POST /registrar con indignación no numérica muestra el error en el formulario")
    void registrarNumeroInvalido() throws Exception {
        aplicarFormulario("N1", "mucha", "2.5");

        new RegistrarLamento().doPost(req, resp);

        assertTrue(servicio.datos.isEmpty());
        verify(req).setAttribute(eq("error"), anyString());
        verify(req).getRequestDispatcher("/WEB-INF/vistas/formulario.jsp");
        verify(despachador).forward(req, resp);
        verify(resp, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("POST /registrar con duración no numérica muestra el error en el formulario")
    void registrarDuracionInvalida() throws Exception {
        aplicarFormulario("N1", "5", "");

        new RegistrarLamento().doPost(req, resp);

        verify(req).setAttribute(eq("error"), anyString());
        verify(despachador).forward(req, resp);
        verify(resp, never()).sendRedirect(anyString());
    }

    @Test
    @DisplayName("POST /registrar muestra el mensaje cuando el modelo rechaza el lamento")
    void registrarRechazadoPorElModelo() throws Exception {
        aplicarFormulario("N1", "5", "2.5");
        servicio.fallarAlAgregar = new IllegalArgumentException("Ya existe un lamento con el código N1");

        new RegistrarLamento().doPost(req, resp);

        verify(req).setAttribute("error", "Ya existe un lamento con el código N1");
        verify(despachador).forward(req, resp);
        verify(resp, never()).sendRedirect(anyString());
    }

    // ---------- /eliminar ----------

    @Test
    @DisplayName("POST /eliminar elimina por código y redirige a listar")
    void eliminar() throws Exception {
        when(req.getParameter("codigo")).thenReturn("L003");

        new EliminarLamento().doPost(req, resp);

        assertTrue(servicio.llamadas.contains("eliminar:L003"));
        verify(resp).sendRedirect("listar");
    }

    // ---------- /reproductor ----------

    @Test
    @DisplayName("/reproductor sin acción muestra el lamento actual")
    void reproductorActual() throws Exception {
        servicio.cursorActual = l("A", 3);
        servicio.datos.add(servicio.cursorActual);

        new Reproductor().doGet(req, resp);

        verify(req).setAttribute("lamento", servicio.cursorActual);
        verify(req).setAttribute("total", 1);
        verify(req).getRequestDispatcher("/WEB-INF/vistas/reproductor.jsp");
        verify(despachador).forward(req, resp);
        assertFalse(servicio.llamadas.contains("siguiente"));
        assertFalse(servicio.llamadas.contains("anterior"));
    }

    @Test
    @DisplayName("/reproductor?accion=siguiente mueve el cursor al siguiente")
    void reproductorSiguiente() throws Exception {
        servicio.cursorSiguiente = l("B", 4);
        when(req.getParameter("accion")).thenReturn("siguiente");

        new Reproductor().doGet(req, resp);

        assertTrue(servicio.llamadas.contains("siguiente"));
        verify(req).setAttribute("lamento", servicio.cursorSiguiente);
        verify(despachador).forward(req, resp);
    }

    @Test
    @DisplayName("/reproductor?accion=anterior mueve el cursor al anterior")
    void reproductorAnterior() throws Exception {
        servicio.cursorAnterior = l("C", 5);
        when(req.getParameter("accion")).thenReturn("anterior");

        new Reproductor().doGet(req, resp);

        assertTrue(servicio.llamadas.contains("anterior"));
        verify(req).setAttribute("lamento", servicio.cursorAnterior);
    }

    @Test
    @DisplayName("/reproductor sin lamentos reenvía igual (lamento = null)")
    void reproductorVacio() throws Exception {
        new Reproductor().doGet(req, resp);

        verify(req).setAttribute("lamento", null);
        verify(req).setAttribute("total", 0);
        verify(despachador).forward(req, resp);
    }
}
