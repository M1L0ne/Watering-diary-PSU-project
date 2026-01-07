const API_URL = 'http://localhost:8080/api';
const currentUserId = localStorage.getItem('currentUserId');

if (!currentUserId) {
    window.location.href = 'index.html';
}

let allPlantTypes = [];
let allMaterials = [];

document.addEventListener('DOMContentLoaded', function() {
    loadPlantTypes();
    loadMaterials();
});

function showTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(tab => {
        tab.classList.remove('active');
    });
    document.querySelectorAll('.tab-button').forEach(btn => {
        btn.classList.remove('active');
    });

    if (tabName === 'plant-types') {
        document.getElementById('plant-types-tab').classList.add('active');
        document.querySelectorAll('.tab-button')[0].classList.add('active');
    } else {
        document.getElementById('materials-tab').classList.add('active');
        document.querySelectorAll('.tab-button')[1].classList.add('active');
    }
}

async function loadPlantTypes() {
    try {
        const response = await fetch(`${API_URL}/plant-types`);
        if (!response.ok) throw new Error('Ошибка загрузки типов растений');

        allPlantTypes = await response.json();
        displayPlantTypes(allPlantTypes);

        document.getElementById('plant-types-loading').style.display = 'none';
        document.getElementById('plant-types-list').style.display = 'block';

    } catch (error) {
        document.getElementById('plant-types-loading').style.display = 'none';
        showError('Ошибка загрузки типов растений: ' + error.message);
    }
}

function displayPlantTypes(plantTypes) {
    const listDiv = document.getElementById('plant-types-list');

    if (plantTypes.length === 0) {
        listDiv.innerHTML = '<p>Справочник пуст.</p>';
        return;
    }

    plantTypes.sort((a, b) => a.name.localeCompare(b.name));

    listDiv.innerHTML = '<div class="reference-list">' + plantTypes.map(pt => `
        <div class="reference-item" onclick="showPlantTypeDetails(${pt.id})">
            <div class="reference-row">
                <span class="reference-name">${pt.name}</span>
                <span class="badge ${getCategoryClass(pt.wateringK)}">${getCategory(pt.wateringK)}</span>
            </div>
        </div>
    `).join('') + '</div>';
}

function getCategory(wateringK) {
    if (wateringK <= 20) return 'Суккуленты';
    if (wateringK <= 40) return 'Умеренный полив';
    if (wateringK <= 70) return 'Средняя потребность';
    return 'Влаголюбивые';
}

function getCategoryClass(wateringK) {
    if (wateringK <= 20) return 'category-low';
    if (wateringK <= 40) return 'category-medium-low';
    if (wateringK <= 70) return 'category-medium';
    return 'category-high';
}

function getRecommendation(wateringK) {
    if (wateringK <= 20) return 'Поливать редко, 1-2 раза в месяц. Дать почве полностью высохнуть.';
    if (wateringK <= 40) return 'Поливать умеренно, когда верхний слой почвы подсохнет.';
    if (wateringK <= 70) return 'Регулярный полив, поддерживать почву слегка влажной.';
    return 'Частый полив, не допускать пересыхания почвы.';
}

function showPlantTypeDetails(id) {
    const plantType = allPlantTypes.find(pt => pt.id === id);
    if (!plantType) return;

    const modal = document.getElementById('details-modal');
    const content = document.getElementById('modal-details-content');

    content.innerHTML = `
        <h3>${plantType.name}</h3>
        <div class="modal-section">
            <h4>Описание</h4>
            <p>${plantType.description || 'Нет описания'}</p>
        </div>
        <div class="modal-section">
            <h4>Рекомендации по поливу</h4>
            <p>${getRecommendation(plantType.wateringK)}</p>
        </div>
    `;

    modal.style.display = 'block';
}

async function loadMaterials() {
    try {
        const response = await fetch(`${API_URL}/materials`);
        if (!response.ok) throw new Error('Ошибка загрузки материалов');

        allMaterials = await response.json();
        displayMaterials(allMaterials);

        document.getElementById('materials-loading').style.display = 'none';
        document.getElementById('materials-list').style.display = 'block';

    } catch (error) {
        document.getElementById('materials-loading').style.display = 'none';
        showError('Ошибка загрузки материалов: ' + error.message);
    }
}

function displayMaterials(materials) {
    const listDiv = document.getElementById('materials-list');

    if (materials.length === 0) {
        listDiv.innerHTML = '<p>Справочник пуст.</p>';
        return;
    }

    materials.sort((a, b) => a.name.localeCompare(b.name));

    listDiv.innerHTML = '<div class="reference-list">' + materials.map(mat => `
        <div class="reference-item" onclick="showMaterialDetails(${mat.id})">
            <div class="reference-row">
                <span class="reference-name">${mat.name}</span>
                <span class="badge ${getMaterialTypeClass(mat.wateringK)}">${getMaterialType(mat.wateringK)}</span>
            </div>
        </div>
    `).join('') + '</div>';
}

function getMaterialType(wateringK) {
    if (wateringK <= 40) return 'Влагоудерживающий';
    if (wateringK <= 70) return 'Средний';
    return 'Высокое испарение';
}

function getMaterialTypeClass(wateringK) {
    if (wateringK <= 40) return 'category-low';
    if (wateringK <= 70) return 'category-medium';
    return 'category-high';
}

function getMaterialDescription(wateringK) {
    if (wateringK <= 40) return 'Хорошо удерживает влагу, требуется меньше поливов. Подходит для влаголюбивых растений.';
    if (wateringK <= 70) return 'Средняя проницаемость, стандартный режим полива. Универсальный вариант для большинства растений.';
    return 'Быстро испаряет влагу, требуется частый полив. Подходит для растений, не любящих застой воды, таких как суккуленты.';
}

function showMaterialDetails(id) {
    const material = allMaterials.find(m => m.id === id);
    if (!material) return;

    const modal = document.getElementById('details-modal');
    const content = document.getElementById('modal-details-content');

    content.innerHTML = `
        <h3>${material.name}</h3>
        <div class="modal-info-group">
            <div class="info-item">
                <span class="info-label">Тип:</span>
                <span class="info-value">${getMaterialType(material.wateringK)}</span>
            </div>
        </div>
        <div class="modal-section">
            <h4>Характеристика</h4>
            <p>${getMaterialDescription(material.wateringK)}</p>
        </div>
    `;

    modal.style.display = 'block';
}

function closeDetailsModal() {
    document.getElementById('details-modal').style.display = 'none';
}

window.onclick = function(event) {
    const modal = document.getElementById('details-modal');
    if (event.target === modal) {
        closeDetailsModal();
    }
}

function showError(message) {
    const errorDiv = document.getElementById('error');
    errorDiv.textContent = message;
    errorDiv.style.display = 'block';
    setTimeout(() => errorDiv.style.display = 'none', 5000);
}

function logout() {
    if (confirm('Вы уверены, что хотите выйти?')) {
        localStorage.removeItem('currentUserId');
        localStorage.removeItem('currentUserLogin');
        window.location.href = 'index.html';
    }
}
