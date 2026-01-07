const API_URL = 'http://localhost:8080/api';
const currentUserId = localStorage.getItem('currentUserId');

if (!currentUserId) {
    window.location.href = 'index.html';
}

let allRecords = [];
let userPlants = [];

document.addEventListener('DOMContentLoaded', function() {
    loadData();
    setDefaultDateTime();
});

function setDefaultDateTime() {
    const today = new Date().toISOString().split('T')[0];
    const now = new Date().toTimeString().slice(0, 5);
    document.getElementById('add-date').value = today;
    document.getElementById('add-time').value = now;
}

async function loadData() {
    try {
        await loadPlants();
        await loadRecords();
    } catch (error) {
        showError('Ошибка загрузки данных: ' + error.message);
    }
}

async function loadPlants() {
    const response = await fetch(`${API_URL}/user-plants/user/${currentUserId}`);
    if (!response.ok) throw new Error('Ошибка загрузки растений');

    userPlants = await response.json();
    populatePlantSelects();
}

function populatePlantSelects() {
    const addSelect = document.getElementById('add-plant');
    const filterSelect = document.getElementById('filter-plant');

    const options = userPlants.map(plant =>
        `<option value="${plant.id}">${plant.name}</option>`
    ).join('');

    addSelect.innerHTML = '<option value="">Выберите растение</option>' + options;
    filterSelect.innerHTML = '<option value="">Все растения</option>' + options;
}

async function loadRecords() {
    try {
        const promises = userPlants.map(plant =>
            fetch(`${API_URL}/watering-records/plant/${plant.id}`)
                .then(res => res.ok ? res.json() : [])
        );

        const results = await Promise.all(promises);
        allRecords = results.flat();

        displayRecords(allRecords);

        document.getElementById('loading').style.display = 'none';
        document.getElementById('diary-list').style.display = 'block';

    } catch (error) {
        document.getElementById('loading').style.display = 'none';
        showError('Не удалось загрузить записи: ' + error.message);
    }
}

function displayRecords(records) {
    const listDiv = document.getElementById('diary-list');

    if (records.length === 0) {
        listDiv.innerHTML = '<p>У вас пока нет записей полива. Добавьте первую!</p>';
        return;
    }

    records.sort((a, b) => {
        const dateA = new Date(a.date + 'T' + a.time);
        const dateB = new Date(b.date + 'T' + b.time);
        return dateB - dateA;
    });

    listDiv.innerHTML = records.map(record => {
        const plant = getPlantById(record.userPlantId);
        const plantName = plant ? plant.name : 'Неизвестно';

        let errorRateMessage = '';
        let errorRateClass = 'error-true';

        if (record.errorRateK > 0) {
            errorRateMessage = `Недолито ${record.errorRateK} мл`;
        } else if (record.errorRateK < 0) {
            errorRateMessage = `Перелито ${Math.abs(record.errorRateK)} мл`;
        } else {
            errorRateMessage = 'Идеально';
            errorRateClass = 'value';
        }

        return `
            <div class="diary-card">
                <div class="diary-info">
                    <h3>${plantName}</h3>
                    <div class="diary-details">
                        <div class="diary-item">
                            <span class="label">Дата:</span>
                            <span class="value">${formatDate(record.date)}</span>
                        </div>
                        <div class="diary-item">
                            <span class="label">Время:</span>
                            <span class="value">${record.time}</span>
                        </div>
                        <div class="diary-item">
                            <span class="label">Объём:</span>
                            <span class="value">${record.volumeWatering} мл</span>
                        </div>
                        <div class="diary-item">
                            <span class="label">Статус:</span>
                            <span class="value ${errorRateClass}">${errorRateMessage}</span>
                        </div>
                    </div>
                </div>
                <div class="diary-actions">
                    <button class="btn btn-primary" onclick="showEditForm(${record.id})">Редактировать</button>
                    <button class="btn btn-danger" onclick="deleteRecord(${record.id})">Удалить</button>
                </div>
            </div>
        `;
    }).join('');
}

function getPlantById(plantId) {
    return userPlants.find(p => p.id === plantId);
}

function formatDate(dateString) {
    const date = new Date(dateString);
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString('ru-RU', options);
}

function filterRecords() {
    const plantId = document.getElementById('filter-plant').value;
    const dateFrom = document.getElementById('filter-date-from').value;
    const dateTo = document.getElementById('filter-date-to').value;

    let filtered = allRecords;

    if (plantId) {
        filtered = filtered.filter(r => r.userPlantId === parseInt(plantId));
    }

    if (dateFrom) {
        filtered = filtered.filter(r => r.date >= dateFrom);
    }

    if (dateTo) {
        filtered = filtered.filter(r => r.date <= dateTo);
    }

    displayRecords(filtered);
}

function resetFilters() {
    document.getElementById('filter-plant').value = '';
    document.getElementById('filter-date-from').value = '';
    document.getElementById('filter-date-to').value = '';
    displayRecords(allRecords);
}

function showAddForm() {
    setDefaultDateTime();
    document.getElementById('recommendation').style.display = 'none';
    document.getElementById('add-form').style.display = 'block';
}

function closeAddForm() {
    document.getElementById('add-form').style.display = 'none';
    document.getElementById('add-plant').value = '';
    document.getElementById('add-volume').value = '';
}

async function calculateRecommendation() {
    const plantId = document.getElementById('add-plant').value;

    if (!plantId) {
        document.getElementById('recommendation').style.display = 'none';
        return;
    }

    try {
        const response = await fetch(`${API_URL}/watering-records/calculate?userPlantId=${plantId}`);

        if (!response.ok) {
            document.getElementById('recommendation').style.display = 'none';
            return;
        }

        const data = await response.json();
        document.getElementById('recommended-volume').textContent = data.recommendedVolume;
        document.getElementById('recommendation').style.display = 'block';

    } catch (error) {
        document.getElementById('recommendation').style.display = 'none';
    }
}

async function addRecord(event) {
    event.preventDefault();

    const recordData = {
        userPlantId: parseInt(document.getElementById('add-plant').value),
        date: document.getElementById('add-date').value,
        time: document.getElementById('add-time').value,
        volumeWatering: parseInt(document.getElementById('add-volume').value)
    };

    try {
        const response = await fetch(`${API_URL}/watering-records`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(recordData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка добавления записи');
        }

        closeAddForm();
        await loadRecords();
        showSuccess('Запись полива успешно добавлена!');

    } catch (error) {
        showError(error.message);
        showWindowError('window-error-add', error.message);
    }
}

async function showEditForm(recordId) {
    try {
        const response = await fetch(`${API_URL}/watering-records/${recordId}`);
        if (!response.ok) throw new Error('Ошибка загрузки данных');

        const record = await response.json();

        document.getElementById('edit-id').value = record.id;
        document.getElementById('edit-date').value = record.date || '';
        document.getElementById('edit-time').value = record.time || '';
        document.getElementById('edit-volume').value = record.volumeWatering || '';

        document.getElementById('edit-form').style.display = 'block';

    } catch (error) {
        showError('Ошибка загрузки данных: ' + error.message);
    }
}

function closeEditForm() {
    document.getElementById('edit-form').style.display = 'none';
}

async function updateRecord(event) {
    event.preventDefault();

    const recordId = document.getElementById('edit-id').value;
    const updateData = {};

    const date = document.getElementById('edit-date').value;
    if (date) updateData.date = date;

    const time = document.getElementById('edit-time').value;
    if (time) updateData.time = time;

    const volume = document.getElementById('edit-volume').value;
    if (volume) updateData.volumeWatering = parseInt(volume);

    try {
        const response = await fetch(`${API_URL}/watering-records/${recordId}`, {
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
        await loadRecords();
        showSuccess('Запись полива успешно обновлена!');

    } catch (error) {
        showError(error.message);
        showWindowError('window-error-edit', error.message);
    }
}

async function deleteRecord(recordId) {
    if (!confirm('Вы уверены, что хотите удалить эту запись?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/watering-records/${recordId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления записи');
        }

        await loadRecords();
        showSuccess('Запись полива успешно удалена!');

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

function showWindowError(elementId, message) {
    const errorDiv = document.getElementById(elementId);
    if (errorDiv) {
        errorDiv.textContent = message;
        errorDiv.style.display = 'block';
        setTimeout(() => {
            errorDiv.style.display = 'none';
        }, 5000);
    }
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
