const API_URL = 'http://localhost:8080/api';
const currentUserId = localStorage.getItem('currentUserId');

if (!currentUserId) {
    window.location.href = 'index.html';
}

let plantTypes = [];
let materials = [];

document.addEventListener('DOMContentLoaded', function() {
    loadData();
});

async function loadData() {
    try {
        await Promise.all([
            loadPlantTypes(),
            loadMaterials(),
            loadPlants()
        ]);
    } catch (error) {
        showError('Ошибка загрузки данных: ' + error.message);
    }
}

async function loadPlantTypes() {
    const response = await fetch(`${API_URL}/plant-types`);
    if (!response.ok) throw new Error('Ошибка загрузки типов растений');
    plantTypes = await response.json();
    populatePlantTypeSelects();
}

async function loadMaterials() {
    const response = await fetch(`${API_URL}/materials`);
    if (!response.ok) throw new Error('Ошибка загрузки материалов');
    materials = await response.json();
    populateMaterialSelects();
}

function populatePlantTypeSelects() {
    const addSelect = document.getElementById('add-plant-type');
    const editSelect = document.getElementById('edit-plant-type');

    const options = plantTypes.map(pt => `<option value="${pt.id}">${pt.name}</option>`).join('');

    addSelect.innerHTML = '<option value="">Выберите тип</option>' + options;
    editSelect.innerHTML = '<option value="">Не менять</option>' + options;
}

function populateMaterialSelects() {
    const addSelect = document.getElementById('add-material');
    const editSelect = document.getElementById('edit-material');

    const options = materials.map(m => `<option value="${m.id}">${m.name}</option>`).join('');

    addSelect.innerHTML = '<option value="">Выберите материал</option>' + options;
    editSelect.innerHTML = '<option value="">Не менять</option>' + options;
}

async function loadPlants() {
    try {
        const response = await fetch(`${API_URL}/user-plants/user/${currentUserId}`);

        if (!response.ok) {
            throw new Error('Ошибка загрузки растений');
        }

        const plants = await response.json();
        displayPlants(plants);

        document.getElementById('loading').style.display = 'none';
        document.getElementById('plants-list').style.display = 'block';

    } catch (error) {
        document.getElementById('loading').style.display = 'none';
        showError('Не удалось загрузить растения: ' + error.message);
    }
}

function displayPlants(plants) {
    const listDiv = document.getElementById('plants-list');

    if (plants.length === 0) {
        listDiv.innerHTML = '<p>У вас пока нет растений. Добавьте первое!</p>';
        return;
    }

    listDiv.innerHTML = plants.map(plant => `
        <div class="plant-card">
            <div class="plant-info">
                <h3>${plant.name}</h3>
                <p><strong>Тип:</strong> ${getPlantTypeName(plant.plantTypeId)}</p>
                <p><strong>Материал горшка:</strong> ${getMaterialName(plant.materialId)}</p>
                ${plant.high ? `<p><strong>Высота:</strong> ${plant.high} см</p>` : ''}
                ${plant.potSize ? `<p><strong>Размер горшка:</strong> ${plant.potSize} см</p>` : ''}
                ${plant.soilLoosenerk ? `<p><strong>Разрыхлитель:</strong> ${plant.soilLoosenerk}%</p>` : ''}
            </div>
            <div class="plant-actions">
                <button class="btn btn-primary" onclick="showEditForm(${plant.id})">Редактировать</button>
                <button class="btn btn-danger" onclick="deletePlant(${plant.id})">Удалить</button>
            </div>
        </div>
    `).join('');
}

function getPlantTypeName(id) {
    const type = plantTypes.find(pt => pt.id === id);
    return type ? type.name : 'Неизвестно';
}

function getMaterialName(id) {
    const material = materials.find(m => m.id === id);
    return material ? material.name : 'Неизвестно';
}

function showAddForm() {
    document.getElementById('add-form').style.display = 'block';
}

function closeAddForm() {
    document.getElementById('add-form').style.display = 'none';
    document.getElementById('add-name').value = '';
    document.getElementById('add-plant-type').value = '';
    document.getElementById('add-material').value = '';
    document.getElementById('add-high').value = '';
    document.getElementById('add-pot-size').value = '';
    document.getElementById('add-soil-loosener').value = '';
}

async function addPlant(event) {
    event.preventDefault();

    const plantData = {
        userId: parseInt(currentUserId),
        name: document.getElementById('add-name').value.trim(),
        plantTypeId: parseInt(document.getElementById('add-plant-type').value),
        materialId: parseInt(document.getElementById('add-material').value)
    };

    const high = document.getElementById('add-high').value;
    if (high) plantData.high = parseInt(high);

    const potSize = document.getElementById('add-pot-size').value;
    if (potSize) plantData.potSize = parseInt(potSize);

    const soilLoosenerk = document.getElementById('add-soil-loosener').value;
    if (soilLoosenerk) plantData.soilLoosenerk = parseInt(soilLoosenerk);

    try {
        const response = await fetch(`${API_URL}/user-plants`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(plantData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка добавления растения');
        }

        closeAddForm();
        await loadPlants();
        showSuccess('Растение успешно добавлено!');

    } catch (error) {
        showError(error.message);
    }
}

async function showEditForm(plantId) {
    try {
        const response = await fetch(`${API_URL}/user-plants/${plantId}`);
        if (!response.ok) throw new Error('Ошибка загрузки данных растения');

        const plant = await response.json();

        document.getElementById('edit-id').value = plant.id;
        document.getElementById('edit-name').value = plant.name || '';
        document.getElementById('edit-plant-type').value = plant.plantTypeId || '';
        document.getElementById('edit-material').value = plant.materialId || '';
        document.getElementById('edit-high').value = plant.high || '';
        document.getElementById('edit-pot-size').value = plant.potSize || '';
        document.getElementById('edit-soil-loosener').value = plant.soilLoosenerk || '';

        document.getElementById('edit-form').style.display = 'block';

    } catch (error) {
        showError('Ошибка загрузки данных: ' + error.message);
    }
}

function closeEditForm() {
    document.getElementById('edit-form').style.display = 'none';
}

async function updatePlant(event) {
    event.preventDefault();

    const plantId = document.getElementById('edit-id').value;
    const updateData = {};

    const name = document.getElementById('edit-name').value.trim();
    if (name) updateData.name = name;

    const plantTypeId = document.getElementById('edit-plant-type').value;
    if (plantTypeId) updateData.plantTypeId = parseInt(plantTypeId);

    const materialId = document.getElementById('edit-material').value;
    if (materialId) updateData.materialId = parseInt(materialId);

    const high = document.getElementById('edit-high').value;
    if (high) updateData.high = parseInt(high);

    const potSize = document.getElementById('edit-pot-size').value;
    if (potSize) updateData.potSize = parseInt(potSize);

    const soilLoosenerk = document.getElementById('edit-soil-loosener').value;
    if (soilLoosenerk) updateData.soilLoosenerk = parseInt(soilLoosenerk);

    try {
        const response = await fetch(`${API_URL}/user-plants/${plantId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updateData)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Ошибка обновления растения');
        }

        closeEditForm();
        await loadPlants();
        showSuccess('Растение успешно обновлено!');

    } catch (error) {
        showError(error.message);
    }
}

async function deletePlant(plantId) {
    if (!confirm('Вы уверены, что хотите удалить это растение?')) {
        return;
    }

    try {
        const response = await fetch(`${API_URL}/user-plants/${plantId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Ошибка удаления растения');
        }

        await loadPlants();
        showSuccess('Растение успешно удалено!');

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
