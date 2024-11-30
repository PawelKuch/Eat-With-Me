
https://www.bezkoder.com/spring-boot-jwt-authentication/


# Opis od Pawła:

## Encje

### User
- ID
- ExternalID
- Username
- Login
- Rola

### Zamówienie:
- ID
- ExternalID
- Organizator (user)
- Restauracja
- Rozliczone
- Data stworzenia zamówienia
- Data końca przyjmowania zamówień

### Pozycja zamówienia
- ID
- ExternalID
- Zamawiający (user)
- Nazwa dania
- Uwagi
- Cena

### Restauracja
- ID
- nazwa
- Adres
- Nr Telefonu
- Email

### Menu
- ID
- Nazwa dania
- Restauracja
- Cena


Tworzenie i dodawanie restauracji
Tworzenie menu
Tworzenie dań
Dania należą do menu
Z menu wybierać można Dania. Pojawia się dostępne menu w zależności od przypisanej do zamówienia restauracji

Moduł logowania

Po zalogowaniu wyświetlają się grupy juz utworzone na aktualny dzień z możliwością dodania nowej grupy.
Przy grupie wyświetla się nazwa grupy, ilość osób w grupie, restauracja i przybliżona godzina zamówienia/zamknięcia zamówienia.
Po dołączeniu do grupy wybiera się danie z menu oraz dodaje swoje uwagi.

Po naciśnięciu tworzenia nowej grupy formularz z nazwą grupy, restauracja, przewidywana godzina zamknięcia.

W zamówieniach twórca zamówienia może edytować przybliżoną godzinę zamknięcia.

Administrator aplikacji ma możliwość wyświetlania wszystkich restauracji w bazie, menu do nich przynależnych oraz dań należących do danego menu.
Ma możliwość edytowania restauracji, menu i dań.

Po kliknięciu w szczegóły zamówienia wyświetla się aktualna kwota zamówienia. Jeśli zamówienie jest zamknięte, wyświetla się informacja o największej kwocie danego pracownika.

Wszystkie zamówienia można sortować według daty, najwyższej kwoty zamówienia.

Właściciel/twórca grupy może wyświetlić szczegóły zamówienia, to znaczy pracowników i dania jakie wzięli, kwotę łączna dla danego pracownika oraz czy damy pracownik się rozliczył. W szczegółach powinna być tez łączna kwota zamówienia.

Panel statystyk. Wyświetlają się informacje o najwyższej kwocie zamówienia pracownika dla danego dnia, największa całkowita kwota dla danej grupy. Dzień z największa ilością zamówień oraz dokładna liczba.

Pracownik również ma możliwość wejścia w historie swoich zamówień oraz grup z wyszczególnieniem kwoty dla niego oraz rekordowej wartości zamówienia, a także całkowitej sumy wydanej na jedzenie