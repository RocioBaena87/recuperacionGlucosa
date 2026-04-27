package org.iesalixar.daw2.rbp.daos;

import jakarta.servlet.ServletContextEvent;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Clase utilitaria encargada de cargar e inicializar datos en la base de datos
 * a partir de un archivo SQL proporcionado como {@link InputStream}.
 * <p>
 * Permite ejecutar múltiples sentencias SQL dentro de una única transacción.
 */
public class DataInitializer {

    /**
     * Logger para registrar la ejecución del proceso de carga de datos.
     */
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Lee un archivo SQL y ejecuta sus sentencias en la base de datos.
     * <p>
     * El archivo se procesa completamente, dividiendo las sentencias por ';'
     * y ejecutándolas dentro de una transacción.
     *
     * @param sqlFileStream flujo de entrada del archivo SQL
     * @throws SQLException si ocurre un error durante la ejecución de las sentencias
     * @throws IOException si ocurre un error al leer el archivo
     */
    public static void loadDataFromSQL(InputStream sqlFileStream) throws SQLException, IOException {
        logger.info("Entrando en el método loadDataFromSQL");

        if (sqlFileStream == null) {
            logger.error("El archivo SQL no se ha proporcionado o es nulo");
            throw new IOException("El archivo SQL es nulo o no se ha encontrado");
        }

        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            logger.info("Conexión a la base de datos establecida");

            String sql;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(sqlFileStream))) {
                sql = reader.lines().collect(Collectors.joining("\n"));
            } catch (IOException e) {
                logger.error("Error al leer el archivo SQL: {}", e.getMessage(), e);
                throw new IOException("Error al leer el archivo SQL", e);
            }

            String[] statements = sql.split(";");

            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                for (String sqlStatement : statements) {
                    if (!sqlStatement.trim().isEmpty()) {
                        logger.info("Ejecutando la sentencia SQL: {}", sqlStatement);
                        statement.execute(sqlStatement.trim());
                    }
                }
                connection.commit();
                logger.info("Datos cargados exitosamente desde el archivo SQL");
            } catch (SQLException e) {
                connection.rollback();
                logger.error("Error al ejecutar el archivo SQL, se ha revertido la transacción: {}", e.getMessage(), e);
                throw new SQLException("Error al ejecutar el archivo Sql, transacción revertida", e);
            }
        } catch (SQLException e) {
            logger.error("Error durante la conexión a la base de datos o la ejecución SQL: {}", e.getMessage(), e);
            throw e;
        }

        logger.info("Saliendo del método loadDataFromSQL");
    }
}