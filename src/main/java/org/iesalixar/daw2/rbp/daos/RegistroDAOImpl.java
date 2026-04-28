package org.iesalixar.daw2.rbp.daos;

import org.iesalixar.daw2.rbp.entities.Registro;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación DAO para la entidad {@link Registro}.
 * <p>
 * Gestiona las operaciones CRUD sobre la tabla registro_glucosa.
 */
public class RegistroDAOImpl implements RegistroDAO {

    /**
     * Obtiene todos los registros de glucosa ordenados por fecha descendente.
     *
     * @return lista de registros
     * @throws SQLException si ocurre un error en la consulta SQL
     */
    @Override
    public List<Registro> listAllRegistros() throws SQLException {
        List<Registro> registros = new ArrayList<>();
        String query = "SELECT * FROM registro_glucosa ORDER BY fecha DESC";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                registros.add(new Registro(
                        resultSet.getLong("id"),
                        resultSet.getInt("valor"),
                        resultSet.getObject("fecha", LocalDateTime.class),
                        (Long) resultSet.getObject("clasificacion_id")
                ));
            }
        }
        return registros;
    }

    /**
     * Inserta un nuevo registro de glucosa en la base de datos.
     *
     * @param registro objeto {@link Registro} a insertar
     * @throws SQLException si ocurre un error en la inserción
     */
    @Override
    public void insertRegistro(Registro registro) throws SQLException {
        String query = "INSERT INTO registro_glucosa (id, valor, fecha, clasificacion_id) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, registro.getId());
            preparedStatement.setInt(2, registro.getValor());
            preparedStatement.setObject(3, registro.getFecha());

            if (registro.getClasificacionId() != null) {
                preparedStatement.setLong(4, registro.getClasificacionId());
            } else {
                preparedStatement.setNull(4, Types.BIGINT);
            }

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Actualiza un registro existente en la base de datos.
     *
     * @param registro objeto {@link Registro} con los datos actualizados
     * @throws SQLException si ocurre un error en la actualización
     */
    @Override
    public void updateRegistro(Registro registro) throws SQLException {
        String query = "UPDATE registro_glucosa SET valor=?, fecha=?, clasificacion_id=? WHERE id=?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, registro.getValor());
            preparedStatement.setObject(2, registro.getFecha());

            if (registro.getClasificacionId() != null) {
                preparedStatement.setLong(3, registro.getClasificacionId());
            } else {
                preparedStatement.setNull(3, Types.BIGINT);
            }

            preparedStatement.setLong(4, registro.getId());
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Obtiene un registro por su identificador.
     *
     * @param id identificador del registro
     * @return objeto {@link Registro} o {@code null} si no existe
     * @throws SQLException si ocurre un error en la consulta
     */
    @Override
    public Registro getRegistroById(Long id) throws SQLException {
        String query = "SELECT * FROM registro_glucosa WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Registro(
                            resultSet.getLong("id"),
                            resultSet.getInt("valor"),
                            resultSet.getObject("fecha", LocalDateTime.class),
                            (Long) resultSet.getObject("clasificacion_id")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Elimina un registro por su identificador.
     *
     * @param id identificador del registro a eliminar
     * @throws SQLException si ocurre un error en la eliminación
     */
    @Override
    public void deleteRegistro(Long id) throws SQLException {
        String query = "DELETE FROM registro_glucosa WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Comprueba si existe un registro con el identificador indicado.
     *
     * @param id identificador del registro
     * @return {@code true} si existe, {@code false} en caso contrario
     * @throws SQLException si ocurre un error en la consulta SQL
     */
    @Override
    public boolean existsRegistroById(Long id) throws SQLException {
        String query = "SELECT COUNT(*) FROM registro_glucosa WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}