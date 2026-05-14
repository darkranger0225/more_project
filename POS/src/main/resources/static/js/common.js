function showAlert(message, type = 'success') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.innerHTML = `
        <i class="bi ${type === 'success' ? 'bi-check-circle-fill' : type === 'error' ? 'bi-x-circle-fill' : 'bi-exclamation-circle-fill'}"></i>
        <span style="margin-left: 10px;">${message}</span>
    `;
    alertDiv.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        z-index: 99999;
        padding: 20px 40px;
        border-radius: 10px;
        color: white;
        font-size: 18px;
        font-weight: 500;
        background: ${type === 'success' ? 'linear-gradient(135deg, #27ae60, #2ecc71)' : type === 'error' ? 'linear-gradient(135deg, #e74c3c, #c0392b)' : 'linear-gradient(135deg, #f39c12, #e67e22)'};
        box-shadow: 0 10px 40px rgba(0,0,0,0.3);
        animation: alertFadeIn 0.3s ease;
        display: flex;
        align-items: center;
        min-width: 300px;
        text-align: center;
        justify-content: center;
    `;
    
    const style = document.createElement('style');
    style.textContent = `
        @keyframes alertFadeIn {
            from { opacity: 0; transform: translate(-50%, -50%) scale(0.8); }
            to { opacity: 1; transform: translate(-50%, -50%) scale(1); }
        }
    `;
    document.head.appendChild(style);
    document.body.appendChild(alertDiv);
    
    setTimeout(() => {
        alertDiv.style.animation = 'alertFadeIn 0.3s ease reverse';
        setTimeout(() => {
            alertDiv.remove();
            style.remove();
        }, 300);
    }, 2000);
}

function formatPrice(price) {
    return '¥' + parseFloat(price).toFixed(2);
}

function formatDateTime(dateStr) {
    const date = new Date(dateStr);
    return date.toLocaleString('zh-CN');
}

function getStatusText(status) {
    const statusMap = {
        'PENDING': '待处理',
        'COOKING': '制作中',
        'SERVED': '已上菜',
        'PAID': '已支付',
        'CANCELLED': '已取消',
        'FREE': '空闲',
        'OCCUPIED': '占用',
        'ON_SALE': '在售',
        'OFF_SALE': '下架',
        'ENABLED': '启用',
        'DISABLED': '禁用'
    };
    return statusMap[status] || status;
}

function getStatusBadgeClass(status) {
    const classMap = {
        'PENDING': 'badge-warning',
        'COOKING': 'badge-info',
        'SERVED': 'badge-success',
        'PAID': 'badge-success',
        'CANCELLED': 'badge-danger',
        'FREE': 'badge-success',
        'OCCUPIED': 'badge-danger',
        'ON_SALE': 'badge-success',
        'OFF_SALE': 'badge-warning',
        'ENABLED': 'badge-success',
        'DISABLED': 'badge-danger'
    };
    return classMap[status] || 'badge-info';
}

function confirmAction(message, callback) {
    showConfirm(message, callback);
}

function showConfirm(message, onConfirm, onCancel) {
    const overlay = document.createElement('div');
    overlay.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(0, 0, 0, 0.5);
        z-index: 99998;
        animation: fadeIn 0.2s ease;
    `;
    
    const dialog = document.createElement('div');
    dialog.innerHTML = `
        <div style="text-align: center; margin-bottom: 20px;">
            <i class="bi bi-question-circle" style="font-size: 48px; color: #667eea;"></i>
        </div>
        <div style="font-size: 18px; font-weight: 500; margin-bottom: 25px; text-align: center; color: #333;">${message}</div>
        <div style="display: flex; gap: 15px; justify-content: center;">
            <button class="btn-cancel" style="flex: 1; padding: 12px 30px; border: none; border-radius: 5px; background: #95a5a6; color: white; font-size: 16px; cursor: pointer; transition: all 0.2s;">取消</button>
            <button class="btn-confirm" style="flex: 1; padding: 12px 30px; border: none; border-radius: 5px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; font-size: 16px; cursor: pointer; transition: all 0.2s;">确定</button>
        </div>
    `;
    dialog.style.cssText = `
        position: fixed;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        z-index: 99999;
        background: white;
        padding: 30px 40px;
        border-radius: 15px;
        box-shadow: 0 10px 50px rgba(0, 0, 0, 0.3);
        min-width: 400px;
        animation: slideIn 0.3s ease;
    `;
    
    const style = document.createElement('style');
    style.textContent = `
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        @keyframes slideIn {
            from { opacity: 0; transform: translate(-50%, -50%) scale(0.8); }
            to { opacity: 1; transform: translate(-50%, -50%) scale(1); }
        }
        .btn-cancel:hover { background: #7f8c8d !important; transform: translateY(-2px); }
        .btn-confirm:hover { transform: translateY(-2px); box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4); }
    `;
    document.head.appendChild(style);
    document.body.appendChild(overlay);
    document.body.appendChild(dialog);
    
    const closeDialog = () => {
        dialog.style.animation = 'slideIn 0.15s ease reverse';
        overlay.style.animation = 'fadeIn 0.15s ease reverse';
        setTimeout(() => {
            overlay.remove();
            dialog.remove();
            style.remove();
        }, 150);
    };
    
    dialog.querySelector('.btn-cancel').onclick = () => {
        closeDialog();
        if (onCancel) onCancel();
    };
    
    dialog.querySelector('.btn-confirm').onclick = () => {
        closeDialog();
        if (onConfirm) onConfirm();
    };
    
    overlay.onclick = () => {
        closeDialog();
        if (onCancel) onCancel();
    };
}

async function apiGet(url) {
    const response = await fetch(url, {
        headers: {
            'Content-Type': 'application/json'
        }
    });
    return response.json();
}

async function apiPost(url, data) {
    const response = await fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    return response.json();
}

async function apiPut(url, data) {
    const response = await fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });
    return response.json();
}

async function apiDelete(url) {
    const response = await fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    });
    return response.json();
}

function logout() {
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/logout';
    const csrfInput = document.createElement('input');
    csrfInput.type = 'hidden';
    csrfInput.name = '_csrf';
    const csrfMeta = document.querySelector('meta[name="_csrf"]');
    csrfInput.value = csrfMeta ? csrfMeta.getAttribute('content') : '';
    form.appendChild(csrfInput);
    document.body.appendChild(form);
    form.submit();
}
