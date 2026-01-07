const API_URL = 'http://localhost:8080/api';
const currentUserId = localStorage.getItem('currentUserId');

if (!currentUserId) {
    window.location.href = 'index.html';
}

document.addEventListener('DOMContentLoaded', function() {
    loadConditions();
    setDefaultDate();
});

function setDefaultDate() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('add-date').value = today;
}

async function loadConditions() {
    try {
        const response = await fetch(`${API_URL}/conditions/user/${currentUserId}`);

        if (!response.ok) {
            throw new Error('Ошибка загрузки данных микроклимата');
        }

        const conditions = await response.json();
        displayConditions(conditions);

        document.getElementById('loading').style.display = 'none';
        document.getElementById('conditions-list').style.display = 'block';

    } catch (error) {
        document.getElementById('loading').style.display = 'none';
        showError('Не удалось загрузить данные: ' + error.message);
    }
}

function displayConditions(conditions) {
    const listDiv = document.getElementById('conditions-list');

    if (conditions.length === 0) {
        listDiv.innerHTML = '<p>У вас пока нет записей микроклимата. Добавьте первую!</p>';
        return;
    }

    conditions.sort((a, b) => new Date(b.date) - new Date(a.date));

    listDiv.innerHTML = conditions.map(condition => `
        <div class="condition-card">
            <div class="condition-info">
                <h3>${formatDate(condition.date)}</h3>
                <div class="condition-details">
                    <div class="condition-item">
                        <span class="label">Температура:</span>
                        <span class="value">${condition.temperature}°C</span>
                    </div>
                    <div class="condition-item">
                        <span class="label">Влажность:</span>
                        <span class="value">${condition.watering}%</span>
                    </div>
                </div>
            </div>
            <div class="condition-actions">
                <button class="btn btn-primary" onclick="showEditForm(${condition.id})">Редактировать</button>
                <button class="btn btn-danger" onclick="deleteCondition(${condition.id})">Удалить</button>
            </div>
        </div>
    `).join('');
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('ru-RU', options);
}

function showAddForm() {
    setDefaultDate();
    document.getElementById('add-form').style.display = 'block';
}

function closeAddForm() {
    document.getElementById('add-form').style.display = 'none';
    document.getElementById('add-date').value = '';
    document.getElementById('add-temperature').value = '';
    document.getElementById('add-watering').value = '';
}

async function addCondition(event) {
    event.preventDefault();

    const conditionData = {
        userId: parseInt(currentUserId),
        date: document.getElementById('add-date').value,
        temperature: parseInt(document.getElementById('add-temperature').value),
        watering: parseInt(document.getElementById('add-watering').value)
    };

    try {
        const response = await fetch(`${API_URL}/conditions`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(conditionData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка добавления записи');
        }

        closeAddForm();
        await loadConditions();
        showSuccess('Запись успешно добавлена!');

    } catch (error) {
        showError(error.message);
    }
}

async function showEditForm(conditionId) {
    try {
        const response = await fetch(`${API_URL}/conditions/${conditionId}`);
        if (!response.ok) throw new Error('Ошибка загрузки данных');

        const condition = await response.json();

        document.getElementById('edit-id').value = condition.id;
        document.getElementById('edit-date').value = condition.date || '';
        document.getElementById('edit-temperature').value = condition.temperature || '';
        document.getElementById('edit-watering').value = condition.watering || '';

        document.getElementById('edit-form').style.display = 'block';

    } catch (error) {
        showError('Ошибка загрузки данных: ' + error.message);
    }
}

function closeEditForm() {
    document.getElementById('edit-form').style.display = 'none';
}

async function updateCondition(event) {
    event.preventDefault();

    const conditionId = document.getElementById('edit-id').value;
    const updateData = {};

    const date = document.getElementById('edit-date').value;
    if (date) updateData.date = date;

    const temperature = document.getElementById('edit-temperature').value;
    if (temperature) updateData.temperature = parseInt(temperature);

    const watering = document.getElementById('edit-watering').value;
    if (watering) updateData.watering = parseInt(watering);

    try {
        const response = await fetch(`${API_URL}/conditions/${conditionId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updateData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка обновления записи');
        }

        closeEditForm();
        await loadConditions();
        showSuccess('Запись успешно обновлена!');

    } catch (error) {
        showError(error.message);
    }
}

async function deleteCondition(conditionId) {
    if (!confirm('Вы уверены, что хотите удалить эту запись?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/conditions/${conditionId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления записи');
        }

        await loadConditions();
        showSuccess('Запись успешно удалена!');

    } catch (error) {
        showError('Ошибка удаления: ' + error.message);
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

function showSuccess(message) {
    const successDiv = document.getElementById('success');
    successDiv.textContent = message;
    successDiv.style.display = 'block';
    setTimeout(() => {
        successDiv.style.display = 'none';
    }, 3000);
}

function logout() {
    if (confirm('Вы уверены, что хотите выйти?')) {
        localStorage.removeItem('currentUserId');
        localStorage.removeItem('currentUserLogin');
        window.location.href = 'index.html';
    }
}
