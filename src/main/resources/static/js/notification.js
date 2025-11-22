// Bookshop Green - Notification System

/**
 * Show notification
 * @param {string} type - 'success', 'error', or 'info'
 * @param {string} title - Notification title
 * @param {string} message - Notification message
 * @param {number} duration - Duration in milliseconds (default: 5000)
 */
function showNotification(type, title, message, duration = 5000) {
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;

    // Set icon based on type
    let icon = '✓';
    if (type === 'error') icon = '✕';
    if (type === 'info') icon = 'ℹ';

    notification.innerHTML = `
        <div class="notification-icon">${icon}</div>
        <div class="notification-content">
            <div class="notification-title">${title}</div>
            <div class="notification-message">${message}</div>
        </div>
        <button class="notification-close" onclick="closeNotification(this)">×</button>
    `;

    // Add to body
    document.body.appendChild(notification);

    // Auto remove after duration
    setTimeout(() => {
        closeNotification(notification.querySelector('.notification-close'));
    }, duration);
}

/**
 * Close notification
 */
function closeNotification(button) {
    const notification = button.parentElement;
    notification.classList.add('hiding');

    setTimeout(() => {
        notification.remove();
    }, 300);
}

/**
 * Show success notification
 */
function showSuccess(title, message) {
    showNotification('success', title, message);
}

/**
 * Show error notification
 */
function showError(title, message) {
    showNotification('error', title, message);
}

/**
 * Show info notification
 */
function showInfo(title, message) {
    showNotification('info', title, message);
}
