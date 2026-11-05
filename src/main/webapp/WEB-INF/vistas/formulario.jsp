<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
    <%@ page
        import="java.util.ArrayList, java.util.Collections, java.util.List, java.util.Set, co.umariana.lamento.web.Html"
        %>
        <%! /** Devuelve el parámetro ya "escapado" (para volver a mostrar lo escrito si hubo un error). */ private
            static String valor(javax.servlet.http.HttpServletRequest req, String nombre) { String
            v=req.getParameter(nombre); return (v==null) ? "" : Html.esc(v); } %>
            <% // Atributo que pone el servlet RegistrarLamento cuando algo falla Object
                atributoError=request.getAttribute("error"); String error=(atributoError==null) ? null :
                atributoError.toString(); // Audios que existen en la carpeta media/ List<String> archivos = new
                ArrayList<>();
                    Set<String> rutas = application.getResourcePaths("/media/");
                        if (rutas != null) {
                        for (String ruta : rutas) {
                        String nombre = ruta.substring(ruta.lastIndexOf('/') + 1);
                        if (nombre.matches("(?i).+\\.(mp3|ogg|wav|m4a)")) {
                        archivos.add(nombre);
                        }
                        }
                        }
                        Collections.sort(archivos);

                        String archivoElegido = request.getParameter("archivo");
                        String indignacion = request.getParameter("indignacion");
                        if (indignacion == null || !indignacion.matches("([1-9]|10)")) {
                        indignacion = "5";
                        }
                        %>
                        <!DOCTYPE html>
                        <html lang="es">

                        <head>
                            <meta charset="UTF-8">
                            <meta name="viewport" content="width=device-width, initial-scale=1">
                            <title>Registrar lamento · Lamentómetro</title>
                            <link rel="stylesheet" href="css/estilos.css">
                        </head>

                        <body class="app">
                            <header class="sitio__cabecera">
                                <div class="contenedor sitio__barra">
                                    <a class="marca" href="index.html"><span aria-hidden="true">🎙️</span>
                                        Lamentómetro</a>
                                    <nav class="menu" aria-label="Principal">
                                        <a href="index.html">Inicio</a>
                                        <a href="catalogo">Catálogo</a>
                                        <a href="listar">Lamentos</a>
                                        <a href="registrar" aria-current="page">Registrar</a>
                                        <a href="reproductor">Reproductor</a>
                                    </nav>
                                </div>
                            </header>

                            <main>
                                <div class="encabezado">
                                    <div>
                                        <h1>Registrar un lamento</h1>
                                        <p>Complete los datos del audio. Los campos con * son obligatorios.</p>
                                    </div>
                                </div>

                                <% if (error !=null) { %>
                                    <div class="alerta alerta--error" role="alert">⚠ <%= Html.esc(error) %>
                                    </div>
                                    <% } %>

                                        <form class="formulario tarjeta" method="post" action="registrar">
                                            <div class="fila">
                                                <div class="campo">
                                                    <label for="codigo">Código *</label>
                                                    <input type="text" id="codigo" name="codigo" required maxlength="20"
                                                        pattern="[^;]+" placeholder="Ej. L005"
                                                        value="<%= valor(request, " codigo") %>" autocomplete="off">
                                                    <small>Único para cada lamento.</small>
                                                </div>
                                                <div class="campo">
                                                    <label for="categoria">Categoría *</label>
                                                    <input type="text" id="categoria" name="categoria" required
                                                        maxlength="40" pattern="[^;]+" list="categorias"
                                                        placeholder="Ej. Insulto" value="<%= valor(request, "
                                                        categoria") %>">
                                                    <datalist id="categorias">
                                                        <option value="Insulto"></option>
                                                        <option value="Queja"></option>
                                                        <option value="Tráfico"></option>
                                                        <option value="Política"></option>
                                                    </datalist>
                                                </div>
                                            </div>

                                            <div class="campo">
                                                <label for="titulo">Título *</label>
                                                <input type="text" id="titulo" name="titulo" required maxlength="80"
                                                    pattern="[^;]+" placeholder="Ej. Jueputa nivel 5"
                                                    value="<%= valor(request, " titulo") %>">
                                            </div>

                                            <div class="campo">
                                                <label for="archivo">Archivo de audio *</label>
                                                <% if (archivos.isEmpty()) { %>
                                                    <input type="text" id="archivo" name="archivo" required
                                                        placeholder="Ej. hp_1.mp3" value="<%= valor(request, " archivo")
                                                        %>">
                                                    <small>Nombre de un archivo de la carpeta
                                                        <code>media/</code>.</small>
                                                    <% } else { %>
                                                        <select id="archivo" name="archivo" required>
                                                            <option value="">Elija un audio…</option>
                                                            <% for (String a : archivos) { %>
                                                                <option value="<%= Html.esc(a) %>"
                                                                    <%=a.equals(archivoElegido) ? " selected" : "" %>>
                                                                    <%= Html.esc(a) %>
                                                                </option>
                                                                <% } %>
                                                        </select>
                                                        <audio id="vista" controls preload="metadata" hidden
                                                            aria-label="Vista previa del audio"></audio>
                                                        <% } %>
                                            </div>

                                            <div class="fila">
                                                <div class="campo">
                                                    <div class="indignacion">
                                                        <label for="indignacion">Indignación *</label>
                                                        <span><output id="indignacion-valor" for="indignacion">
                                                                <%= indignacion %>/10
                                                            </output>
                                                            <span class="indignacion__texto"
                                                                id="indignacion-texto"></span></span>
                                                    </div>
                                                    <input type="range" id="indignacion" name="indignacion" min="1"
                                                        max="10" step="1" value="<%= indignacion %>">
                                                </div>
                                                <div class="campo">
                                                    <label for="duracion">Duración (segundos) *</label>
                                                    <input type="number" id="duracion" name="duracion" required
                                                        min="0.01" step="0.01" inputmode="decimal"
                                                        placeholder="Ej. 1.32" value="<%= valor(request, " duracion")
                                                        %>">
                                                    <small>Se completa sola al elegir el audio.</small>
                                                </div>
                                            </div>

                                            <div class="campo">
                                                <label for="descripcion">Descripción</label>
                                                <textarea id="descripcion" name="descripcion" maxlength="300"
                                                    placeholder="Cuente en qué situación se usa este lamento (opcional)"><%= valor(request, "descripcion") %></textarea>
                                                <small>Sin punto y coma (;) ni saltos de línea.</small>
                                            </div>

                                            <div class="botones">
                                                <button type="submit" class="boton">Guardar lamento</button>
                                                <a class="boton boton--secundario" href="listar">Cancelar</a>
                                            </div>
                                        </form>
                            </main>

                            <footer>Proyecto final · Estructuras de Datos I</footer>

                            <script>
                                (function () {
                                    // 1) Indignación: muestra el valor y una frase mientras se mueve el control
                                    var rango = document.getElementById('indignacion');
                                    var salida = document.getElementById('indignacion-valor');
                                    var texto = document.getElementById('indignacion-texto');
                                    function pintar() {
                                        var n = parseInt(rango.value, 10);
                                        salida.textContent = n + '/10';
                                        texto.textContent = n >= 8 ? '¡Furioso!' : (n >= 4 ? 'Molesto' : 'Tranquilo');
                                    }
                                    rango.addEventListener('input', pintar);
                                    pintar();

                                    // 2) Audio: vista previa y duración automática
                                    var selector = document.getElementById('archivo');
                                    var vista = document.getElementById('vista');
                                    var duracion = document.getElementById('duracion');
                                    if (!vista || selector.tagName !== 'SELECT') return;

                                    duracion.addEventListener('input', function () { duracion.dataset.auto = '0'; });
                                    vista.addEventListener('loadedmetadata', function () {
                                        if (isFinite(vista.duration) && (!duracion.value || duracion.dataset.auto === '1')) {
                                            duracion.value = (Math.round(vista.duration * 100) / 100).toString();
                                            duracion.dataset.auto = '1';
                                        }
                                    });
                                    function mostrarVista() {
                                        if (!selector.value) { vista.hidden = true; vista.removeAttribute('src'); return; }
                                        vista.src = 'media/' + encodeURIComponent(selector.value);
                                        vista.hidden = false;
                                    }
                                    selector.addEventListener('change', mostrarVista);
                                    mostrarVista();
                                })();
                            </script>
                        </body>

                        </html>