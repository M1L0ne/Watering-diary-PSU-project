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
        const response = await fetch(`${API_URL}/users/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ login: loginValue, password: passwordValue })
        });

        const data = await response.json();

        if (data.success) {
            localStorage.setItem('currentUserId', data.userId);
            localStorage.setItem('currentUserLogin', data.login);
            window.location.href = 'profile.html';
        } else {
            showError(data.error || 'Неверный логин или пароль');
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
        let errorMessage = 'Ошибка регистрации';
        const errorBody = await response.json();
        if (errorBody && errorBody.message) {
            errorMessage = errorBody.message;
        }

        throw new Error(errorMessage);
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
