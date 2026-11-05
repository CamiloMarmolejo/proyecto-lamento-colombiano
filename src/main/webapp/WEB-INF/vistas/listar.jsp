<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
    <%@ page
        import="java.util.List, java.util.Locale, java.util.Map, co.umariana.lamento.modelo.Lamento, co.umariana.lamento.web.Html"
        %>
        <%! private static final Locale ES=Locale.forLanguageTag("es-CO"); /** Clasifica la indignación para pintar el
            medidor: bajo, medio o alto. */ private static String clasificar(double indignacion) { if (indignacion>= 8)
            return "alto";
            if (indignacion >= 4) return "medio";
            return "bajo";
            }
            %>
            <% // Atributos que pone el servlet ListarLamentos List<?> lamentos = (List
                <?>) request.getAttribute("lamentos");
    Map<?, ?> promedios = (Map
                <?, ?>) request.getAttribute("promedios");
                boolean ranking = "ranking".equals(request.getParameter("orden"));
                int cantidad = (lamentos == null) ? 0 : lamentos.size();
                String[] medallas = {"🥇", "🥈", "🥉"};
                %>
                <!DOCTYPE html>
                <html lang="es">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>Lamentos · Lamentómetro</title>
                    <link rel="stylesheet" href="../css/estilos.css">
                </head>

                <body class="app">
                    <header class="sitio__cabecera">
                        <div class="contenedor sitio__barra">
                            <a class="marca" href="index.html"><span aria-hidden="true">🎙️</span> Lamentómetro</a>
                            <nav class="menu" aria-label="Principal">
                                <a href="index.html">Inicio</a>
                                <a href="catalogo">Catálogo</a>
                                <a href="listar" aria-current="page">Lamentos</a>
                                <a href="registrar">Registrar</a>
                                <a href="reproductor">Reproductor</a>
                            </nav>
                        </div>
                    </header>

                    <main>
                        <div class="encabezado">
                            <div>
                                <h1>Lamentos</h1>
                                <p>
                                    <%= cantidad %>
                                        <%= cantidad==1 ? "lamento guardado" : "lamentos guardados" %>
                                </p>
                            </div>
                            <div class="acciones">
                                <a class="boton" href="registrar">＋ Registrar lamento</a>
                                <a class="boton boton--secundario" href="reproductor">▶ Reproducir</a>
                            </div>
                        </div>

                        <nav class="pestanas" aria-label="Orden de la lista">
                            <a href="listar" <%=ranking ? "" : "aria-current=\" true\"" %>>Orden de la lista</a>
                            <a href="listar?orden=ranking" <%=ranking ? "aria-current=\" true\"" : "" %>>🏆 Ranking</a>
                        </nav>

                        <% if (cantidad==0) { %>
                            <section class="tarjeta vacio" aria-live="polite">
                                <span class="vacio__icono" aria-hidden="true">🔇</span>
                                <h2>Todavía no hay lamentos</h2>
                                <p>Agregue audios desde el catálogo o registre uno a mano.</p>
                                <div class="acciones acciones--centro">
                                    <a class="boton" href="catalogo">Ir al catálogo</a>
                                    <a class="boton boton--secundario" href="registrar">Registrar uno</a>
                                </div>
                            </section>
                            <% } else { %>
                                <div class="tabla-envoltura">
                                    <table class="tabla">
                                        <caption class="solo-lector">Lista de lamentos<%= ranking
                                                ? " ordenada por indignación" : "" %>
                                        </caption>
                                        <thead>
                                            <tr>
                                                <% if (ranking) { %>
                                                    <th scope="col" class="puesto">Puesto</th>
                                                    <% } %>
                                                        <th scope="col">Código</th>
                                                        <th scope="col">Título</th>
                                                        <th scope="col">Categoría</th>
                                                        <th scope="col">Indignación</th>
                                                        <th scope="col">Duración</th>
                                                        <th scope="col">Acción</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% int puesto=0; for (Object o : lamentos) { Lamento l=(Lamento) o;
                                                puesto++; int nivel=l.getIndignacion(); %>
                                                <tr>
                                                    <% if (ranking) { %>
                                                        <td class="puesto" data-etiqueta="Puesto">
                                                            <%= puesto <=3 ? medallas[puesto - 1] :
                                                                String.valueOf(puesto) %>
                                                        </td>
                                                        <% } %>
                                                            <td data-etiqueta="Código">
                                                                <code><%= Html.esc(l.getCodigo()) %></code>
                                                            </td>
                                                            <td class="titulo" data-etiqueta="Título">
                                                                <%= Html.esc(l.getTitulo()) %>
                                                            </td>
                                                            <td data-etiqueta="Categoría"><span class="etiqueta">
                                                                    <%= Html.esc(l.getCategoria()) %>
                                                                </span></td>
                                                            <td data-etiqueta="Indignación">
                                                                <div class="medidor medidor--<%= clasificar(nivel) %>"
                                                                    role="img"
                                                                    aria-label="Indignación <%= nivel %> de 10">
                                                                    <span class="medidor__pista"><span
                                                                            class="medidor__barra"
                                                                            style="--nivel:<%= nivel * 10 %>%"></span></span>
                                                                    <span class="medidor__valor">
                                                                        <%= nivel %>/10
                                                                    </span>
                                                                </div>
                                                            </td>
                                                            <td data-etiqueta="Duración">
                                                                <%= String.format(ES, "%.2f" , l.getDuracionSeg()) %> s
                                                            </td>
                                                            <td data-etiqueta="Acción">
                                                                <form method="post" action="eliminar"
                                                                    class="js-eliminar"
                                                                    data-titulo="<%= Html.esc(l.getTitulo()) %>">
                                                                    <input type="hidden" name="codigo"
                                                                        value="<%= Html.esc(l.getCodigo()) %>">
                                                                    <button type="submit"
                                                                        class="boton boton--peligro boton--chico"
                                                                        aria-label="Eliminar <%= Html.esc(l.getTitulo()) %>">Eliminar</button>
                                                                </form>
                                                            </td>
                                                </tr>
                                                <% } %>
                                        </tbody>
                                    </table>
                                </div>
                                <% } %>

                                    <% if (promedios !=null && !promedios.isEmpty()) { %>
                                        <section class="seccion" aria-labelledby="titulo-promedios">
                                            <h2 id="titulo-promedios">Promedio de indignación por categoría</h2>
                                            <div class="promedios">
                                                <% for (Map.Entry<?, ?> e : promedios.entrySet()) {
                                                    double valor = ((Number) e.getValue()).doubleValue();
                                                    %>
                                                    <article class="tarjeta">
                                                        <p class="promedio__categoria">
                                                            <%= Html.esc(String.valueOf(e.getKey())) %>
                                                        </p>
                                                        <p class="promedio__valor">
                                                            <%= String.format(ES, "%.1f" , valor) %> <small>/ 10</small>
                                                        </p>
                                                        <div class="medidor medidor--<%= clasificar(valor) %>"
                                                            role="img" aria-label="Promedio <%= String.format(ES, "
                                                            %.1f", valor) %> de 10">
                                                            <span class="medidor__pista"><span class="medidor__barra"
                                                                    style="--nivel:<%= Math.round(valor * 10) %>%"></span></span>
                                                        </div>
                                                    </article>
                                                    <% } %>
                                            </div>
                                        </section>
                                        <% } %>
                    </main>

                    <footer>Proyecto final · Estructuras de Datos I</footer>

                    <script>
                        // Pide confirmación antes de eliminar (el formulario POST sigue funcionando sin JavaScript).
                        document.querySelectorAll('form.js-eliminar').forEach(function (form) {
                            form.addEventListener('submit', function (evento) {
                                if (!confirm('¿Eliminar el lamento "' + form.dataset.titulo + '"?')) {
                                    evento.preventDefault();
                                }
                            });
                        });
                    </script>
                </body>

                </html>