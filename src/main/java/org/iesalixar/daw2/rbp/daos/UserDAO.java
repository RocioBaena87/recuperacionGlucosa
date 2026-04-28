package org.iesalixar.daw2.rbp.daos;

import org.iesalixar.daw2.rbp.entities.User;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de usuarios en la base de datos.
 * <p>
 * Define las operaciones CRUD básicas y validaciones relacionadas con la entidad {@link User}.
 */
public interface UserDAO {

    /**
     * Obtiene una lista con todos los usuarios almacenados.
     *
     * @return lista de usuarios
     * @throws SQLException si ocurre un error al acceder a la base de datos
     */
    List<User> listAllUsers() throws SQLException;

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param user usuario a insertar
     * @throws SQLException si ocurre un error durante la inserción
     */
    void insertUser(User user) throws SQLException;

    /**
     * Comprueba si existe un usuario con un nombre de usuario específico.
     *
     * @param username nombre de usuario a comprobar
     * @return true si existe, false en caso contrario
     * @throws SQLException si ocurre un error en la consulta
     */
    boolean existsUserByUsername(String username) throws SQLException;

    /**
     * Actualiza la información de un usuario existente.
     *
     * @param user usuario con los datos actualizados
     * @throws SQLException si ocurre un error durante la actualización
     */
    void updateUser(User user) throws SQLException;

    /**
     * Obtiene un usuario a partir de su identificador.
     *
     * @param id identificador del usuario
     * @return usuario encontrado o null si no existe
     * @throws SQLException si ocurre un error en la consulta
     */
    User getUserById(Long id) throws SQLException;

    /**
     * Comprueba si existe un nombre de usuario excluyendo un ID concreto.
     * <p>
     * Útil para validaciones durante actualizaciones.
     *
     * @param username nombre de usuario a comprobar
     * @param id identificador del usuario a excluir
     * @return true si existe otro usuario con ese nombre, false en caso contrario
     * @throws SQLException si ocurre un error en la consulta
     */
    boolean existsUserByUsernameAndNotId(String username, Long id) throws SQLException;

    /**
     * Elimina un usuario de la base de datos por su identificador.
     *
     * @param id identificador del usuario a eliminar
     * @throws SQLException si ocurre un error durante la eliminación
     */
    void deleteUser(Long id) throws SQLException;
}
