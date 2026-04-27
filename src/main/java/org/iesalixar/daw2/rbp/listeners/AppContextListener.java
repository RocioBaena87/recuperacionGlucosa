package org.iesalixar.daw2.rbp.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.iesalixar.daw2.rbp.daos.DataInitializer;
import org.iesalixar.daw2.rbp.daos.DatabaseConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.Connection;

/**
 * Listener del contexto de la aplicación web.
 * <p>
 * Se ejecuta automáticamente al iniciar y detener la aplicación,
 * permitiendo realizar tareas de inicialización y liberación de recursos.
 */
@WebListener
public class AppContextListener implements ServletContextListener {

    /**
     * Logger para registrar eventos del ciclo de vida de la aplicación.
     */
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    /**
     * Método invocado al iniciar el contexto de la aplicación.
     * <p>
     * Establece la conexión a la base de datos y carga datos iniciales
     * desde un archivo SQL ubicado en el classpath.
     *
     * @param sce evento del contexto del servlet
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Inicializando la aplicacion y conectando a la base de datos...");

        try (Connection connection = DatabaseConnectionManager.getConnection()) {

            InputStream sqlFileStream = sce.getServletContext().getResourceAsStream("/WEB-INF/classes/data.sql");

            if (sqlFileStream == null) {
                logger.error("No se pudo encontrar el archivo data.sql en /WEB-INF/classes/");
                return;
            }

            DataInitializer.loadDataFromSQL(sqlFileStream);
            logger.info("Carga de datos finalizada");

        } catch (Exception e) {
            logger.error("Error al inicializar la aplicacion y cargar los datos: {}", e.getMessage(), e);
        }
    }

    /**
     * Método invocado al destruir el contexto de la aplicación.
     * <p>
     * Se encarga de cerrar la conexión a la base de datos.
     *
     * @param sce evento del contexto del servlet
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Cerrando la conexión a la base de datos al apagar la aplicación...");
        DatabaseConnectionManager.closeConnection();
    }
}