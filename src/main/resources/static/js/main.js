// Bookshop Green - Main JavaScript

console.log('🌿 Bookshop Green loaded!');

// Load categories into dropdown menu
function loadCategoryMenu() {
    const menu = document.getElementById('categoryMenu');
    if (!menu) return;

    fetch('/api/categories')
        .then(response => response.json())
        .then(categories => {
            categories.forEach(category => {
                const item = document.createElement('a');
                item.href = `/books.html?category=${encodeURIComponent(category)}`;
                item.className = 'dropdown-item';
                item.textContent = `📗 ${category}`;
                menu.appendChild(item);
            });
        })
        .catch(error => {
            console.error('Error loading categories:', error);
        });
}

// Handle dropdown menu toggle
function initDropdownMenus() {
    const dropdowns = document.querySelectorAll('.dropdown');

    dropdowns.forEach(dropdown => {
        const toggle = dropdown.querySelector('.dropdown-toggle');

        if (toggle) {
            // Toggle on click
            toggle.addEventListener('click', function (e) {
                e.preventDefault();
                e.stopPropagation();

                // Close other dropdowns
                dropdowns.forEach(other => {
                    if (other !== dropdown) {
                        other.classList.remove('active');
                    }
                });

                // Toggle current dropdown
                dropdown.classList.toggle('active');
            });
        }
    });

    // Close dropdown when clicking outside
    document.addEventListener('click', function (e) {
        if (!e.target.closest('.dropdown')) {
            dropdowns.forEach(dropdown => {
                dropdown.classList.remove('active');
            });
        }
    });
}

// Auto-dismiss alerts after 5 seconds
document.addEventListener('DOMContentLoaded', function () {
    // Load category menu
    loadCategoryMenu();

    // Initialize dropdown menus
    initDropdownMenus();

    // Auto-dismiss alerts
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.remove();
            }, 500);
        }, 5000);
    });
});
