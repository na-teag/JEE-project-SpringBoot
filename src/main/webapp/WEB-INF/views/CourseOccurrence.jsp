<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/courseOccurrence.css">

<div class="main-content">
	<h2>Gérer la plannification des cours</h2>
  <c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
  </c:if>
  <c:if test="${not empty successMessage}">
    <p class="success-message">${successMessage}</p>
  </c:if>

    <c:forEach var="courseOccurrence" items="${courseOccurrences}">
      <div class="course-occurrence-item" onclick="openPopup(this, false, '${courseOccurrence.course.id}', '${courseOccurrence.category.id}', '${courseOccurrence.day}', '${courseOccurrence.beginning}', '${courseOccurrence.end}', '${courseOccurrence.id}', '${courseOccurrence.professor.id}', '${courseOccurrence.classroom}')">
        <h4>${courseOccurrence.course.subject.name}</h4>
        <p>Le ${courseOccurrence.day.format(dateFormatter)}</p>
        <p>De ${courseOccurrence.beginning} à ${courseOccurrence.end}</p>
        <p>Catégorie : ${courseOccurrence.category.name}</p>
        <p>Avec : ${courseOccurrence.professor.firstName} ${courseOccurrence.professor.lastName}</p>
        <p>salle : ${courseOccurrence.classroom}</p>
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

        Optionnel :
        <label for="professor"> Professeur :</label>
        <select name="professor" id="professor">
          <option value="" disabled selected>Sélectionnez un professeur</option>
          <c:forEach var="professor" items="${professors}">
            <option value="${professor.id}">${professor.firstName} ${professor.lastName}</option>
          </c:forEach>
        </select><br>

        <label for="classroom" > Salle de classe :</label>
        <input type="text" id="classroom" name="classroom" placeholder="Numéro de salle"/>

        <input type="hidden" name="id" class="id" value="">
        <button type="submit" name="action" value="save">Enregistrer</button>
        <button type="submit" name="action" value="delete" style="color: red;">Supprimer l'occurrence</button>
      </form>
    </div>
  </div>
  <button onclick="openPopup(this, true, null, null, null, null, null, null, null)">Créer une occurrence</button>
  <script src="${pageContext.request.contextPath}/assets/js/courseOccurrence.js"></script>

</div>

<%@ include file="fragments/footer.jsp" %>
