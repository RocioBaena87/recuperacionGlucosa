package org.iesalixar.daw2.rbp.daos;

import org.iesalixar.daw2.rbp.entities.Registro;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la entidad {@link Registro}.
 * <p>
 * Define las operaciones básicas de acceso a datos para la tabla de registros de glucosa.
 */
public interface RegistroDAO {

    /**
     * Obtiene todos los registros almacenados en la base de datos.
     *
     * @return lista de registros
     * @throws SQLException si ocurre un error en la consulta SQL
     */
    List<Registro> listAllRegistros() throws SQLException;

    /**
     * Inserta un nuevo registro en la base de datos.
     *
     * @param registro objeto {@link Registro} a insertar
     * @throws SQLException si ocurre un error en la inserción
     */
    void insertRegistro(Registro registro) throws SQLException;

    /**
     * Actualiza un registro existente en la base de datos.
     *
     * @param registro objeto {@link Registro} con los datos actualizados
     * @throws SQLException si ocurre un error en la actualización
     */
    void updateRegistro(Registro registro) throws SQLException;

    /**
     * Elimina un registro por su identificador.
     *
     * @param id identificador del registro a eliminar
     * @throws SQLException si ocurre un error en la eliminación
     */
    void deleteRegistro(Long id) throws SQLException;

    /**
     * Obtiene un registro por su identificador.
     *
     * @param id identificador del registro
     * @return registro encontrado o {@code null} si no existe
     * @throws SQLException si ocurre un error en la consulta
     */
    Registro getRegistroById(Long id) throws SQLException;

    /**
     * Comprueba si existe un registro con el identificador indicado.
     *
     * @param id identificador del registro
     * @return {@code true} si existe un registro con ese ID, {@code false} en caso contrario
     * @throws SQLException si ocurre un error en el acceso a la base de datos
     */
    boolean existsRegistroById(Long id) throws SQLException;
}