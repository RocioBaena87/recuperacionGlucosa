<%@ include file="../../partials/header.jsp" %>
<!-- Inluimos el encabezado común (doctype, <head>, menú, etc.) -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />

<h1><fmt:message key="msg.registro-list.title"/></h1>

<!-- Enlace que lleva el servelet con action=new -->
<!-- El servlet interpretará eso como: "mostrar formulario vació para creae una nueva región" -->
<a href="registros?action=new"><fmt:message key="msg.registro-list.add"/></a>

<!-- Tabla HTML para mostrar todos los registros -->
<table border="1">
    <thead>
        <tr>
            <!-- Cabecera de columnas -->
            <th><fmt:message key="msg.registro-list.id"/></th>
            <th><fmt:message key="msg.registro-list.valor"/></th>
            <th><fmt:message key="msg.registro-list.fecha"/></th>
            <th><fmt:message key="msg.registro-list.clasificacionId"/></th>
            <th><fmt:message key="msg.registro-list.actions"/></th>
        </tr>
    </thead>
    <tbody>
    <!--
             Bucle forEach (JSTL) : reconoce la lista "listRegistros" enviada por el servlet
             Cada elemento de la lista se asigna a la variable "registro"
          -->
        <c:forEach var="registro" items="${listRegistros}">
            <tr>
                <!-- Mostramos cada propiedad de la región usando El (Expresión Language) -->
                <td>${registro.id}</td>
                <td>${registro.valor} mg/dL</td>
                <td>${registro.fecha}</td>
                <td>${registro.clasificacionId != null ? registro.clasificacionId : '-'}</td>
                <td>
                    <!-- Enlace de edición: llama al servlet con "action=edit"
                    y pasa también el "id" del registro.
                    El servlet cargará los datos y mostrará el formulario de edición.
                    -->
                    <a href="registros?action=edit&id=${registro.id}">
                        <fmt:message key="msg.registro-list.edit"/>
                    </a>

                    <form action="registros" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="${registro.id}" />
                        <input type="submit" value="<fmt:message key='msg.registro-list.delete'/>"
                               onclick="return confirm('<fmt:message key='msg.registro-list.confirm'/>')" />
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="../../partials/footer.jsp" %>
<!-- Incluimos el pie común (script, cierre de HTML, etc.) -->