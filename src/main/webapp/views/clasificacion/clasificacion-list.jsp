<%@ include file="../../partials/header.jsp" %>
<!-- Incluye el encabezado común (doctype, <head>, menú, etc.) desde la carpeta partials -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />

<!-- Título de la página -->
<h1>
    <fmt:message key="msg.clasificacion-list.title"/>
</h1>

<!-- Enlace para crear una nueva clasificación -->
<a href="clasificaciones?action=new">
    <fmt:message key="msg.clasificacion-list.add"/>
</a>

<!-- Tabla que muestra el listado de clasificaciones -->
<table border="1">

    <!-- Cabecera de la tabla -->
    <thead>
        <tr>
            <th><fmt:message key="msg.clasificacion-list.id"/></th>
            <th><fmt:message key="msg.clasificacion-list.nombreRango"/></th>
            <th><fmt:message key="msg.clasificacion-list.valorMin"/></th>
            <th><fmt:message key="msg.clasificacion-list.valorMax"/></th>
            <th><fmt:message key="msg.clasificacion-list.descripcion"/></th>
            <th><fmt:message key="msg.clasificacion-list.actions"/></th>
        </tr>
    </thead>

    <!-- Cuerpo de la tabla -->
    <tbody>

        <!-- Itera sobre la lista de clasificaciones recibida desde el servlet -->
        <c:forEach var="clasificacion" items="${listClasificaciones}">
            <tr>

                <!-- Datos de cada clasificación -->
                <td>${clasificacion.id}</td>
                <td>${clasificacion.nombreRango}</td>
                <td>${clasificacion.valorMin}</td>
                <td>${clasificacion.valorMax}</td>
                <td>${clasificacion.descripcion}</td>

                <!-- Acciones disponibles para cada fila -->
                <td>

                    <!-- Enlace para editar la clasificación -->
                    <a href="clasificaciones?action=edit&id=${clasificacion.id}">
                        <fmt:message key="msg.clasificacion-list.edit"/>
                    </a>

                    <!-- Formulario para eliminar la clasificación -->
                    <form action="clasificaciones" method="post" style="display:inline;">

                        <!-- Indica la acción delete -->
                        <input type="hidden" name="action" value="delete"/>

                        <!-- ID de la clasificación a eliminar -->
                        <input type="hidden" name="id" value="${clasificacion.id}" />

                        <!-- Botón de eliminación con confirmación -->
                        <input type="submit"
                               value="<fmt:message key='msg.clasificacion-list.delete'/>"
                               onclick="return confirm('<fmt:message key='msg.clasificacion-list.confirm'/>')" />
                    </form>

                </td>
            </tr>
        </c:forEach>

    </tbody>
</table>

<%@ include file="../../partials/footer.jsp" %>
<!-- Incluye el pie de la página común (HTML de cierre, scripts, etc.) -->