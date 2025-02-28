## Lend-Loop

#### Веб-решение для обмена вещами в аренду, состоящее из микросервисов, взаимодействующих через HTTP-запросы.

![](https://github.com/mynameisSergey/Lend-Loop/blob/main/img/%D0%A0%D0%B8%D1%81%D1%83%D0%BD%D0%BE%D0%BA.png)

#### Основные функции:
* Создание запросов на аренду вещей с указанием характеристик и срока.
* Поиск предложений от других пользователей.
* Добавление вещей с описанием и фотографиями.
* Сообщество взаимопомощи для обмена ресурсами и экономии средств.
* Уведомления о новых запросах и предложениях.
* Возможность оставлять отзывы и оценки после аренды.

#### Используемые технологии и инструменты:
* Java 11, Spring Boot, Spring Data JPA, Hibernate, Docker, JUnit, Mockito

### Каркас приложения
### Вещь
Основная сущность сервиса — **вещь**. В коде она фигурирует как Item.
Пользователь, который добавляет в приложение новую вещь, считается её **владельцем**. При добавлении вещи есть возможность указать её краткое название и добавить небольшое описание. Также у вещи есть **статус** — доступна ли она для аренды. Статус проставляет владелец.
### Бронирование
Для поиска вещей организован **поиск**. Чтобы воспользоваться нужной вещью, её требуется забронировать. Бронирование (Booking) — ещё одна важная сущность приложения. Бронируется вещь всегда на определённые даты. Владелец вещи обязательно должен подтвердить бронирование.
После того как вещь возвращена, у пользователя, который её арендовал, есть возможность оставить отзыв.
### Запрос вещи
Еще одна из сущностей - запрос вещи. Пользователь создаёт запрос если нужная ему вещь не найдена при поиске. В запросе указывается, что именно он ищет. В ответ на запрос другие пользовали могут добавить нужную вещь.
