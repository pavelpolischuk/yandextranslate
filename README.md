# Yandex.Translate Sample

Пример использования сервиса Яндекс.Переводчик для перевода слов и текстов, c ведением истории и закладок.

### Architecture

Всё приложение работает в одной активности. Разделение функциональности происходит по фрагментам. 

##### Fragments

Есть 3 основных: 
- `TranslateFragment` для перевода,
- `BookmarksFragment` для работы с закладками,
- `InfoFragment` для информации о приложении;

И 1 дополнительный `TranslateRootFragment` для получения списка языков и старта `TranslateFragment`, если список успешно получен.

Фрагменты осуществляют процесс взаимодействия с пользователем, дёргают сервисы для получения и сохранения необходимых данных.

##### Services

Подключение к API Яндекса находится в пакете `api`, там есть DTO классы для получения данных и сервис `YandexService` выполняющий работу. Он использует библиотеку `Retrofit2` (которая делает HTTP-запрос с помощью `OkHttp`, получает ответ, парсит и выдает заполненый класс в качестве результата). Ответ она умеет преобразовывать к `Observable`.

Есть ещё сервисы `TranslateService` для перевода и `HistoryService` для ведения работы с базой и сохранения истории и закладок.

##### RxJava

Вообще вся работа приложения осуществляется через `Observable'ы` (`RxJava`, `RxBindings`), мы создаем потоки событий на основе действий пользователя, преобразуем их, фильтруем и подписываемся для совершения нужных действий и вывода результата. Настравиваются они при создании View фрагмента. Также RxJava я использовал для обновления списка при изменении данных в истории (типа сообщений).

### For future

- В данный момент все сервисы сделаны синглтонами и создаются при первом обращении, а надо чтобы для каждого был интерфейс и как зависимости внедрялись во фрагменты или даже в `Presenter'ы` (которые будут управлять логикой вьюх). 
- Тогда можно написать нормальные тесты, внедряя в качестве зависимостей моки интерфейсов, настраивая логику их работы как надо. Те тесты, что есть сейчас проверяют маленькую функциональность (нынешнее построение приложения не позволяет его нормальн протестировать).
