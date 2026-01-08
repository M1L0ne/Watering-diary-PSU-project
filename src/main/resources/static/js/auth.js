const API_URL = 'http://localhost:8080/api';

document.addEventListener('DOMContentLoaded', function() {
    const currentUserId = localStorage.getItem('currentUserId');
    const currentPath = window.location.pathname;

    if (currentUserId && (currentPath.includes('index.html') || currentPath.includes('register.html'))) {
        window.location.href = 'profile.html';
    }
});

async function loginUser(event) {
    event.preventDefault();

    const loginValue = document.getElementById('login').value.trim();
    const passwordValue = document.getElementById('password').value.trim();

    if (!loginValue || !passwordValue) {
        showError('Заполните все поля');
        return;
    }

    try {
        const response = await fetch(`${API_URL}/users`);

        if (!response.ok) {
            throw new Error('Ошибка подключения к серверу');
        }

        const users = await response.json();
        const user = users.find(u => u.login === loginValue && u.password === passwordValue);

        if (user) {
            localStorage.setItem('currentUserId', user.id);
            localStorage.setItem('currentUserLogin', user.login);
            window.location.href = 'profile.html';
        } else {
            showError('Неверный логин или пароль');
        }

    } catch (error) {
        showError('Ошибка входа: ' + error.message);
    }
}

async function registerUser(event) {
    event.preventDefault();

    const loginValue = document.getElementById('login').value.trim();
    const passwordValue = document.getElementById('password').value.trim();

    if (!loginValue || !passwordValue) {
        showError('Заполните обязательные поля');
        return;
    }

    const userData = {
        login: loginValue,
        password: passwordValue
    };

    const surnameValue = document.getElementById('surname').value.trim();
    if (surnameValue) userData.surname = surnameValue;

    const nameValue = document.getElementById('name').value.trim();
    if (nameValue) userData.name = nameValue;

    const patronymicValue = document.getElementById('patronymic').value.trim();
    if (patronymicValue) userData.patronymic = patronymicValue;

    try {
        const response = await fetch(`${API_URL}/users`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка регистрации');
        }

        const newUser = await response.json();

        localStorage.setItem('currentUserId', newUser.id);
        localStorage.setItem('currentUserLogin', newUser.login);

        window.location.href = 'profile.html';

    } catch (error) {
        showError(error.message);
    }
}

function showError(message) {
    const errorDiv = document.getElementById('error');
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
        setTimeout(() => {
            errorDiv.style.display = 'none';
        }, 5000);
    }
}
