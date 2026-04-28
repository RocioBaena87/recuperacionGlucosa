package org.iesalixar.daw2.rbp.daos;

import org.iesalixar.daw2.rbp.entities.Clasificacion;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de la entidad {@link Clasificacion}.
 * <p>
 * Define las operaciones de acceso a datos relacionadas con clasificaciones,
 * incluyendo operaciones CRUD y validaciones.
 */
public interface ClasificacionDAO {

    /**
     * Obtiene todas las clasificaciones almacenadas en la base de datos.
     *
     * @return lista de clasificaciones
     * @throws SQLException si ocurre un error durante la consulta
     */
    List<Clasificacion> listAllClasificaciones() throws SQLException;

    /**
     * Inserta una nueva clasificación en la base de datos.
     *
     * @param clasificacion objeto clasificación a insertar
     * @throws SQLException si ocurre un error durante la inserción
     */
    void insertClasificacion(Clasificacion clasificacion) throws SQLException;

    /**
     * Actualiza una clasificación existente.
     *
     * @param clasificacion objeto clasificación con datos actualizados
     * @throws SQLException si ocurre un error durante la actualización
     */
    void updateClasificacion(Clasificacion clasificacion) throws SQLException;

    /**
     * Obtiene una clasificación por su identificador.
     *
     * @param id identificador de la clasificación
     * @return clasificación encontrada o null si no existe
     * @throws SQLException si ocurre un error durante la consulta
     */
    Clasificacion getClasificacionById(Long id) throws SQLException;

    /**
     * Elimina una clasificación por su identificador.
     *
     * @param id identificador de la clasificación
     * @throws SQLException si ocurre un error durante la eliminación
     */
    void deleteClasificacion(Long id) throws SQLException;

    /**
     * Verifica si existe una clasificación con un nombre de rango específico.
     *
     * @param nombreRango nombre del rango a comprobar
     * @return true si existe, false en caso contrario
     * @throws SQLException si ocurre un error durante la consulta
     */
    boolean existsClasificacionByNombreRango(String nombreRango) throws SQLException;

    /**
     * Verifica si existe una clasificación con un nombre de rango específico
     * excluyendo un identificador determinado.
     *
     * @param nombreRango nombre del rango a comprobar
     * @param id identificador a excluir
     * @return true si existe otra clasificación con ese nombre, false en caso contrario
     * @throws SQLException si ocurre un error durante la consulta
     */
    boolean existsClasificacionByNombreRangoAndNotId(String nombreRango, Long id) throws SQLException;
}