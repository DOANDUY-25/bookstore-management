// Simple authentication UI handler
console.log('Auth module loaded');

// Check if user is logged in by checking session
async function checkAuth() {
    try {
        const response = await fetch('/api/user/check', {
            credentials: 'include'
        });

        if (response.ok) {
            const data = await response.json();
            if (data.authenticated) {
                showUserMenu(data);
            } else {
                showGuestMenu();
            }
        } else {
            showGuestMenu();
        }
    } catch (error) {
        console.error('Auth check error:', error);
        showGuestMenu();
    }
}

function showGuestMenu() {
    const authSection = document.getElementById('authSection');
    if (!authSection) return;

    authSection.innerHTML = `
        <a href="/login.html" class="nav-link">🔐 Đăng Nhập</a>
        <a href="/register.html" class="nav-link">✍️ Đăng Ký</a>
    `;
}

function showUserMenu(user) {
    const authSection = document.getElementById('authSection');
    if (!authSection) return;

    const isAdmin = user.role === 'ADMIN';

    authSection.innerHTML = `
        <div class="user-menu">
            <div class="user-trigger" onclick="toggleUserDropdown()">
                <span class="user-name">👤 ${user.fullName || user.username}</span>
                <span class="dropdown-arrow">▼</span>
            </div>
            <div class="user-dropdown" id="userDropdown">
                ${isAdmin ? `
                <a href="/admin/dashboard.html" class="dropdown-item">
                    <span class="icon">⚙️</span> Quản Trị
                </a>
                <div class="dropdown-divider"></div>
                ` : ''}
                <a href="/profile.html" class="dropdown-item">
                    <span class="icon">👤</span> Thông Tin
                </a>
                <a href="/orders.html" class="dropdown-item">
                    <span class="icon">📦</span> Đơn Hàng
                </a>
                <a href="/gift-orders.html" class="dropdown-item">
                    <span class="icon">🎁</span> Quà Tặng
                </a>
                <div class="dropdown-divider"></div>
                <a href="#" onclick="handleLogout(event)" class="dropdown-item logout">
                    <span class="icon">🚪</span> Đăng Xuất
                </a>
            </div>
        </div>
    `;
}

function toggleUserDropdown() {
    const dropdown = document.getElementById('userDropdown');
    if (dropdown) {
        dropdown.classList.toggle('show');
    }
}

function handleLogout(event) {
    if (event) event.preventDefault();

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/logout';
    document.body.appendChild(form);
    form.submit();
}

// Close dropdown when clicking outside
document.addEventListener('click', function (event) {
    const userMenu = document.querySelector('.user-menu');
    const dropdown = document.getElementById('userDropdown');

    if (dropdown && userMenu && !userMenu.contains(event.target)) {
        dropdown.classList.remove('show');
    }
});

// Initialize on page load
document.addEventListener('DOMContentLoaded', checkAuth);
