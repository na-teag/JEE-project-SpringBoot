function openPopup(button, isNew, courseId, categoryId, day, beginning, end, id, professorId, classroom) {
    if (!isNew) {
        document.getElementById('course').value = courseId;
        document.getElementById('classCategory').value = categoryId;
        document.getElementById('day').value = day;
        document.getElementById('beginning').value = beginning;
        document.getElementById('end').value = end;
        document.querySelector('.id').value = id;
        document.getElementById("professor").value = professorId;
        document.getElementById("classroom").value = classroom;
        document.querySelector('button[name="action"][value="delete"]').style.display = 'inline-block';
    } else {
        document.getElementById('course').value = '';
        document.getElementById('classCategory').value = '';
        document.getElementById('day').value = '';
        document.getElementById('beginning').value = '';
        document.getElementById('end').value = '';
        document.querySelector('.id').value = '';
        document.getElementById("professor").value = '';
        document.getElementById("classroom").value = '';
        document.querySelector('button[name="action"][value="delete"]').style.display = 'none';

    }
    document.getElementById('popup').style.display = 'flex'
}

function closePopup(event) {
    if (event.target.id === 'popup') {
        document.getElementById('popup').style.display = 'none';
    }
}
