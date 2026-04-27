package org.iesalixar.daw2.rbp.daos;

import io.github.cdimascio.dotenv.Dotenv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión a la base de datos utilizando el patrón Singleton.
 * <p>
 * Esta clase se encarga de:
 * <ul>
 *     <li>Inicializar una única conexión a la base de datos</li>
 *     <li>Reutilizar la conexión existente</li>
 *     <li>Cerrar la conexión cuando sea necesario</li>
 * </ul>
 * <p>
 * La configuración de la conexión se obtiene desde variables de entorno
 * definidas en un archivo .env.
 */
public class DatabaseConnectionManager {

    /**
     * Instancia única de la conexión a la base de datos.
     */
    private static Connection connection = null;

    /**
     * Logger para registrar eventos y errores.
     */
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);

    /**
     * Cargador de variables de entorno desde el archivo .env.
     */
    private static Dotenv dotenv = Dotenv.load();

    /**
     * Constructor privado para evitar la instanciación de la clase.
     */
    private DatabaseConnectionManager() {}

    /**
     * Obtiene la conexión a la base de datos.
     * <p>
     * Si la conexión no existe o está cerrada, se crea una nueva.
     *
     * @return conexión activa a la base de datos
     * @throws RuntimeException si ocurre un error al establecer la conexión
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                logger.info("Iniciando la conexión a la base de datos MariaDB...");

                String dbUrl = dotenv.get("DB_URL");
                String dbUser = dotenv.get("DB_USER");
                String dbPassword = dotenv.get("DB_PASSWORD");

                connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                logger.info("Conexión a la base de datos establecida exitosamente");
            }
        } catch (SQLException e) {
            logger.error("Error al conectar con la base de datos: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo conectar a la base de datos.", e);
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos si está abierta.
     * <p>
     * En caso de error durante el cierre, se registra en el logger.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                logger.info("Cerrando la conexión a la base de datos...");
                connection.close();
                logger.info("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                logger.error("Error al cerrar la conexión a la base de datos: {}", e.getMessage(), e);
            }
        }
    }
}