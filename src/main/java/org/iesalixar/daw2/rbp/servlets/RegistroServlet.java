package org.iesalixar.daw2.rbp.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.rbp.daos.ClasificacionDAO;
import org.iesalixar.daw2.rbp.daos.ClasificacionDAOImpl;
import org.iesalixar.daw2.rbp.daos.RegistroDAO;
import org.iesalixar.daw2.rbp.daos.RegistroDAOImpl;
import org.iesalixar.daw2.rbp.entities.Clasificacion;
import org.iesalixar.daw2.rbp.entities.Registro;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servlet encargado de gestionar las operaciones CRUD de la entidad {@link Registro}.
 * <p>
 * Permite listar, crear, editar y eliminar registros de glucosa.
 */
@WebServlet("/registros")
public class RegistroServlet extends HttpServlet {

    private RegistroDAO registroDAO;
    private ClasificacionDAO clasificacionDAO;

    /**
     * Inicializa los DAOs necesarios para el funcionamiento del servlet.
     *
     * @throws ServletException si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        try {
            registroDAO = new RegistroDAOImpl();
            clasificacionDAO = new ClasificacionDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar los DAOs", e);
        }
    }

    /**
     * Gestiona las peticiones GET.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {
            if (action == null) {
                action = "list";
            }

            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                default:
                    listRegistro(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Muestra el formulario para crear un nuevo registro.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si ocurre un error en la base de datos
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Clasificacion> listClasificaciones = clasificacionDAO.listAllClasificaciones();
        request.setAttribute("listClasificaciones", listClasificaciones);
        request.getRequestDispatcher("views/registro/registro-form.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario para editar un registro existente.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si ocurre un error en la base de datos
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        Registro existingRegistro = registroDAO.getRegistroById(id);
        List<Clasificacion> listClasificaciones = clasificacionDAO.listAllClasificaciones();

        request.setAttribute("registro", existingRegistro);
        request.setAttribute("listClasificaciones", listClasificaciones);
        request.getRequestDispatcher("views/registro/registro-form.jsp").forward(request, response);
    }

    /**
     * Muestra el listado de registros.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si ocurre un error en la base de datos
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void listRegistro(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Registro> listRegistros = registroDAO.listAllRegistros();
        request.setAttribute("listRegistros", listRegistros);
        request.getRequestDispatcher("views/registro/registro-list.jsp").forward(request, response);
    }

    /**
     * Gestiona las peticiones POST.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws ServletException si ocurre un error en el servlet
     * @throws IOException si ocurre un error de entrada/salida
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action) {
                case "insert":
                    insertRegistro(request, response);
                    break;
                case "update":
                    updateRegistro(request, response);
                    break;
                case "delete":
                    deleteRegistro(request, response);
                    break;
                default:
                    listRegistro(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Elimina un registro por su identificador.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IOException si ocurre un error de entrada/salida
     */
    private void deleteRegistro(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        registroDAO.deleteRegistro(id);
        response.sendRedirect("registros");
    }

    /**
     * Actualiza un registro de glucosa existente en la base de datos.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IOException si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error en el servlet
     */
    private void updateRegistro(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        Long id = Long.parseLong(request.getParameter("id"));
        String valorStr = request.getParameter("valor");
        String fechaStr = request.getParameter("fecha");
        String clasificacionIdStr = request.getParameter("clasificacion_id");

        if (valorStr == null || valorStr.isEmpty()
                || fechaStr == null || fechaStr.isEmpty()) {

            request.setAttribute("errorMessage", "El valor de glucosa y la fecha son obligatorios.");
            List<Clasificacion> listClasificaciones = clasificacionDAO.listAllClasificaciones();
            request.setAttribute("listClasificaciones", listClasificaciones);
            request.getRequestDispatcher("views/registro/registro-form.jsp").forward(request, response);
            return;
        }

        Integer valor = Integer.parseInt(valorStr);
        LocalDateTime fecha = LocalDateTime.parse(fechaStr);
        Long clasificacionId = (clasificacionIdStr != null && !clasificacionIdStr.isEmpty())
                ? Long.parseLong(clasificacionIdStr)
                : null;

        Registro updateRegistro = new Registro(id, valor, fecha, clasificacionId);

        try {
            registroDAO.updateRegistro(updateRegistro);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al actualizar el registro.");
            showEditForm(request, response);
            return;
        }

        response.sendRedirect("registros");
    }

    /**
     * Inserta un nuevo registro de glucosa en la base de datos.
     *
     * @param request petición HTTP
     * @param response respuesta HTTP
     * @throws SQLException si ocurre un error en la base de datos
     * @throws IOException si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error en el servlet
     */
    private void insertRegistro(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String idStr = request.getParameter("id");
        String valorStr = request.getParameter("valor");
        String fechaStr = request.getParameter("fecha");
        String clasificacionIdStr = request.getParameter("clasificacion_id");

        if (idStr == null || idStr.isEmpty()
                || valorStr == null || valorStr.isEmpty()
                || fechaStr == null || fechaStr.isEmpty()) {

            request.setAttribute("errorMessage", "Todos los campos, incluido el ID, son obligatorios.");
            showNewForm(request, response);
            return;
        }

        Long id = Long.parseLong(idStr);

        if (registroDAO.existsRegistroById(id)) {
            request.setAttribute("errorMessage", "El código (ID) del registro ya existe.");
            showNewForm(request, response);
            return;
        }

        try {
            Registro newRegistro = new Registro(
                    id,
                    Integer.parseInt(valorStr),
                    LocalDateTime.parse(fechaStr),
                    (clasificacionIdStr == null || clasificacionIdStr.isEmpty())
                            ? null
                            : Long.parseLong(clasificacionIdStr)
            );

            registroDAO.insertRegistro(newRegistro);

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                request.setAttribute("errorMessage", "Error de base de datos: El ID ya está en uso.");
                showNewForm(request, response);
                return;
            }
            throw e;
        }

        response.sendRedirect("registros");
    }
}