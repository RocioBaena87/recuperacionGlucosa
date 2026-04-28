<%@ include file="../../partials/header.jsp" %>
<!-- Incluimos cabecera común -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />

<!--
  Formulario de usuario.
  Este JSP sirve para crear un nuevo usuario o editar uno existente.

  Variables esperadas en request:
    - user : objeto User a editar (si es null, se asume creación)
    - errorMessage : mensaje de error a mostrar (opcional)

  Campos:
    - username
    - passwordHash
    - active
    - accountNonLocked
    - failedLoginAttempts
    - lastPasswordChange
-->

<h1>
   <c:if test="${user == null}">
      <fmt:message key="msg.user-form.add"/>
   </c:if>
   <c:if test="${user != null}">
      <fmt:message key="msg.user-form.edit"/>
   </c:if>
</h1>

<!-- Mostrar mensaje de error si existe -->
<c:if test="${not empty errorMessage}">
   <div class="error-message">${errorMessage}</div>
</c:if>

<form action="users" method="post">

   <!-- ID oculto: se usa al editar un usuario -->
   <input type="hidden" name="id" value="${user != null ? user.id : ''}"/>

   <!-- Acción del formulario: insert para crear, update para actualizar -->
   <input type="hidden" name="action" value="${user == null ? 'insert' : 'update'}" />

   <!-- Campo: Nombre de usuario -->
   <label for="username"><fmt:message key="msg.user-form.username"/></label>
   <input type="text" name="username" id="username"
          value="${user != null ? user.username : ''}" required maxlength="40" />

   <!-- Campo: Contraseña -->
   <label for="passwordHash"><fmt:message key="msg.user-form.passwordHash"/></label>
   <input type="password" name="passwordHash" id="passwordHash"
          value="${user != null ? user.passwordHash : ''}" required maxlength="500" />

   <!-- Checkbox: Activo -->
   <label for="active"><fmt:message key="msg.user-form.active"/></label>
   <input type="checkbox" name="active" id="active" value="true"
          <c:if test="${user != null and user.active}">checked="checked"</c:if> />

   <!-- Checkbox: Cuenta no bloqueada -->
   <label for="accountNonLocked"><fmt:message key="msg.user-form.accountNonLocked"/></label>
   <input type="checkbox" name="accountNonLocked" id="accountNonLocked" value="true"
          <c:if test="${user != null and user.accountNonLocked}">checked="checked"</c:if> />

   <!-- Número de intentos fallidos -->
   <label for="failedLoginAttempts"><fmt:message key="msg.user-form.failedLoginAttempts"/></label>
   <input type="number" name="failedLoginAttempts" id="failedLoginAttempts"
          min="0" value="${user != null ? user.failedLoginAttempts : '0'}" />

   <!-- Fecha del último cambio de contraseña -->
   <label for="lastPasswordChange"><fmt:message key="msg.user-form.lastPasswordChange"/></label>
   <input type="datetime-local" name="lastPasswordChange" id="lastPasswordChange"
          value="${user != null && user.lastPasswordChange != null ? user.lastPasswordChange : ''}" />

   <!-- Botón de acción dinámico -->
   <c:if test="${user == null}">
      <input type="submit" value="<fmt:message key='msg.user-form.create'/>" />
   </c:if>
   <c:if test="${user != null}">
      <input type="submit" value="<fmt:message key='msg.user-form.update'/>" />
   </c:if>
</form>

<!-- Enlace de retorno a la lista de usuarios -->
<a href="users"><fmt:message key="msg.user-form.returnback"/></a>

<%@ include file="../../partials/footer.jsp" %>