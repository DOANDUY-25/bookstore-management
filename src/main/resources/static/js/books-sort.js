// Books Sorting and Loading Functions

function loadBooksWithSort() {
    const sortSelect = document.getElementById('sortSelect');
    const sortValue = sortSelect ? sortSelect.value : '';

    let sortBy = '';
    let sortOrder = 'asc';

    if (sortValue) {
        const parts = sortValue.split('-');
        sortBy = parts[0];
        sortOrder = parts[1] || 'asc';
    }

    // Get category if exists
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category');

    // Build API URL
    let apiUrl = '/api/books';
    const params = [];

    if (sortBy) {
        params.push(`sortBy=${sortBy}`);
        params.push(`sortOrder=${sortOrder}`);
    }
    if (category) {
        params.push(`category=${encodeURIComponent(category)}`);
    }

    if (params.length > 0) {
        apiUrl += '?' + params.join('&');
    }

    // Fetch books
    fetch(apiUrl, {
        credentials: 'include'
    })
        .then(response => response.json())
        .then(books => {
            // API returns array directly, not wrapped object
            displayBooks(books);
        })
        .catch(error => {
            console.error('Error loading books:', error);
            displayBooks([]);
        });
}

function displayBooks(books) {
    const booksGrid = document.getElementById('booksGrid');
    if (!booksGrid) return;

    if (!books || books.length === 0) {
        booksGrid.innerHTML = '<p style="text-align: center; grid-column: 1/-1;">Không tìm thấy sách nào.</p>';
        return;
    }

    booksGrid.innerHTML = books.map(book => `
        <div class="book-card">
            <img loading="lazy" src="${book.coverImageURL || 'https://via.placeholder.com/300x400?text=No+Image'}" alt="${book.title}">
            <div class="book-card-body">
                <h3 class="book-title">${book.title}</h3>
                <p class="book-author">${book.author}</p>
                <p class="book-price">${(book.price || 0).toLocaleString('vi-VN')} ₫</p>
                <div style="display: flex; gap: 0.5rem; flex-direction: column;">
                    <a href="/book-detail.html?id=${book.bookId}" class="btn btn-primary btn-sm">Xem Chi Tiết</a>
                    ${book.stock > 0 ? `<button onclick="addToCart(${book.bookId})" class="btn btn-outline btn-sm">🛒 Thêm Vào Giỏ</button>` : '<button class="btn btn-outline btn-sm" disabled>Hết hàng</button>'}
                </div>
            </div>
        </div>
    `).join('');
}

// Load books when page loads
document.addEventListener('DOMContentLoaded', function () {
    // Check if we're on index.html or books.html
    const isIndexPage = window.location.pathname === '/' || window.location.pathname.endsWith('index.html');

    if (isIndexPage) {
        // On index page, load only first 8 books without sorting
        fetch('/api/books', {
            credentials: 'include'
        })
            .then(response => response.json())
            .then(books => {
                displayBooks(books.slice(0, 8));
            })
            .catch(error => {
                console.error('Error loading books:', error);
                const grid = document.getElementById('booksGrid');
                if (grid) {
                    grid.innerHTML = '<p class="alert alert-info">Không thể tải danh sách sách</p>';
                }
            });
    } else {
        // On books page, check if there's a search query first
        const urlParams = new URLSearchParams(window.location.search);
        const searchQuery = urlParams.get('query') || urlParams.get('search');

        // Only load books if there's no search query (search will be handled by books.html script)
        if (!searchQuery) {
            loadBooksWithSort();
        }
    }
});
