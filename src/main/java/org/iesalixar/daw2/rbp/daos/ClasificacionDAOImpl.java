package org.iesalixar.daw2.rbp.daos;

import org.iesalixar.daw2.rbp.entities.Clasificacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para la entidad {@link Clasificacion}.
 * <p>
 * Gestiona las operaciones CRUD y consultas auxiliares sobre la tabla
 * clasificacion_glucosa.
 */
public class ClasificacionDAOImpl implements ClasificacionDAO {

    /**
     * Obtiene todas las clasificaciones almacenadas en la base de datos.
     *
     * @return lista de clasificaciones
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public List<Clasificacion> listAllClasificaciones() throws SQLException {
        List<Clasificacion> clasificaciones = new ArrayList<>();
        String query = "SELECT * FROM clasificacion_glucosa";

        try (Connection connection = DatabaseConnectionManager.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String nombreRango = resultSet.getString("nombre_rango");
                int valorMin = resultSet.getInt("valor_min");
                int valorMax = resultSet.getInt("valor_max");
                String descripcion = resultSet.getString("descripcion");

                clasificaciones.add(new Clasificacion(id, nombreRango, valorMin, valorMax, descripcion));
            }
        }
        return clasificaciones;
    }

    /**
     * Inserta una nueva clasificación en la base de datos.
     *
     * @param clasificacion objeto a insertar
     * @throws SQLException si ocurre un error en la inserción
     */
    @Override
    public void insertClasificacion(Clasificacion clasificacion) throws SQLException {
        String query = "INSERT INTO clasificacion_glucosa (nombre_rango, valor_min, valor_max, descripcion) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, clasificacion.getNombreRango());
            preparedStatement.setInt(2, clasificacion.getValorMin());
            preparedStatement.setInt(3, clasificacion.getValorMax());
            preparedStatement.setString(4, clasificacion.getDescripcion());

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Actualiza una clasificación existente.
     *
     * @param clasificacion objeto con datos actualizados
     * @throws SQLException si ocurre un error en la actualización
     */
    @Override
    public void updateClasificacion(Clasificacion clasificacion) throws SQLException {
        String query = "UPDATE clasificacion_glucosa SET nombre_rango=?, valor_min=?, valor_max=?, descripcion=? WHERE id=?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, clasificacion.getNombreRango());
            preparedStatement.setInt(2, clasificacion.getValorMin());
            preparedStatement.setInt(3, clasificacion.getValorMax());
            preparedStatement.setString(4, clasificacion.getDescripcion());
            preparedStatement.setLong(5, clasificacion.getId());

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Obtiene una clasificación por su identificador.
     *
     * @param id identificador de la clasificación
     * @return clasificación encontrada o null si no existe
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public Clasificacion getClasificacionById(Long id) throws SQLException {
        String query = "SELECT * FROM clasificacion_glucosa WHERE id = ?";
        Clasificacion clasificacion = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                clasificacion = new Clasificacion(
                        resultSet.getLong("id"),
                        resultSet.getString("nombre_rango"),
                        resultSet.getInt("valor_min"),
                        resultSet.getInt("valor_max"),
                        resultSet.getString("descripcion")
                );
            }
        }
        return clasificacion;
    }

    /**
     * Elimina una clasificación por su identificador.
     *
     * @param id identificador de la clasificación
     * @throws SQLException si ocurre un error en la eliminación
     */
    @Override
    public void deleteClasificacion(Long id) throws SQLException {
        String query = "DELETE FROM clasificacion_glucosa WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Comprueba si existe una clasificación con un nombre de rango específico.
     *
     * @param nombreRango nombre del rango
     * @return true si existe, false en caso contrario
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public boolean existsClasificacionByNombreRango(String nombreRango) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clasificacion_glucosa WHERE UPPER(nombre_rango) = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nombreRango.toUpperCase());

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }

    /**
     * Comprueba si existe otra clasificación con el mismo nombre de rango
     * excluyendo un identificador concreto.
     *
     * @param nombreRango nombre del rango
     * @param id identificador a excluir
     * @return true si existe otra clasificación con ese nombre
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public boolean existsClasificacionByNombreRangoAndNotId(String nombreRango, Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM clasificacion_glucosa WHERE UPPER(nombre_rango) = ? AND id != ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, nombreRango.toUpperCase());
            preparedStatement.setLong(2, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                return resultSet.getInt(1) > 0;
            }
        }
    }
}