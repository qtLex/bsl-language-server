Процедура Тест1()

    Запрос = Новый Запрос;
    Запрос.Текст = "Выбрать Т.Ссылка Из Справочник.Справочник1 СПр Левое соединение РегистрСведений.Курсы.СрезПоследних КАК Т По СПр.Поле1 = Т.Валюта"; //<-- ошибка

КонецПроцедуры

Процедура Тест2()

    Запрос = Новый Запрос;
    Запрос.Текст = "Выбрать Т.Измерение Из Справочник.Справочник1
    |СПр Левое соединение
    |РегистрНакопления.Склады.Остатки(Склад = &Параметр) КАК Т //<-- ошибка
    |По СПр.Поле1 = Т.Местонахождение";

КонецПроцедуры

Процедура Тест3()

    Запрос = Новый Запрос;
    Запрос.Текст = "Выбрать Т.Регистратор Из Справочник.Справочник1
    |СПр Правое соединение
    |РегистрНакопления.Склады.Остатки(Склад = &Параметр) КАК Т //<-- ошибка
    |По СПр.Поле1 = Т.Местонахождение";

КонецПроцедуры

Процедура Тест4()

    Запрос = Новый Запрос;
    Запрос.Текст = "Выбрать Т.Измерение
    | Из РегистрСведений.Курсы.СрезПоследних(&Период) как Курсы Левое соединение //<-- ошибка

    |РегистрНакопления.Склады.Остатки(Склад = &Параметр) КАК Т //<-- ошибка
    // комментарий
    |По Курсы.Поле1 = Т.Измерение";

КонецПроцедуры

Процедура Тест7()

    Запрос = Новый Запрос;
    Запрос.Текст = "Выбрать Т.Ссылка
    | Из РегистрНакопления.Склады.Остатки(Склад = &Параметр)  как Р,
    |(Выбрать СС.Ссылка Из Справочник.Справочник2 КАК СС Где СС.Ссылка = &Параметр) КАК Т";

КонецПроцедуры
