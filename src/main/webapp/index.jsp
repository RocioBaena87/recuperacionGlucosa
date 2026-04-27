<%@ include file="partials/header.jsp" %>
<h2>Hola Rocio Baena!</h2>
<%
   int count =10;
   for (int i =0; i < count; i++) {
      out.println("Number: " + i + "<br>");
   }
%>
<p>Current Time: <%= new java.util.Date () %></p>
<%!
   private int counter = 0;

   public int getCounter() {
      return ++counter;
   }
%>
<p>Counter Value: <%= getCounter() %></p>
<%
   //Código Java embebido (Scriptlet)
   String userName= "Rocio Baena";
   int age = 38;
%>

<p>User Name: <%= userName %></p> <!-- Expresión -->
<p>User Age: <%= age %></p> <!-- Expresión -->

<%
   //Scriptlet para realizar cálculos
   int num1 = 10;
   int num2 = 20;
   int sum = num1 + num2;
%>

<p>Sum of <%= num1 %> and <%= num2 %> is: <%= sum %></p> <!-- Mostrando el resultado -->

<%
   String role = "admin";
   if ("admin".equals(role)) {
%>
   <p>Welcome, Administrador!</p>
<%
   } else {
%>
   <p>Welcome, User!</p>
<%
   }
%>

<ul>
<%
   int i = 1;
   while ( i <= 5 ) {
%>
   <li>Item <%= i %></li>
<%
   i++;
   }
%>
</ul>

<h1>Number List:</h1>
<ul>
   <c:forEach var="i" begin="1" end="5">
   <li>Number: ${i}</li>
   </c:forEach>
   </ul>
<%@ include file="partials/footer.jsp" %>