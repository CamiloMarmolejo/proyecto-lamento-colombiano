package co.umariana.lamento.web;

import co.umariana.lamento.modelo.ServicioLamentos;

/**
 * Punto único de acceso al modelo desde los servlets. NO se modifica.
 * Los servlets hacen {@code Contexto.servicio().listar()}, etc.
 */
public final class Contexto {

    private static volatile ServicioLamentos servicio;

    private Contexto() {
    }

    public static ServicioLamentos servicio() {
        ServicioLamentos s = servicio;
        if (s == null) {
            throw new IllegalStateException("El modelo no está disponible todavía. Revise la consola del servidor: "
                    + "probablemente falta implementar alguna clase (GestorLamentos, ListaCircular o RepositorioTxt).");
        }
        return s;
    }

    /** Lo usa {@link AppListener} al arrancar y las pruebas para poner un servicio de mentira. */
    public static void usar(ServicioLamentos nuevo) {
        servicio = nuevo;
    }
}
