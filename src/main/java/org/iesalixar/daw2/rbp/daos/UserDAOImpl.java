package org.iesalixar.daw2.rbp.daos;

import org.iesalixar.daw2.rbp.entities.User;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementación de la interfaz {@link UserDAO} para la gestión de usuarios.
 * <p>
 * Proporciona operaciones CRUD y validaciones sobre la tabla {@code users}
 * utilizando JDBC.
 */
public class UserDAOImpl implements UserDAO {

    /**
     * Logger para el seguimiento de operaciones y errores.
     */
    private static final Logger logger = Logger.getLogger(UserDAOImpl.class.getName());

    /**
     * Obtiene todos los usuarios almacenados en la base de datos.
     *
     * @return lista de usuarios
     * @throws SQLException si ocurre un error durante la consulta
     */
    @Override
    public List<User> listAllUsers() throws SQLException {
        logger.info("Entrando en listAllUsers");
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");
                String passwordHash = resultSet.getString("password_hash");
                boolean active = resultSet.getBoolean("active");
                boolean accountNonLocked = resultSet.getBoolean("account_non_locked");
                LocalDateTime lastPasswordChange = resultSet.getObject("last_password_change", LocalDateTime.class);
                LocalDateTime passwordExpiresAt = resultSet.getObject("password_expires_at", LocalDateTime.class);
                int failedLoginAttempts = resultSet.getInt("failed_login_attempts");
                boolean emailVerified = resultSet.getBoolean("email_verified");
                boolean mustChangePassword = resultSet.getBoolean("must_change_password");

                users.add(new User(id, username, passwordHash, active, accountNonLocked,
                        lastPasswordChange, passwordExpiresAt, failedLoginAttempts,
                        emailVerified, mustChangePassword));
            }
            logger.info("listAllUsers completado. Total usuarios: " + users.size());
        } catch (SQLException e) {
            logger.severe("Error en listAllUsers: " + e.getMessage());
            throw e;
        }
        return users;
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param user usuario a insertar
     * @throws SQLException si ocurre un error durante la inserción
     */
    @Override
    public void insertUser(User user) throws SQLException {
        logger.info("Entrando en insertUser para username: " + user.getUsername());
        String query = "INSERT INTO users (username, password_hash, active, account_non_locked, last_password_change, " +
                "password_expires_at, failed_login_attempts, email_verified, must_change_password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setBoolean(3, user.isActive());
            preparedStatement.setBoolean(4, user.isAccountNonLocked());
            preparedStatement.setObject(5, user.getLastPasswordChange());
            preparedStatement.setObject(6, user.getPasswordExpiresAt());
            preparedStatement.setInt(7, user.getFailedLoginAttempts());
            preparedStatement.setBoolean(8, user.isEmailVerified());
            preparedStatement.setBoolean(9, user.isMustChangePassword());

            preparedStatement.executeUpdate();
            logger.info("insertUser completado para username: " + user.getUsername());

        } catch (SQLException e) {
            logger.severe("Error en insertUser: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica si existe un usuario con un nombre de usuario dado.
     *
     * @param username nombre de usuario a comprobar
     * @return true si existe, false en caso contrario
     * @throws SQLException si ocurre un error durante la consulta
     */
    @Override
    public boolean existsUserByUsername(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE UPPER(username) = ?";
        boolean exists;
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username.toUpperCase());
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                exists = resultSet.getInt(1) > 0;
            }
        }
        return exists;
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param user usuario con la información actualizada
     * @throws SQLException si ocurre un error durante la actualización
     */
    @Override
    public void updateUser(User user) throws SQLException {
        logger.info("Entrando en updateUser para ID: " + user.getId());
        String query = "UPDATE users SET username=?, password_hash=?, active=?, account_non_locked=?, last_password_change=?, " +
                "password_expires_at=?, failed_login_attempts=?, email_verified=?, must_change_password=? WHERE id=?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setBoolean(3, user.isActive());
            preparedStatement.setBoolean(4, user.isAccountNonLocked());
            preparedStatement.setObject(5, user.getLastPasswordChange());
            preparedStatement.setObject(6, user.getPasswordExpiresAt());
            preparedStatement.setInt(7, user.getFailedLoginAttempts());
            preparedStatement.setBoolean(8, user.isEmailVerified());
            preparedStatement.setBoolean(9, user.isMustChangePassword());
            preparedStatement.setLong(10, user.getId());

            preparedStatement.executeUpdate();
            logger.info("updateUser completado para ID: " + user.getId());

        } catch (SQLException e) {
            logger.severe("Error en updateUser: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o null si no existe
     * @throws SQLException si ocurre un error durante la consulta
     */
    @Override
    public User getUserById(Long id) throws SQLException {
        logger.info("Entrando en getUserById para ID: " + id);
        String query = "SELECT * FROM users WHERE id = ?";
        User user = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String username = resultSet.getString("username");
                    String passwordHash = resultSet.getString("password_hash");
                    boolean active = resultSet.getBoolean("active");
                    boolean accountNonLocked = resultSet.getBoolean("account_non_locked");
                    LocalDateTime lastPasswordChange = resultSet.getObject("last_password_change", LocalDateTime.class);
                    LocalDateTime passwordExpiresAt = resultSet.getObject("password_expires_at", LocalDateTime.class);
                    int failedLoginAttempts = resultSet.getInt("failed_login_attempts");
                    boolean emailVerified = resultSet.getBoolean("email_verified");
                    boolean mustChangePassword = resultSet.getBoolean("must_change_password");

                    user = new User(id, username, passwordHash, active, accountNonLocked,
                            lastPasswordChange, passwordExpiresAt, failedLoginAttempts,
                            emailVerified, mustChangePassword);
                }
            }
        }
        return user;
    }

    /**
     * Elimina un usuario por su identificador.
     *
     * @param id identificador del usuario
     * @throws SQLException si ocurre un error durante la eliminación
     */
    @Override
    public void deleteUser(Long id) throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Verifica si existe un usuario con un nombre de usuario dado excluyendo un ID específico.
     *
     * @param username nombre de usuario a comprobar
     * @param id identificador a excluir
     * @return true si existe otro usuario con ese nombre, false en caso contrario
     * @throws SQLException si ocurre un error durante la consulta
     */
    @Override
    public boolean existsUserByUsernameAndNotId(String username, Long id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE UPPER(username) = ? AND id != ?";
        boolean exists;
        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username.toUpperCase());
            statement.setLong(2, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                exists = resultSet.getInt(1) > 0;
            }
        }
        return exists;
    }
}