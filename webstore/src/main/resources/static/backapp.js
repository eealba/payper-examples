// Backoffice JS moved from backoffice.html

// Render orders into the table
function renderOrdersTable(orders, actionsEnabled) {
    const table = document.getElementById('ordersTable');
    const tbody = document.getElementById('ordersTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';
    if (!orders || orders.length === 0) {
        tbody.innerHTML = '<tr><td colspan="12" class="text-center">No orders found.</td></tr>';
    } else {
        orders.forEach(order => {
            const reference = (order.referenceId && (order.referenceId.value || order.referenceId)) || '';
            const paymentIntent = order.paymentIntent || '';
            tbody.innerHTML += `
                <tr id="order-row-${reference}">
                    <td>${order.createTime ? new Date(order.createTime).toLocaleString() : ''}</td>
                    <td id="order-status-${reference}">${order.status || ''}</td>
                    <td>${reference}</td>
                    <td>${order.id ? (order.id.value || order.id) : ''}</td>
                    <td>${order.paymentProvider || ''}</td>
                    <td>${order.externalOrderId || ''}</td>
                    <td>${order.externalAuthorizationId || ''}</td>
                    <td>${order.product ? (order.product.id || '') : ''}</td>
                    <td>${order.product && (order.product.price != null) ? order.product.price : ''}</td>
                    <td>${order.paymentMethod ? (order.paymentMethod.type || '') : ''}</td>
                    <td>${paymentIntent}</td>
                    <td>
                    ${actionsEnabled && paymentIntent === 'AUTHORIZE' ? 
                        ('<button class="btn btn-sm btn-success" data-capture-ref="' + reference + '">Capture</button>') 
                        :
                        ''}
                    </td>
                </tr>
            `;
        });
    }
    if (table) table.style.display = '';
}

// Fetch orders (current list)
function fetchBackofficeOrders() {
    fetch('/api/backoffice/orders')
        .then(response => response.json())
        .then(orders => renderOrdersTable(orders, true))
        .catch(error => {
            alert('Error retrieving orders.');
            console.error(error);
        });
}

// Fetch orders history
function fetchBackofficeOrdersHistory() {
    fetch('/api/backoffice/orders/history')
        .then(response => response.json())
        .then(orders => renderOrdersTable(orders, false))
        .catch(error => {
            alert('Error retrieving orders history.');
            console.error(error);
        });
}

// Capture an authorized payment for a given reference
function captureAuthorization(referenceId) {
    if (!referenceId) return;
    const btn = document.querySelector('#order-row-' + referenceId + ' button[data-capture-ref]');
    // fallback to any button in the row
    const fallbackBtn = document.querySelector('#order-row-' + referenceId + ' button');
    const buttonToDisable = btn || fallbackBtn;
    if (buttonToDisable) buttonToDisable.disabled = true;

    const endpoint = '/api/payments/authorizations/' + encodeURIComponent(referenceId) + '/capture';
    fetch(endpoint, { method: 'POST', headers: { 'Content-Type': 'application/json' } })
        .then(r => { if (!r.ok) throw new Error('Failed to capture authorized payment'); return r.json(); })
        .then(data => {
            const statusCell = document.getElementById(`order-status-${referenceId}`);
            if (statusCell && data.status) {
                statusCell.textContent = data.status;
            }
            alert('Payment captured successfully.');
            const actionCell = document.querySelector(`#order-row-${referenceId} td:last-child`);
            if (actionCell) actionCell.innerHTML = '';
        })
        .catch(err => {
            alert('Error capturing payment.');
            console.error(err);
            if (buttonToDisable) buttonToDisable.disabled = false;
        });
}

// Expose globally in case inline handlers or other scripts call it
window.captureAuthorization = captureAuthorization;

// Wire buttons and event delegation after DOM is ready
document.addEventListener('DOMContentLoaded', function() {
    const getBtn = document.getElementById('getOrdersBtn');
    if (getBtn) getBtn.addEventListener('click', fetchBackofficeOrders);
    const historyBtn = document.getElementById('getOrdersHistoryBtn');
    if (historyBtn) historyBtn.addEventListener('click', fetchBackofficeOrdersHistory);

    // Delegate clicks on capture buttons inside tbody
    const tbody = document.getElementById('ordersTableBody');
    if (tbody) {
        tbody.addEventListener('click', function(e) {
            const btn = e.target.closest('button[data-capture-ref]');
            if (btn) {
                const ref = btn.getAttribute('data-capture-ref');
                if (ref) captureAuthorization(ref);
            }
        });
    }
});
