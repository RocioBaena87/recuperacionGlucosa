<%@ include file="../partials/header.jsp" %>
<%
  // Obtener el atributo userName desde la solicitud
  String userName = (String) request.getAttribute("userName");
  // Verificar si userName no es null antes de mostrarlo
  if (userName != null) {
%>
  <h2>Hello, <%= userName %>!</h2>
<%
  } else {
%>
  <h2>Hello, World!</h2>
<%
  }
%>

<p>Otra nomenclartura para escribirlo:</p>
<h2>Hello, ${empty userName ? 'World' : userName}!</h2>

<%@ include file="../partials/footer.jsp" %>