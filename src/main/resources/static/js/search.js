// Bookshop Green - Search Module

console.log('🔍 Search module loaded');

let categories = [];



/**
 * Perform search
 */
function performSearch() {
    const query = document.getElementById('searchQuery').value.trim();

    // Build search URL
    if (query) {
        window.location.href = '/books.html?search=' + encodeURIComponent(query);
    } else {
        window.location.href = '/books.html';
    }
}

/**
 * Handle search form submission
 */
function handleSearchSubmit(event) {
    if (event) {
        event.preventDefault();
    }
    performSearch();
}

/**
 * Handle Enter key in search input
 */
function handleSearchKeyPress(event) {
    if (event.key === 'Enter') {
        event.preventDefault();
        performSearch();
    }
}

// Initialize search on page load
document.addEventListener('DOMContentLoaded', function () {
    // Add event listeners
    const searchForm = document.getElementById('searchForm');
    if (searchForm) {
        searchForm.addEventListener('submit', handleSearchSubmit);
    }

    const searchQuery = document.getElementById('searchQuery');
    if (searchQuery) {
        searchQuery.addEventListener('keypress', handleSearchKeyPress);
    }
});
