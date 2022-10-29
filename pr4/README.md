# РКСП 4 практика

Темой является приложение бронирование билетов оператором в аэропорту

Обящательно запустить бд на порту 5435 или сменить в настройках сервера порт бд

## Авторизация

`login --username user --password pass`

## Методы

### Request-Response
- Забронировать место (`book --ticket-id 1 --passenger-id 2`)

### Request-Stream
- Получить список всех рейсов (`flights`)

### Channel
- По id рейсов узнать их места (`tickets --flight-ids "3 4"`)

### Fire-and-Forget
- Удалить бронь с места в рейсе по id места (`delete-reservation --ticket-id 1`)
