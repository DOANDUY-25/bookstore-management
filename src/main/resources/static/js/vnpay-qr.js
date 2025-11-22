// VNPay QR Code Payment Functions

let vnpayQRCode = null;
let isGiftOrder = false; // Track if this is a gift order payment

function showVNPayQRModal(orderId, amount, isGift = false) {
    console.log('showVNPayQRModal called with:', { orderId, amount, isGift });

    isGiftOrder = isGift;

    // Ensure amount is a valid number
    const validAmount = amount || 0;

    // Generate fake QR data for demo
    const qrData = `VNPAY|${orderId}|${validAmount}|Bookshop Green|${Date.now()}`;
    const transactionId = (isGift ? 'VNP-GIFT-' : 'VNP') + Date.now();

    const paymentData = {
        orderId: orderId,
        amount: validAmount,
        transactionId: transactionId,
        qrData: qrData
    };

    displayVNPayQR(paymentData);
}

function displayVNPayQR(paymentData) {
    console.log('displayVNPayQR called with:', paymentData);

    // Update modal info
    document.getElementById('vnpayQROrderId').textContent = paymentData.orderId;
    document.getElementById('vnpayQRAmount').textContent = paymentData.amount.toLocaleString('vi-VN') + ' ₫';
    document.getElementById('vnpayQRRefCode').textContent = paymentData.transactionId;

    // Clear previous QR code
    const qrContainer = document.getElementById('vnpayQRCode');
    qrContainer.innerHTML = '';

    // Check if QRCode library is loaded
    if (typeof QRCode !== 'undefined') {
        // Generate QR Code
        vnpayQRCode = new QRCode(qrContainer, {
            text: paymentData.qrData,
            width: 200,
            height: 200,
            colorDark: "#000000",
            colorLight: "#ffffff",
            correctLevel: QRCode.CorrectLevel.H
        });
    } else {
        // Fallback if QRCode library not loaded
        qrContainer.innerHTML = '<div style="width: 200px; height: 200px; background: #f0f0f0; display: flex; align-items: center; justify-content: center; border: 2px dashed #ccc;"><p style="text-align: center; color: #666;">QR Code<br/>Demo</p></div>';
    }

    // Show modal
    document.getElementById('vnpayQRModal').style.display = 'block';
    document.body.style.overflow = 'hidden';

    // Store transaction ID for confirmation
    window.currentVNPayTransaction = paymentData.transactionId;
    window.currentVNPayOrderId = paymentData.orderId;
    window.currentVNPayAmount = paymentData.amount;
}

function closeVNPayQRModal() {
    document.getElementById('vnpayQRModal').style.display = 'none';
    document.body.style.overflow = 'auto';
    window.currentVNPayTransaction = null;
    window.currentVNPayOrderId = null;
}

function confirmVNPayQRPayment() {
    if (!window.currentVNPayOrderId || !window.currentVNPayTransaction) {
        showError('Lỗi', 'Thông tin thanh toán không hợp lệ');
        return;
    }

    // Show processing
    showSuccess('Đang xử lý...', 'Đang xác nhận thanh toán...');

    // Determine API endpoint based on order type
    const apiEndpoint = isGiftOrder
        ? `/payment/gift-order/vnpay/confirm/${window.currentVNPayOrderId}`
        : `/payment/vnpay/confirm/${window.currentVNPayOrderId}`;

    // Simulate VNPay API call
    setTimeout(() => {
        fetch(apiEndpoint, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                transactionId: window.currentVNPayTransaction,
                amount: window.currentVNPayAmount || 0
            }),
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    closeVNPayQRModal();
                    showSuccess('Thanh Toán Thành Công!', isGiftOrder ? 'Đơn quà tặng đã được thanh toán qua VNPay.' : 'Đơn hàng đã được thanh toán qua VNPay.');
                    setTimeout(() => {
                        if (typeof loadOrders === 'function') {
                            loadOrders();
                        } else if (typeof loadGiftOrders === 'function') {
                            loadGiftOrders();
                        } else {
                            window.location.reload();
                        }
                    }, 2000);
                } else {
                    showError('Lỗi', data.message || 'Xác nhận thanh toán thất bại');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('Lỗi', 'Không thể xác nhận thanh toán');
            });
    }, 1500); // Simulate network delay
}

// Close modal when clicking outside
document.addEventListener('click', function (e) {
    const vnpayQRModal = document.getElementById('vnpayQRModal');
    if (e.target === vnpayQRModal) {
        closeVNPayQRModal();
    }
});
