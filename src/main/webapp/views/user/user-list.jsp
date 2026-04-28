<%@ include file="../../partials/header.jsp" %>
<!-- Incluimos el encabezado común (doctype, <head>, menú, etc.) -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />

<!--
  JSP para mostrar la lista de usuarios en formato tabla.

  Variables esperadas en request:
    - listUsers : lista de objetos User que se mostrarán en la tabla

  Funcionalidades:
    - Visualizar todos los usuarios con sus propiedades.
    - Enlaces para editar usuarios existentes.
    - Formularios para eliminar usuarios con confirmación.
    - Enlace para agregar un nuevo usuario.
-->

<h1><fmt:message key="msg.user-list.title"/></h1>

<!-- Enlace para crear un nuevo usuario -->
<a href="users?action=new"><fmt:message key="msg.user-list.add"/></a>

<!-- Tabla HTML para mostrar todos los usuarios -->
<table border="1">
   <thead>
      <tr>
         <th><fmt:message key="msg.user-list.id"/></th>
         <th><fmt:message key="msg.user-list.username"/></th>
         <th><fmt:message key="msg.user-list.active"/></th>
         <th><fmt:message key="msg.user-list.accountNonLocked"/></th>
         <th><fmt:message key="msg.user-list.lastPasswordChange"/></th>
         <th><fmt:message key="msg.user-list.passwordExpiresAt"/></th>
         <th><fmt:message key="msg.user-list.failedLoginAttempts"/></th>
         <th><fmt:message key="msg.user-list.emailVerified"/></th>
         <th><fmt:message key="msg.user-list.mustChangePassword"/></th>
         <th><fmt:message key="msg.user-list.actions"/></th>
      </tr>
   </thead>
   <tbody>
      <c:forEach var="user" items="${listUsers}">
         <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.active}</td>
            <td>${user.accountNonLocked}</td>

            <!-- Mostrar fecha del último cambio de contraseña si existe -->
            <td>
               <c:if test="${user.lastPasswordChange != null}">
                   ${user.lastPasswordChange}
               </c:if>
            </td>

            <!-- Mostrar fecha de caducidad de la contraseña si existe -->
            <td>
               <c:if test="${user.passwordExpiresAt != null}">
                   ${user.passwordExpiresAt}
               </c:if>
            </td>

            <td>${user.failedLoginAttempts}</td>
            <td>${user.emailVerified}</td>
            <td>${user.mustChangePassword}</td>

            <!-- Acciones: editar y eliminar -->
            <td>
               <!-- Enlace para editar usuario -->
               <a href="users?action=edit&id=${user.id}">
                  <fmt:message key="msg.user-list.edit"/>
               </a>

               <!-- Formulario para eliminar usuario con confirmación -->
               <form action="users" method="post" style="display:inline;">
                  <input type="hidden" name="action" value="delete"/>
                  <input type="hidden" name="id" value="${user.id}" />
                  <input type="submit" value="<fmt:message key='msg.user-list.delete'/>"
                         onclick="return confirm('<fmt:message key='msg.user-list.confirm'/>')" />
               </form>
            </td>
         </tr>
      </c:forEach>
   </tbody>
</table>

<%@ include file="../../partials/footer.jsp" %>