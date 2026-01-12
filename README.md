# Дневник полива растений
## Проект для итоговой работы по дисциплине "Язык программирования Java"
Проект представляет из себя Spring Boot Web MVC приложение
для учёта полива комнатных растений с расчётом рекомендуемого объёма воды 
на основе параметров растения и условий микроклимата.

## Структура
    ├───java
    │   └───tsygvintsev
    │       └───watering_diary
    │           │   WateringDiaryApplication.java      # главный класс
    │           │
    │           ├───controller
    │           │       ConditionsController.java      # контроллер микроклимата
    │           │       MaterialController.java        # контроллер материалов
    │           │       PlantTypeController.java       # контроллер растений
    │           │       UserController.java            # контроллер пользователей
    │           │       UserPlantController.java       # контроллер растений пользователей
    │           │       WateringRecordController.java  # контроллер дневника поливов
    │           │
    │           ├───dto
    │           │       ErrorResponse.java             # класс персонализации сообщений об ошибке
    │           │
    │           ├───entity
    │           │       Conditions.java                # класс с сущностью микроклимата
    │           │       Material.java                  # класс с сущностью материала
    │           │       PlantType.java                 # класс с сущностью растений
    │           │       User.java                      # класс с сущностью пользователя
    │           │       UserPlant.java                 # класс с сущностью растения пользователя
    │           │       WateringRecord.java            # класс с сущностью записи о поливе
    │           │
    │           ├───exception
    │           │       GlobalExceptionHandler.java    # класс обработчика сообщения об ошибке
    │           │
    │           ├───repository
    │           │       ConditionsRepository.java      # репозиторий для работы с микроклиматом
    │           │       MaterialRepository.java        # репозиторий для работы с материалами
    │           │       PlantTypeRepository.java       # репозиторий для работы с растениями
    │           │       UserPlantRepository.java       # репозиторий для работы с растениями пользователей
    │           │       UserRepository.java            # репозиторий для работы с пользователями
    │           │       WateringRecordRepository.java  # репозиторий для работы с записями полива
    │           │
    │           ├───seeder
    │           │       ReferencesSeeder.java          # сидер для заполнения БД при запуске
    │           │
    │           ├───service
    │           │       ConditionsService.java         # бизнес-логика для работы с микроклиматами
    │           │       MaterialService.java           # бизнес-логика для работы с материалами
    │           │       PlantTypeService.java          # бизнес-логика для работы с растениями
    │           │       UserPlantService.java          # бизнес-логика для работы с растениями пользователей
    │           │       UserService.java               # бизнес-логика для работы с пользователями
    │           │       WateringRecordService.java     # бизнес-логика для работы с записями полива
    │           │
    │           └───util
    │                   WateringRecordExcelExporter.java  # вспомогательный класс для экспорта Excel
    │
    └───resources
        │   application.properties                     # конфигурация
        │
        └───static
            │   conditions.html
            │   diary.html
            │   favicon.ico
            │   index.html
            │   plants.html
            │   profile.html
            │   references.html
            │   register.html
            │
            ├───css
            │       style.css
            │
            └───js
                    auth.js
                    conditions.js
                    diary.js
                    plants.js
                    profile.js
                    references.js

## Функицональность
- Просмотр справочника растений и материалов для горшков
- Просмотр/добавление/изменение/удаление своих растений
- Просмотр/добавление/изменение/удаление условий микроклимата помещения
- Просмотр/добавление/изменение/удаление записи о поливе растения
- Поиск по названию, диапазону дат
- Авторизация и регистрация
- Изменения данных профиля
- Валидация вводимых данных

## Требования
Для запуска проекта Вам потребуется установить следующий программные средства:
- Java 17+
- PostgreSQL 17+

## Установка

1. Клонируйте репозиторий в новую директорию:

```bash
git clone https://github.com/M1L0ne/Watering-diary-PSU-project.git
```
2. Перейдите в директорию проекта:
```bash
cd watering_diary
```
3. Создайте файл .env и задайте в нём параметры для подключения к базе данных:
```bash
DB_USERNAME=username
DB_PASSWORD=password
DB_URL=jdbc:postgresql://localhost:5432/watering_diary
```

4. Запустите проект из среды разработки или соберите проект в JAR-файл:
```bash
./gradlew clean bootJar
```
Приложение будет доступно по адресу http://localhost:8080 
