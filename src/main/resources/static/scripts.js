// Funkcja do pobierania książek
async function fetchBooks() {
    try {
        const response = await fetch('/api/books');
        if (!response.ok) {
            throw new Error('Nie udało się pobrać książek.');
        }
        const books = await response.json();
        const bookList = document.getElementById('book-list');
        bookList.innerHTML = ''; // Wyczyść listę przed dodaniem nowych elementów

        books.forEach(book => {
            const li = document.createElement('li');
            li.textContent = `${book.title} autorstwa ${book.author}`;
            bookList.appendChild(li);
        });
    } catch (error) {
        console.error('Błąd:', error);
    }
}

// Funkcja do obsługi rejestracji
document.getElementById('registration-form').addEventListener('submit', async function(event) {
    event.preventDefault(); // Zatrzymaj domyślną akcję formularza

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirm-password').value;
    const messageDiv = document.getElementById('registration-message');

    if (password !== confirmPassword) {
        messageDiv.textContent = 'Hasła nie pasują!';
        return;
    }

    // Zapisz nowego użytkownika (tutaj możesz dodać logikę zapisywania do bazy danych)
    try {
        const response = await fetch('/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            messageDiv.textContent = 'Rejestracja zakończona sukcesem!';
            document.getElementById('registration-form').reset(); // Wyczyść formularz
        } else {
            messageDiv.textContent = 'Wystąpił problem z rejestracją.';
        }
    } catch (error) {
        console.error('Błąd:', error);
        messageDiv.textContent = 'Błąd serwera.';
    }
});

// Wywołaj funkcję do pobrania książek przy załadowaniu strony
document.addEventListener('DOMContentLoaded', fetchBooks);
