<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/courseOccurrence.css">

<div class="main-content">
  <c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
  </c:if>
  <c:if test="${not empty successMessage}">
    <p class="success-message">${successMessage}</p>
  </c:if>

    <c:forEach var="courseOccurrence" items="${courseOccurrences}">
      <div class="course-occurrence-item" onclick="openPopup(this, false, '${courseOccurrence.course.id}', '${courseOccurrence.category.id}', '${courseOccurrence.day}', '${courseOccurrence.beginning}', '${courseOccurrence.end}', '${courseOccurrence.id}')">
        <h4>${courseOccurrence.course.subject.name} - ${courseOccurrence.day}</h4>
        <p>De ${courseOccurrence.beginning} à ${courseOccurrence.end}</p>
        <p>Catégorie : ${courseOccurrence.category.name}</p>
        <p>Avec : ${courseOccurrence.professor.firstName} ${courseOccurrence.professor.lastName}</p>
        <p>Dans : ${courseOccurrence.classroom}</p>
      </div>
    </c:forEach>

  <div class="popup" id="popup" onclick="closePopup(event)">
    <div class="popup-content">
      <form action="${pageContext.request.contextPath}/CourseOccurrences" method="post">
        <label for="course">Cours :</label>
        <select id="course" name="course" required>
          <option value="">Sélectionnez un cours</option>
          <c:forEach var="course" items="${courses}">
            <option value="${course.id}">
                ${course.subject.name} avec ${course.professor.firstName} ${course.professor.lastName} Dans la salle ${course.classroom}
            </option>
          </c:forEach>
        </select><br>

        <label for="classCategory">Catégorie :</label>
        <select name="classCategory" id="classCategory" required>
          <option value="" disabled selected>Sélectionnez une catégorie</option>
          <c:forEach var="classCategory" items="${categories}">
            <option value="${classCategory.id}">${classCategory.name}</option>
          </c:forEach>
        </select><br>

        <label for="day">Jour :</label>
        <input type="date" id="day" name="day" required><br>

        <label for="beginning">Heure de début :</label>
        <input type="time" id="beginning" name="beginning" required><br>

        <label for="end">Heure de fin :</label>
        <input type="time" id="end" name="end" required><br>

        <input type="hidden" name="id" class="id" value="">
        <button type="submit" name="action" value="save">Enregistrer</button>
        <button type="submit" name="action" value="delete" style="color: red;">Supprimer l'occurrence</button>
      </form>
    </div>
  </div>
  <button onclick="openPopup(this, true, null, null, null, null, null)">Créer une occurrence</button>
  <script src="${pageContext.request.contextPath}/assets/js/courseOccurrence.js"></script>

</div>

<%@ include file="fragments/footer.jsp" %>
