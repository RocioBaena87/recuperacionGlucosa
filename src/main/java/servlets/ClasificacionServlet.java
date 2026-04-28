package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesalixar.daw2.rbp.daos.ClasificacionDAO;
import org.iesalixar.daw2.rbp.daos.ClasificacionDAOImpl;
import org.iesalixar.daw2.rbp.entities.Clasificacion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet encargado de gestionar las operaciones CRUD de la entidad {@link Clasificacion}.
 * Permite listar, crear, editar y eliminar clasificaciones.
 */
@WebServlet("/clasificaciones")
public class ClasificacionServlet extends HttpServlet {

    private ClasificacionDAO clasificacionDAO;

    /**
     * Inicializa el DAO de clasificaciones.
     *
     * @throws ServletException si ocurre un error durante la inicialización
     */
    @Override
    public void init() throws ServletException {
        try {
            clasificacionDAO = new ClasificacionDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar el ClasificacionDAO", e);
        }
    }

    /**
     * Gestiona las peticiones GET.
     * Permite mostrar listado, formulario de creación o formulario de edición.
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
                    listClasificacion(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Muestra el formulario para crear una nueva clasificación.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario de edición de una clasificación existente.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        Clasificacion existingClasificacion = clasificacionDAO.getClasificacionById(id);

        request.setAttribute("clasificacion", existingClasificacion);
        request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
    }

    /**
     * Muestra el listado de clasificaciones.
     */
    private void listClasificacion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Clasificacion> listClasificaciones = clasificacionDAO.listAllClasificaciones();
        request.setAttribute("listClasificaciones", listClasificaciones);
        request.getRequestDispatcher("views/clasificacion/clasificacion-list.jsp").forward(request, response);
    }

    /**
     * Gestiona las peticiones POST.
     * Permite insertar, actualizar o eliminar clasificaciones.
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
                    insertClasificacion(request, response);
                    break;
                case "update":
                    updateClasificacion(request, response);
                    break;
                case "delete":
                    deleteClasificacion(request, response);
                    break;
                default:
                    listClasificacion(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Elimina una clasificación por su identificador.
     */
    private void deleteClasificacion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Long id = Long.parseLong(request.getParameter("id"));
        clasificacionDAO.deleteClasificacion(id);
        response.sendRedirect("clasificaciones");
    }

    /**
     * Actualiza una clasificación existente tras validar los datos.
     */
    private void updateClasificacion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        Long id = Long.parseLong(request.getParameter("id"));
        String nombreRango = request.getParameter("nombre_rango").trim().toUpperCase();
        String descripcion = request.getParameter("descripcion").trim();
        String valorMinStr = request.getParameter("valor_min").trim();
        String valorMaxStr = request.getParameter("valor_max").trim();

        if (nombreRango.isEmpty() || valorMinStr.isEmpty() || valorMaxStr.isEmpty()) {
            request.setAttribute("errorMessage", "El nombre de rango y los valores no pueden estar vacíos.");
            request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
            return;
        }

        if (clasificacionDAO.existsClasificacionByNombreRangoAndNotId(nombreRango, id)) {
            request.setAttribute("errorMessage", "El nombre de rango ya existe para otra clasificacion.");
            request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
            return;
        }

        Integer valorMin = Integer.parseInt(valorMinStr);
        Integer valorMax = Integer.parseInt(valorMaxStr);

        Clasificacion updateClasificacion = new Clasificacion(id, nombreRango, valorMin, valorMax, descripcion);

        try {
            clasificacionDAO.updateClasificacion(updateClasificacion);
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                request.setAttribute("errorMessage", "El nombre de rango debe ser único.");
                request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
                return;
            } else {
                throw e;
            }
        }

        response.sendRedirect("clasificaciones");
    }

    /**
     * Inserta una nueva clasificación tras validar los datos.
     */
    private void insertClasificacion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        String nombreRango = request.getParameter("nombre_rango").trim().toUpperCase();
        String descripcion = request.getParameter("descripcion").trim();
        String valorMinStr = request.getParameter("valor_min").trim();
        String valorMaxStr = request.getParameter("valor_max").trim();

        if (nombreRango.isEmpty() || valorMinStr.isEmpty() || valorMaxStr.isEmpty()) {
            request.setAttribute("errorMessage", "El nombre de rango y los valores no pueden estar vacíos.");
            request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
            return;
        }

        if (clasificacionDAO.existsClasificacionByNombreRango(nombreRango)) {
            request.setAttribute("errorMessage", "El nombre de rango de la clasificacion ya exite.");
            request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
            return;
        }

        Integer valorMin = Integer.parseInt(valorMinStr);
        Integer valorMax = Integer.parseInt(valorMaxStr);

        Clasificacion newClasificacion = new Clasificacion(nombreRango, valorMin, valorMax, descripcion);

        try {
            clasificacionDAO.insertClasificacion(newClasificacion);
        } catch (SQLException e) {
            if (e.getSQLState() != null && e.getSQLState().equals("23505")) {
                request.setAttribute("errorMessage", "El nombre de rango debe ser único.");
                request.getRequestDispatcher("views/clasificacion/clasificacion-form.jsp").forward(request, response);
                return;
            } else {
                throw e;
            }
        }

        response.sendRedirect("clasificaciones");
    }
}