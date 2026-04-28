package org.iesalixar.daw2.rbp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una clasificación de rangos (por ejemplo, niveles de glucosa).
 *
 * Contiene información sobre:
 * - Identificador único.
 * - Nombre del rango.
 * - Valores mínimo y máximo del rango.
 * - Descripción asociada.
 *
 * Se utiliza para definir y gestionar distintos rangos de valores en la aplicación.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Clasificacion {

    /**
     * Identificador único de la clasificación.
     */
    private Long id;

    /**
     * Nombre del rango (ej: "Normal", "Alto", "Bajo").
     */
    private String nombreRango;

    /**
     * Valor mínimo del rango.
     */
    private Integer valorMin;

    /**
     * Valor máximo del rango.
     */
    private Integer valorMax;

    /**
     * Descripción del rango.
     */
    private String descripcion;

    /**
     * Constructor utilizado para crear una nueva clasificación sin especificar el ID.
     *
     * Este constructor es útil antes de persistir el objeto en base de datos,
     * donde el ID suele generarse automáticamente.
     *
     * @param nombreRango Nombre del rango.
     * @param valorMin Valor mínimo del rango.
     * @param valorMax Valor máximo del rango.
     * @param descripcion Descripción del rango.
     */
    public Clasificacion(String nombreRango, Integer valorMin, Integer valorMax, String descripcion) {
        this.nombreRango = nombreRango;
        this.valorMin = valorMin;
        this.valorMax = valorMax;
        this.descripcion = descripcion;
    }
}