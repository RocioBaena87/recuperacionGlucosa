package org.iesalixar.daw2.rbp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.logging.Logger;


/**
 * La clase {@code User} representa una entidad que modela un usuario dentro de la base de datos.
 *
 * <p>Contiene toda la información relacionada con la cuenta del usuario, incluyendo:
 * <ul>
 *   <li>{@code id} - Identificador único en la base de datos.</li>
 *   <li>{@code username} - Nombre de usuario (único para login).</li>
 *   <li>{@code passwordHash} - Contraseña (actualmente en texto plano para pruebas).</li>
 *   <li>{@code active} - Estado de activación de la cuenta.</li>
 *   <li>{@code accountNonLocked} - Indica si la cuenta está bloqueada o no.</li>
 *   <li>{@code lastPasswordChange} - Fecha del último cambio de contraseña.</li>
 *   <li>{@code passwordExpiresAt} - Fecha de caducidad de la contraseña.</li>
 *   <li>{@code failedLoginAttempts} - Número de intentos fallidos de login.</li>
 *   <li>{@code emailVerified} - Si el correo electrónico ha sido verificado.</li>
 *   <li>{@code mustChangePassword} - Si el usuario debe cambiar su contraseña en el próximo inicio de sesión.</li>
 * </ul>
 *
 * <p>Además, esta clase utiliza {@link Logger} para registrar la creación de instancias de usuario,
 * con el objetivo de facilitar el seguimiento y depuración durante la ejecución.</p>
 *
 * <p>Las anotaciones de Lombok {@link Data}, {@link NoArgsConstructor} y {@link AllArgsConstructor}
 * generan automáticamente los métodos necesarios para evitar código repetitivo.</p>
 *
 * @author Rocío
 * @version 1.1
 */

@Data // Esta anotación de Lombok genera automáticamente los siguientes métodos:
// - Getters y setters para todos los campos
// - Los métodos 'equals()' y 'hashCode()' basados en todos los campos no transitorios.
// - El método 'toString()' que incluye todos los campos.
// - Un método 'canEqual()' que verefica si una instancia puede ser igual a otra.
// Esto evita tener que escribir manualmente todos estos métodos y mejora la mantenibilidad del código.

@NoArgsConstructor // Esta anotación genera un constructor sin argumentos (constructor vacío),
// es útil cuando quieres crear un objeto 'users' sin inicializarlo inmediatamente
// con valores. Esto es muy útil en frameworks como Hibernate o JPA.
// que requieren un constructor vacío para la creación de entidades.

@AllArgsConstructor
// Esta anotación genera un constructor que acepta todos los campos como parámetros.
// Este constructor es útil cuando necesitas crear una instancia completamente inicializada de 'User'.



public class User {

    /** Logger para registrar eventos y acciones relacionadas con la clase User. */
    private static final Logger logger = Logger.getLogger(User.class.getName());

    /** Identificador único del usuario en la base de datos. */
    private Long id;

    /** Nombre de usuario utilizado para iniciar sesión (único). */
    private String username;

    /** Contraseña del usuario (de momento almacenada en texto plano). */
    private String passwordHash;

    /** Indica si la cuenta está activa (true) o desactivada (false). */
    private boolean active;

    /** Indica si la cuenta está bloqueada por intentos fallidos (false = bloqueada). */
    private boolean accountNonLocked;

    /** Fecha y hora del último cambio de contraseña del usuario. */
    private LocalDateTime lastPasswordChange;

    /** Fecha exacta en la que caduca la contraseña. */
    private LocalDateTime passwordExpiresAt;

    /** Número de intentos fallidos de inicio de sesión. */
    private int failedLoginAttempts;

    /** Indica si el correo del usuario ha sido verificado. */
    private boolean emailVerified;

    /** Si es true, el usuario deberá cambiar la contraseña al iniciar sesión. */
    private boolean mustChangePassword;


    /**
     * Constructor que crea un nuevo usuario con nombre y contraseña.
     *
     * <p>Este constructor inicializa parcialmente el objeto {@code User}.
     * También registra el evento de creación en el {@link Logger}.</p>
     *
     * @param username Nombre de usuario.
     * @param passwordHash Contraseña del usuario (sin cifrar en este ejemplo).
     */
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;

        // Registro de la creación del objeto User
        logger.info("Se ha creado un nuevo objeto User con username: " + username);
    }
    /**
     * Método opcional para mostrar un mensaje de depuración (ejemplo de uso del Logger).
     *
     * <p>Este método podría usarse para imprimir información de diagnóstico sobre el usuario
     * o para registrar eventos específicos.</p>
     */
    public void logUserInfo() {
        logger.info("Información de usuario -> ID: " + id + ", Username: " + username);
    }

}