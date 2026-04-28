package org.iesalixar.daw2.rbp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet encargado de gestionar el cambio de idioma de la aplicación.
 * <p>
 * Permite al usuario seleccionar un idioma mediante un parámetro en la URL
 * y lo almacena en la sesión para futuras peticiones.
 */
@WebServlet("/changeLanguage")
public class LanguageServlet extends HttpServlet {

    /**
     * Logger para registrar eventos y errores del servlet.
     */
    private static final Logger logger = Logger.getLogger(LanguageServlet.class.getName());

    /**
     * Maneja las peticiones GET para cambiar el idioma del usuario.
     * <p>
     * Lee el parámetro {@code lang}, establece el {@link Locale} correspondiente
     * en la sesión y redirige al usuario a la página anterior.
     *
     * @param request objeto {@link HttpServletRequest}
     * @param response objeto {@link HttpServletResponse}
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("Entrando en el método doGet del servlet LanguageServlet");

        try {
            String language = request.getParameter("lang");

            if (language != null){
                Locale locale;

                if (language.equals("es")) {
                    locale = new Locale("es");
                    logger.info("Idioma seleccionado: Español (es)");
                } else {
                    locale = new Locale("en");
                    logger.info("Idioma seleccionado: Inglés (en)");
                }

                request.getSession().setAttribute("locale", locale);
                logger.info("El locale '" + locale + "' ha sido guardado en la sesión");
            } else {
                logger.warning("No se ha recibido ningún parámetro 'lang'");
            }

            String referer = request.getHeader("Referer");
            if (referer != null) {
                logger.info("Redirigiendo al usuario a la página anterior: " + referer);
                response.sendRedirect(referer);
            } else {
                logger.warning("No se encontró la cabecera Referer, redirigiendo a la página principal.");
                response.sendRedirect(request.getContextPath()+"/");
            }

        } catch (Exception e){
            logger.log(Level.SEVERE, "Se ha producido un error en el método doGet de LanguageServlet", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cambiar el idioma");
        }

        logger.info("Saliendo del método doGet del servlet LanguageServlet");
    }
}