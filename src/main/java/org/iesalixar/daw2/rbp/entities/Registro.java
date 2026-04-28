package org.iesalixar.daw2.rbp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Representa un registro almacenado en la base de datos.
 * <p>
 * Contiene información sobre un valor numérico, la fecha en la que se registró
 * y la referencia a una clasificación asociada mediante su identificador.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Registro {

    /**
     * Identificador único del registro.
     */
    private Long id;

    /**
     * Valor numérico asociado al registro.
     */
    private Integer valor;

    /**
     * Fecha y hora en la que se creó el registro.
     */
    private LocalDateTime fecha;

    /**
     * Identificador de la clasificación asociada.
     */
    private Long clasificacionId;

    /**
     * Constructor para crear un registro sin identificador.
     * <p>
     * Útil cuando el ID es generado automáticamente por la base de datos.
     *
     * @param valor valor numérico del registro
     * @param fecha fecha y hora del registro
     * @param clasificacionId identificador de la clasificación asociada
     */
    public Registro(Integer valor, LocalDateTime fecha, Long clasificacionId) {
        this.valor = valor;
        this.fecha = fecha;
        this.clasificacionId = clasificacionId;
    }
}