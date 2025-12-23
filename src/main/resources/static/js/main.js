const API_BASE = 'http://localhost:8080/api';

async function searchEvents() {
    const query = document.getElementById('searchInput').value.trim();
    const url = query ? `${API_BASE}/search?criteria=${encodeURIComponent(query)}` : `${API_BASE}/search`;

    document.getElementById('loading').style.display = 'block';
    document.getElementById('eventsGrid').innerHTML = '';
    document.getElementById('noResults').style.display = 'none';

    try {
        const response = await fetch(url);
        const events = await response.json();

        document.getElementById('loading').style.display = 'none';

        if (events.length === 0) {
            document.getElementById('noResults').style.display = 'block';
            return;
        }

        const grid = document.getElementById('eventsGrid');
        events.forEach(event => {
            const card = document.createElement('div');
            card.className = 'event-card';
            card.onclick = () => window.location.href = `event.html?id=${event.id}`;

            const imgSrc = event.posterUrl || 'images/placeholder.jpg';

            card.innerHTML = `
                <img src="${imgSrc}" alt="${event.title}">
                <div class="info">
                    <h3>${event.title}</h3>
                    <p><strong>Артист:</strong> ${event.artist?.name || 'Неизвестно'}</p>
                    <p><strong>Город:</strong> ${event.venue?.city || 'Не указан'}</p>
                    <p><strong>Дата:</strong> ${formatDate(event.eventDate)}</p>
                </div>
            `;
            grid.appendChild(card);
        });
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('loading').style.display = 'none';
        document.getElementById('noResults').innerHTML = '<p>Ошибка сервера</p>';
        document.getElementById('noResults').style.display = 'block';
    }
}

async function loadEventDetails(id) {
    try {
        const response = await fetch(`${API_BASE}/events/${id}`);
        const event = await response.json();

        const detail = document.getElementById('eventDetail');
        const imgSrc = event.posterUrl || 'images/placeholder.jpg';

        detail.innerHTML = `
            <img src="${imgSrc}" alt="${event.title}">
            <h2>${event.title}</h2>
            <div class="meta">
                <p><strong>Дата:</strong> ${formatDate(event.eventDate)}</p>
                <p><strong>Статус:</strong> ${event.status === 'published' ? 'Активно' : 'Завершено'}</p>
            </div>
            ${event.description ? `<div class="description">${event.description}</div>` : ''}

            <div class="artist-info">
                <h3>Исполнитель</h3>
                <p><strong>Имя:</strong> ${event.artist?.name || 'Не указано'}</p>
                ${event.artist?.genre ? `<p><strong>Жанр:</strong> ${event.artist.genre}</p>` : ''}
                ${event.artist?.description ? `<p>${event.artist.description}</p>` : ''}
            </div>

            <div class="venue-info">
                <h3>Площадка</h3>
                <p><strong>Название:</strong> ${event.venue?.name || 'Не указано'}</p>
                <p><strong>Город:</strong> ${event.venue?.city || 'Не указано'}</p>
                ${event.venue?.address ? `<p><strong>Адрес:</strong> ${event.venue.address}</p>` : ''}
                ${event.venue?.totalCapacity ? `<p><strong>Вместимость:</strong> ${event.venue.totalCapacity}</p>` : ''}
            </div>
        `;
    } catch (error) {
        console.error('Ошибка:', error);
        document.getElementById('eventDetail').innerHTML = '<p>Не удалось загрузить мероприятие</p>';
    }
}

function formatDate(isoString) {
    const date = new Date(isoString);
    return date.toLocaleDateString('ru-RU', {
        day: '2-digit',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

async function generateReport() {
    const start = document.getElementById('startDate').value;
    const end = document.getElementById('endDate').value;

    if (!start || !end) {
        alert('Выберите обе даты!');
        return;
    }

    const url = `${API_BASE}/report?startDate=${start}&endDate=${end}`;

    try {
        const response = await fetch(url);
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || response.statusText);
        }

        const blob = await response.blob();
        const downloadUrl = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = downloadUrl;
        a.download = 'events_report.xlsx';
        document.body.appendChild(a);
        a.click();
        a.remove();
        window.URL.revokeObjectURL(downloadUrl);
    } catch (error) {
        console.error('Ошибка:', error);
        alert('Не удалось сгенерировать отчёт: ' + error.message);
    }
}