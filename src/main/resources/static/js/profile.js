const API_URL = 'http://localhost:8080/api';
const currentUserId = localStorage.getItem('currentUserId');

if (!currentUserId) {
    window.location.href = 'index.html';
}

document.addEventListener('DOMContentLoaded', function() {
    loadProfile();
});

async function loadProfile() {
    try {
        const response = await fetch(`${API_URL}/users/${currentUserId}`);

        if (!response.ok) {
            throw new Error('Ошибка загрузки профиля');
        }

        const user = await response.json();
        displayProfile(user);

        document.getElementById('loading').style.display = 'none';
        document.getElementById('profile-info').style.display = 'block';

    } catch (error) {
        document.getElementById('loading').style.display = 'none';
        showError('Не удалось загрузить профиль: ' + error.message);
    }
}

function displayProfile(user) {
    document.getElementById('user-login').textContent = user.login;
    document.getElementById('user-surname').textContent = user.surname || '-';
    document.getElementById('user-name').textContent = user.name || '-';
    document.getElementById('user-patronymic').textContent = user.patronymic || '-';
}

function showEditForm() {
    const surname = document.getElementById('user-surname').textContent;
    const name = document.getElementById('user-name').textContent;
    const patronymic = document.getElementById('user-patronymic').textContent;

    document.getElementById('edit-surname').value = surname !== '-' ? surname : '';
    document.getElementById('edit-name').value = name !== '-' ? name : '';
    document.getElementById('edit-patronymic').value = patronymic !== '-' ? patronymic : '';

    document.getElementById('profile-info').style.display = 'none';
    document.getElementById('edit-form').style.display = 'block';
}

function cancelEdit() {
    document.getElementById('edit-form').style.display = 'none';
    document.getElementById('profile-info').style.display = 'block';
    document.getElementById('edit-password').value = '';
}

async function updateProfile(event) {
    event.preventDefault();

    const updateData = {};

    const password = document.getElementById('edit-password').value;
    if (password) {
        updateData.password = password;
    }

    const surname = document.getElementById('edit-surname').value;
    if (surname) {
        updateData.surname = surname;
    }

    const name = document.getElementById('edit-name').value;
    if (name) {
        updateData.name = name;
    }

    const patronymic = document.getElementById('edit-patronymic').value;
    if (patronymic) {
        updateData.patronymic = patronymic;
    }

    try {
        const response = await fetch(`${API_URL}/users/${currentUserId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updateData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка обновления профиля');
        }

        const updatedUser = await response.json();
        displayProfile(updatedUser);
        cancelEdit();
        alert('Профиль успешно обновлен!');

    } catch (error) {
        showError('Ошибка обновления: ' + error.message);
    }
}

function showError(message) {
    const errorDiv = document.getElementById('error');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
    setTimeout(() => {
        errorDiv.style.display = 'none';
    }, 5000);
}

function logout() {
    if (confirm('Вы уверены, что хотите выйти?')) {
        localStorage.removeItem('currentUserId');
        localStorage.removeItem('currentUserLogin');
        window.location.href = 'index.html';
    }
}
