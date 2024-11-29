function openPopup(element, isNew, courseId, subjectId, professorId, classroom) {
    if (!isNew) {
        document.querySelector('.id').value = courseId || ""; // ID du cours
        document.querySelector('[name="subjectId"]').value = subjectId || ""; // ID de la matière
        document.querySelector('[name="professorId"]').value = professorId || ""; // ID du professeur
        document.querySelector('[name="classroom"]').value = classroom || ""; // Salle de classe
        document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';
    } else {
        document.querySelector('.id').value = ""; // Réinitialiser l'ID
        document.querySelector('[name="subjectId"]').value = ""; // Réinitialiser la matière
        document.querySelector('[name="professorId"]').value = ""; // Réinitialiser le professeur
        document.querySelector('[name="classroom"]').value = ""; // Réinitialiser la salle de classe
        document.querySelector('button[name="action"][value="delete"]').style.display = 'none';
    }
    document.getElementById('popup').style.display = 'flex'; // Afficher la popup
}

function closePopup(event) {
    const popupContent = document.querySelector('.popup-content');
    if (!popupContent.contains(event.target)) {
        document.getElementById('popup').style.display = 'none'; // Masquer la popup
    }
}
