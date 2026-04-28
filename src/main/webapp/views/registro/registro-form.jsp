<%@ include file="../../partials/header.jsp" %>
<!-- Incluye el encabezado común (HTML <head>, menú, etc.) desde la carpeta partials -->

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />

<h1>
   <!--
      Título dinámico
      Si "registro" es null -> muestra "Nuevo Registro".
      Si "registro" no es null -> muestra "Editar Registro".
   -->
   <fmt:message key="${registro == null ? 'msg.registro-form.add' : 'msg.registro-form.edit'}" />
</h1>

<%-- Mostrar mensaje de error si existe en el modelo --%>
<c:if test="${not empty errorMessage}">
   <div class="error-message">${errorMessage}</div>
</c:if>

<!-- Formulario que envía datos a la ruta /registros por POST -->
<form action="registros" method="post">

   <!-- Campo para introducir el id del registro (vacío si es nuevo) -->
   <label for="id">ID:</label>
   <input type="number" name="id" id="id"
          value="${registro != null ? registro.id : ''}"
          ${registro != null ? 'readonly' : ''} required />
   <br/>

   <!-- Campo oculto para indicar la acción: insert (crear) o update (editar) -->
   <input type="hidden" name="action" value="${registro == null ? 'insert' : 'update'}" />

   <!-- Campo para introducir el valor de glucosa -->
   <label for="valor"><fmt:message key="msg.registro-form.valor"/></label>
   <input type="number" name="valor" id="valor"
          value="${registro != null ? registro.valor : ''}"
          required />

   <!-- Campo para introducir la fecha del registro -->
   <label for="fecha"><fmt:message key="msg.registro-form.fecha"/></label>
   <input type="datetime-local" name="fecha" id="fecha"
          value="${registro != null && registro.fecha != null ? registro.fecha : ''}"
          required />

   <!-- Selector para elegir la clasificación del registro -->
   <label for="clasificacion_id"><fmt:message key="msg.registro-form.clasificacion"/></label>
   <select name="clasificacion_id" id="clasificacion_id">

       <!-- Opción por defecto (sin clasificación seleccionada) -->
       <option value="">
           <fmt:message key="msg.registro-form.selectClasificacion"/>
       </option>

       <!-- Itera sobre la lista de clasificaciones disponibles -->
       <c:forEach var="clasificacion" items="${listClasificaciones}">

           <!--
              Marca como "selected" la clasificación asociada al registro
              cuando se está editando
           -->
           <option value="${clasificacion.id}"
               <c:if test="${registro != null && registro.clasificacionId == clasificacion.id}">
                   selected
               </c:if>>
               ${clasificacion.nombreRango}
           </option>

       </c:forEach>
   </select>

   <!-- Botón dinámico: "Crear" si es nuevo, "Actualizar" si se está editando -->
   <input type="submit"
          value="<fmt:message key='${registro == null ? "msg.registro-form.create" : "msg.registro-form.update"}' />" />
</form>

<!-- Enlace para volver a la lista de registros -->
<a href="registros">
   <fmt:message key="msg.registro-form.returnback"/>
</a>

<%@ include file="../../partials/footer.jsp" %>
<!-- Incluye el pie de la página común (HTML de cierre, scripts, etc.) -->