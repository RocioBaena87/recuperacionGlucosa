package org.iesalixar.daw2.rbp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.rbp.daos.UserDAO;
import org.iesalixar.daw2.rbp.daos.UserDAOImpl;
import org.iesalixar.daw2.rbp.entities.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Servlet encargado de gestionar todas las operaciones CRUD de la entidad {@link User}.
 *
 * <p>Este servlet atiende las peticiones HTTP GET y POST provenientes de las vistas JSP
 * para realizar operaciones de listado, creación, actualización y eliminación de usuarios.</p>
 *
 * <p>Incluye logging detallado para depuración y trazabilidad de las operaciones.</p>
 *
 * @author Rocío
 * @version 1.1
 */
@WebServlet("/users")
public class UserServlet extends HttpServlet {

    /** Logger para registrar eventos y acciones del servlet. */
    private static final Logger logger = Logger.getLogger(UserServlet.class.getName());

    /** DAO que proporciona acceso a la base de datos de usuarios. */
    private UserDAO userDAO;

    /**
     * Inicializa el servlet y establece la conexión con el DAO de usuarios.
     *
     * @throws ServletException si no se puede inicializar el DAO
     */
    @Override
    public void init() throws ServletException {
        logger.info("Entrando en init");
        try {
            userDAO = new UserDAOImpl();
            logger.info("Conexión con el DAO de usuarios establecida correctamente");
        } catch (Exception e) {
            logger.severe("Error al iniciar el DAO de usuarios: " + e.getMessage());
            throw new ServletException("No se pudo inicializar el DAO de usuarios", e);
        }
        logger.info("Saliendo de init");
    }

    /**
     * Procesa las solicitudes GET para listar, mostrar formularios de nuevo o editar usuario.
     *
     * @param request  Objeto HttpServletRequest que contiene la petición del cliente
     * @param response Objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException      si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en doGet");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            switch (action) {
                case "new":
                    mostrarFormularioNuevo(request, response);
                    break;
                case "edit":
                    mostrarFormularioEditar(request, response);
                    break;
                default:
                    mostrarListaUsuarios(request, response);
                    break;
            }
        } catch (SQLException ex) {
            logger.severe("Se produjo un error en doGet: " + ex.getMessage());
            throw new ServletException(ex);
        }

        logger.info("Saliendo de doGet");
    }

    /**
     * Procesa las solicitudes POST para insertar, actualizar o eliminar usuarios.
     *
     * @param request  Objeto HttpServletRequest que contiene la petición del cliente
     * @param response Objeto HttpServletResponse para enviar la respuesta
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException      si ocurre un error de entrada/salida
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en doPost");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "insert":
                    insertarUsuario(request, response);
                    break;
                case "update":
                    actualizarUsuario(request, response);
                    break;
                case "delete":
                    eliminarUsuario(request, response);
                    break;
                default:
                    mostrarListaUsuarios(request, response);
                    break;
            }
        } catch (SQLException ex) {
            logger.severe("Se produjo un error en doPost: " + ex.getMessage());
            throw new ServletException(ex);
        }

        logger.info("Saliendo de doPost");
    }

    /**
     * Muestra la lista completa de usuarios en la vista JSP correspondiente.
     *
     * @param request  Objeto HttpServletRequest
     * @param response Objeto HttpServletResponse
     * @throws SQLException       si ocurre un error con la base de datos
     * @throws ServletException   si ocurre un error de servlet
     * @throws IOException        si ocurre un error de entrada/salida
     */
    private void mostrarListaUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Entrando en mostrarListaUsuarios");
        List<User> listUsers = userDAO.listAllUsers();
        logger.info("Se han obtenido " + listUsers.size() + " usuarios de la base de datos");
        request.setAttribute("listUsers", listUsers);
        request.getRequestDispatcher("views/user/user-list.jsp").forward(request, response);
        logger.info("Saliendo de mostrarListaUsuarios");
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     *
     * @param request  Objeto HttpServletRequest
     * @param response Objeto HttpServletResponse
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException      si ocurre un error de entrada/salida
     */
    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        logger.info("Entrando en mostrarFormularioNuevo");
        request.getRequestDispatcher("views/user/user-form.jsp").forward(request, response);
        logger.info("Saliendo de mostrarFormularioNuevo");
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     *
     * @param request  Objeto HttpServletRequest
     * @param response Objeto HttpServletResponse
     * @throws SQLException       si ocurre un error con la base de datos
     * @throws ServletException   si ocurre un error de servlet
     * @throws IOException        si ocurre un error de entrada/salida
     */
    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        logger.info("Entrando en mostrarFormularioEditar");
        Long id = Long.parseLong(request.getParameter("id"));
        logger.info("Cargando usuario con ID: " + id);
        User existingUser = userDAO.getUserById(id);
        request.setAttribute("user", existingUser);
        request.getRequestDispatcher("views/user/user-form.jsp").forward(request, response);
        logger.info("Saliendo de mostrarFormularioEditar");
    }

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param request  Objeto HttpServletRequest
     * @param response Objeto HttpServletResponse
     * @throws SQLException       si ocurre un error con la base de datos
     * @throws IOException        si ocurre un error de entrada/salida
     * @throws ServletException   si ocurre un error de servlet
     */
    private void insertarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        logger.info("Entrando en insertarUsuario");

        // Lectura de parámetros del formulario
        String username = request.getParameter("username").trim();
        String passwordHash = request.getParameter("passwordHash").trim();
        Boolean active = Boolean.valueOf(request.getParameter("active"));
        Boolean accountNonLocked = Boolean.valueOf(request.getParameter("accountNonLocked"));

        LocalDateTime lastPasswordChange = request.getParameter("lastPasswordChange") != null
                && !request.getParameter("lastPasswordChange").isEmpty()
                ? LocalDateTime.parse(request.getParameter("lastPasswordChange"))
                : LocalDateTime.now();

        LocalDateTime passwordExpiresAt = lastPasswordChange.plusMonths(3);

        int failedLoginAttempts = request.getParameter("failedLoginAttempts") != null
                ? Integer.parseInt(request.getParameter("failedLoginAttempts"))
                : 0;
        Boolean emailVerified = Boolean.valueOf(request.getParameter("emailVerified"));
        Boolean mustChangePassword = Boolean.valueOf(request.getParameter("mustChangePassword"));

        logger.info("Intentando crear un nuevo usuario: " + username);

        // Validación de campos vacíos
        if (username.isEmpty() || passwordHash.isEmpty()) {
            logger.warning("No se puede crear el usuario: campos vacíos");
            request.setAttribute("errorMessage", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            request.getRequestDispatcher("views/user/user-form.jsp").forward(request, response);
            logger.info("Saliendo de insertarUsuario por error de campos vacíos");
            return;
        }

        // Validación de duplicados
        if (userDAO.existsUserByUsername(username)) {
            logger.warning("No se puede crear el usuario: ya existe un usuario con ese nombre");
            request.setAttribute("errorMessage", "Ya existe un usuario con ese nombre.");
            request.getRequestDispatcher("views/user/user-form.jsp").forward(request, response);
            logger.info("Saliendo de insertarUsuario por duplicado");
            return;
        }

        // Creación e inserción del usuario
        User newUser = new User(null, username, passwordHash, active, accountNonLocked,
                lastPasswordChange, passwordExpiresAt, failedLoginAttempts,
                emailVerified, mustChangePassword);

        userDAO.insertUser(newUser);
        logger.info("Usuario creado correctamente: " + username);
        response.sendRedirect("users");
        logger.info("Saliendo de insertarUsuario");
    }

    /**
     * Actualiza los datos de un usuario existente en la base de datos.
     *
     * @param request  Objeto HttpServletRequest
     * @param response Objeto HttpServletResponse
     * @throws SQLException       si ocurre un error con la base de datos
     * @throws IOException        si ocurre un error de entrada/salida
     * @throws ServletException   si ocurre un error de servlet
     */
    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        logger.info("Entrando en actualizarUsuario");

        Long id = Long.parseLong(request.getParameter("id"));
        String username = request.getParameter("username").trim();
        String passwordHash = request.getParameter("passwordHash").trim();
        Boolean active = Boolean.valueOf(request.getParameter("active"));
        Boolean accountNonLocked = Boolean.valueOf(request.getParameter("accountNonLocked"));

        LocalDateTime lastPasswordChange = request.getParameter("lastPasswordChange") != null
                && !request.getParameter("lastPasswordChange").isEmpty()
                ? LocalDateTime.parse(request.getParameter("lastPasswordChange"))
                : null;

        LocalDateTime passwordExpiresAt = lastPasswordChange != null
                ? lastPasswordChange.plusMonths(3)
                : null;

        int failedLoginAttempts = request.getParameter("failedLoginAttempts") != null
                ? Integer.parseInt(request.getParameter("failedLoginAttempts"))
                : 0;
        Boolean emailVerified = Boolean.valueOf(request.getParameter("emailVerified"));
        Boolean mustChangePassword = Boolean.valueOf(request.getParameter("mustChangePassword"));

        logger.info("Intentando actualizar el usuario con ID: " + id);

        if (username.isEmpty() || passwordHash.isEmpty()) {
            logger.warning("No se puede actualizar el usuario: campos vacíos");
            request.setAttribute("errorMessage", "El nombre de usuario y la contraseña no pueden estar vacíos.");
            request.getRequestDispatcher("views/user/user-form.jsp").forward(request, response);
            logger.info("Saliendo de actualizar Usuario por error de campos vacíos");
            return;
        }

        if (userDAO.existsUserByUsernameAndNotId(username, id)) {
            logger.warning("No se puede actualizar el usuario: nombre duplicado");
            request.setAttribute("errorMessage", "Ya existe un usuario con ese nombre.");
            request.getRequestDispatcher("views/user/user-form.jsp").forward(request, response);
            logger.info("Saliendo de actualizar Usuario por duplicado");
            return;
        }

        User updateUser = new User(id, username, passwordHash, active, accountNonLocked,
                lastPasswordChange, passwordExpiresAt, failedLoginAttempts,
                emailVerified, mustChangePassword);

        userDAO.updateUser(updateUser);
        logger.info("Usuario actualizado correctamente: " + username);
        response.sendRedirect("users");
        logger.info("Saliendo de actualizar Usuario");
    }

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param request  Objeto HttpServletRequest
     * @param response Objeto HttpServletResponse
     * @throws SQLException si ocurre un error con la base de datos
     * @throws IOException  si ocurre un error de entrada/salida
     */
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        logger.info("Entrando en eliminar Usuario");
        Long id = Long.parseLong(request.getParameter("id"));
        logger.info("Eliminando el usuario con ID: " + id);
        userDAO.deleteUser(id);
        logger.info("Usuario eliminado correctamente con ID: " + id);
        response.sendRedirect("users");
        logger.info("Saliendo de eliminar Usuario");
    }
}