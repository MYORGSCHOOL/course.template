## Описание

Фул стек приложение - справочник документов

## Структура

- `backend` - Бэкенд на java.
- `ui` - Фронтенд на react + redux.
- `data base` - postgresql

## Подготовка

Установите:

- [node](https://nodejs.org) - front
- [openjdk](https://openjdk.java.net) 15 - java бэк
- [docker-desktop](https://www.docker.com/) - инструмент для управления контейнерами.
- [offset Explorer](https://www.kafkatool.com/) - для отправки ответа о принятии или отклонении документа

## Запуск

### Запуск фронта

```
./gradlew ui:npm_run_start
```
### Запуск бэка
```
Через файл Application
```
### Запуск kafka и zookeeper
```
Через файл docker-compose.yml
```
## Использование
### Адрес страницы
```
http://localhost:9000/#/
```
Отправка документа осушествляется в топик "documentIn".
После отправки через приложение "Offset Explorer" отправляем сообщение в топик "documentOut".
Пример сообщения: 
```
{
"id": 1,
"status":"ACCEPTED" // или DECLINED
}
```
