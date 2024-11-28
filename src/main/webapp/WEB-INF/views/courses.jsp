<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="fragments/header.jsp" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/globalManagement.css">

<div class="main-content">
    <c:forEach var="course" items="${sessionScope.courses}">
        <div class="featured-sections" onclick="openPopup(this, false, '${course.id}', '${course.subject.id}', '${course.professor.id}', '${course.classroom}')">
            <h4>Matière : ${course.subject.name}</h4>
            <p>Professeur : ${course.professor.firstName} ${course.professor.lastName}</p>
            <p>Salle de classe : ${course.classroom}</p>
        </div>
    </c:forEach>

    <div class="popup" id="popup" onclick="closePopup(event)">
        <div class="popup-content">
            <form action="${pageContext.request.contextPath}/course" method="get">
                <p>Professeur :</p>
                <div id="professor-options">
                    <select name="professorId" required>
                        <option value="" disabled selected>Choisir un professeur</option>
                        <c:forEach var="professor" items="${sessionScope.professors}">
                            <option value="${professor.id}">${professor.firstName} ${professor.lastName}</option>
                        </c:forEach>
                    </select>
                </div>
                <p>Matière :</p>
                <div id="subject-options">
                    <select name="subjectId" required>
                        <option value="" disabled selected>Choisir une matière</option>
                        <c:forEach var="subject" items="${sessionScope.subjects}">
                            <option value="${subject.id}">${subject.name}</option>
                        </c:forEach>
                    </select>
                </div>

                <label for="classroom">Salle de classe :
                    <input type="text" id="classroom" name="classroom" required>
                </label>

                <input type="hidden" name="id" class="id" value="">

                <button type="submit" name="action" value="save">Valider</button>
                <button type="submit" name="action" value="delete" style="color: red;">Supprimer le cours</button>
            </form>
        </div>
    </div>

    <button onclick="openPopup(this, true, null, null, null)">Créer un cours</button>
    <script src="${pageContext.request.contextPath}/assets/js/courses.js"></script>

</div>

<%@ include file="fragments/footer.jsp" %>
