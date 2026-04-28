<%@ include file="../../partials/header.jsp" %>
<!-- Incluye el encabezado común (HTML <head>, menú, etc.) desde la carpeta partials -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />

<h1>
   <!--
      Título dinámico
      Si "clasificacion" es null -> muestra "Nueva Clasificación".
      Si "clasificacion" no es null -> muestra "Editar Clasificación".
   -->
   <fmt:message key="${clasificacion == null ? 'msg.clasificacion-form.add' : 'msg.clasificacion-form.edit'}" />
</h1>

<%-- Mostrar mensaje de error si existe en el modelo --%>
<c:if test="${not empty errorMessage}">
   <div class="error-message">${errorMessage}</div>
</c:if>

<!-- Formulario que envía datos a la ruta /clasificaciones por POST -->
<form action="clasificaciones" method="post">

   <!-- Campo oculto con el id de la clasificación (vacío si es nueva) -->
   <input type="hidden" name="id" value="${clasificacion != null ? clasificacion.id : ''}"/>

   <!-- Campo oculto para indicar la acción: insert (crear) o update (editar) -->
   <input type="hidden" name="action" value="${clasificacion == null ? 'insert' : 'update'}" />

   <!-- Campo para introducir el nombre del rango -->
   <label for="nombre_rango">
      <fmt:message key="msg.clasificacion-form.nombreRango"/>
   </label>
   <input type="text" name="nombre_rango" id="nombre_rango"
          value="${clasificacion != null ? clasificacion.nombreRango : ''}"
          required />

   <!-- Campo para introducir el valor mínimo -->
   <label for="valor_min">
      <fmt:message key="msg.clasificacion-form.valorMin"/>
   </label>
   <input type="number" name="valor_min" id="valor_min"
          value="${clasificacion != null ? clasificacion.valorMin : ''}"
          required />

   <!-- Campo para introducir el valor máximo -->
   <label for="valor_max">
      <fmt:message key="msg.clasificacion-form.valorMax"/>
   </label>
   <input type="number" name="valor_max" id="valor_max"
          value="${clasificacion != null ? clasificacion.valorMax : ''}"
          required />

   <!-- Campo para introducir la descripción -->
   <label for="descripcion">
      <fmt:message key="msg.clasificacion-form.descripcion"/>
   </label>
   <input type="text" name="descripcion" id="descripcion"
          value="${clasificacion != null ? clasificacion.descripcion : ''}" />

   <!-- Botón dinámico: "Crear" si es nueva, "Actualizar" si se está editando -->
   <input type="submit"
          value="<fmt:message key='${clasificacion == null ? "msg.clasificacion-form.create" : "msg.clasificacion-form.update"}' />" />
</form>

<!-- Enlace para volver a la lista de clasificaciones -->
<a href="clasificaciones">
   <fmt:message key="msg.clasificacion-form.returnback"/>
</a>

<%@ include file="../../partials/footer.jsp" %>
<!-- Incluye el pie de la página común (HTML de cierre, scripts, etc.) -->