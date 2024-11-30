<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Administration des horaires</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/courseAdd.css">
</head>
<body>
<div class="scheduleAdmin">
  <form action="/scheduleAdmin" method="post">
    <label for="course">Cours :</label>
    <select id="course" name="course" required>
      <option value="">Sélectionnez un cours</option>
      <c:forEach var="course" items="${courses}">
        <option value="${course.id}">
            ${course.subject.name} avec ${course.professor.firstName} ${course.professor.lastName}
        </option>
      </c:forEach>
    </select>
    <br>
    <!-- Catégorie -->
    <label for="classCategory">Catégorie :</label>
    <select name="classCategory" id="classCategory" required>
      <option value="" disabled selected>Sélectionnez une catégorie</option>
      <c:forEach var="classCategory" items="${classCategories}">
        <option value="${classCategory.id}">${classCategory.name}</option>
      </c:forEach>
    </select>

    <br>

    <label for="day">Jour :</label>
    <input type="date" id="day" name="day" required>
    <br>

    <label for="beginning">Heure de début :</label>
    <input type="time" id="beginning" name="beginning" required>
    <br>

    <label for="end">Heure de fin :</label>
    <input type="time" id="end" name="end" required>
    <br>

    <button type="submit">Enregistrer</button>
  </form>
  <c:if test="${not empty successMessage}">
    <p class="success-message">${successMessage}</p>
  </c:if>
  <c:if test="${not empty errorMessage}">
    <p class="error-message">${errorMessage}</p>
  </c:if>
</div>
</body>
</html>
