<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!--
  Header JSP común para todas las páginas.

  Funcionalidades:
    - Configura la codificación y tipo de contenido mediante directivas JSP.
    - Importa las librerías JSTL (core y fmt) necesarias para lógica e internacionalización.
    - Establece el locale y el bundle de mensajes para i18n usando fmt:setLocale y fmt:setBundle.
    - Define el título dinámico de la página a partir de recursos internacionalizados.
    - Proporciona una barra de navegación con acceso a:
        * Página principal.
        * Registros de glucosa.
        * Clasificación de rangos.
        * Gestión de usuarios.
    - Incluye un selector de idioma que envía la petición al servlet changeLanguage.

  Variables esperadas en session:
    - sessionScope.locale : objeto Locale usado para internacionalización y selección de idioma.
-->

<fmt:setLocale value="${sessionScope.locale}"/>

<head>
   <!-- Título dinámico según idioma -->
   <title><fmt:message key="msg.title"/></title>
</head>

<html>
<body>

<!-- Navegación principal -->
<nav>
   <ul>
      <!-- Enlace directo a la página principal -->
      <li><a href="/index.jsp"><fmt:message key="msg.title"/></a></li>

      <!-- Enlaces a rutas de Servlets -->
      <li><a href="/registros"><fmt:message key="msg.registro-list.title"/></a></li>
            <li><a href="/clasificaciones"><fmt:message key="msg.clasificacion-list.title"/></a></li>
            <li><a href="/users"><fmt:message key="msg.user-list.title"/></a></li>

      <!-- Selector de idioma para internacionalización -->
      <li>
         <fmt:setBundle basename="messages"/>
         <form action="changeLanguage" method="get">
            <select name="lang" onchange="this.form.submit()">
               <option value="en" ${sessionScope.locale.language == 'en' ? 'selected' : ''}>English</option>
               <option value="es" ${sessionScope.locale.language == 'es' ? 'selected' : ''}>Español</option>
            </select>
         </form>
      </li>
   </ul>
</nav>