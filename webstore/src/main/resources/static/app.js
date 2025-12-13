let productsData = [];
let selectedProduct = null; // holds the currently selected product object

function getLabelForIntent(intent) {
    if (!intent) return 'Pay';
    switch ((intent || '').toString().toUpperCase()) {
        case 'CAPTURE':
            return 'Buy';
        case 'AUTHORIZE':
            return 'Authorize';
        default:
            return 'Pay';
    }
}


function renderProducts(products) {
    const productsDiv = document.getElementById('products');
    productsDiv.innerHTML = '';
    products.forEach(product => {
        productsDiv.innerHTML += `
            <div class="col-md-4 mb-4">
                <div class="card h-100">
                    <img src="${product.img}" class="card-img-top" alt="${product.title}">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${product.title}</h5>
                        <p class="card-text">${product.description}</p>
                        <div class="mt-auto">
                            <p class="fw-bold">Price: €${product.price}</p>
                            <button class="btn btn-primary" data-buy-id="${product.id}">${getLabelForIntent(product.paymentIntent)}</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    });
}

function showCheckoutById(id) {
    const product = productsData.find(p => p.id === id);
    if (!product) return;
    selectedProduct = product; // set the selected product for later use
    document.getElementById('main-products').style.display = 'none';
    document.getElementById('checkout-title').textContent = product.title;
    document.getElementById('checkout-price').textContent = 'Price: €' + product.price;
    document.getElementById('checkout-img').src = product.img;

    // Set checkout button label based on paymentIntent
    const checkoutPayBtn = document.getElementById('checkout-pay-btn');
    if (checkoutPayBtn) {
        checkoutPayBtn.textContent = getLabelForIntent(product.paymentIntent);
    }

    // Also ensure the final pay button in the payment form has the same label
    const payBtn = document.getElementById('pay-btn');
    if (payBtn) {
        payBtn.textContent = getLabelForIntent(product.paymentIntent);
    }

    document.getElementById('main-checkout').style.display = 'block';
}

function cancelCheckout() {
    selectedProduct = null;
    document.getElementById('main-checkout').style.display = 'none';
    document.getElementById('main-products').style.display = 'flex';
}

function pay() {
    // Hide checkout and show payment panel
    document.getElementById('checkout').style.display = 'none';
    document.getElementById('payment-panel').style.display = 'block';

    // Ensure final pay button label matches selected product intent
    const payBtn = document.getElementById('pay-btn');
    if (payBtn) {
        const intent = selectedProduct ? selectedProduct.paymentIntent : null;
        payBtn.textContent = getLabelForIntent(intent);
    }
}

function showPaymentForm() {
    pay();
}

function cancelPaymentForm() {
    // Hide payment panel and show checkout again
    document.getElementById('payment-panel').style.display = 'none';
    document.getElementById('checkout').style.display = 'block';
}

document.addEventListener('DOMContentLoaded', function() {
    // Show/hide card fields depending on selected method
    const paypalRadio = document.getElementById('pay-method-paypal');
    const cardRadio = document.getElementById('pay-method-card');
    const cardFields = document.getElementById('card-fields');
    function toggleCardFields() {
        if (cardRadio && cardRadio.checked) {
            cardFields.style.display = 'block';
        } else if (cardFields) {
            cardFields.style.display = 'none';
        }
    }
    if (paypalRadio && cardRadio) {
        paypalRadio.addEventListener('change', toggleCardFields);
        cardRadio.addEventListener('change', toggleCardFields);
    }
    toggleCardFields();

    // Delegate Buy button clicks to the products container
    const productsContainer = document.getElementById('products');
    if (productsContainer) {
        productsContainer.addEventListener('click', function(e) {
            const btn = e.target.closest('button[data-buy-id]');
            if (btn) {
                const id = btn.getAttribute('data-buy-id');
                showCheckoutById(id);
            }
        });
    }
});

function submitPayment() {
    // Collect form data
    const deliveryAddress = {
        address: document.getElementById('address').value.trim(),
        city: document.getElementById('city').value.trim(),
        zip: document.getElementById('zip').value.trim(),
        country: document.getElementById('country').value
    };
    const method = document.querySelector('input[name="pay-method"]:checked').value;
    const product = selectedProduct || productsData.find(p => p.title === document.getElementById('checkout-title').textContent);
    if (!product) {
        alert('No product selected.');
        return;
    }
    const productId = product.id;
    const paymentIntent = product.paymentIntent || 'CAPTURE';
    let paymentMethod = { type: method };
    if (method === 'card') {
        const cardNumber = document.getElementById('card-number').value.trim();
        const cardName = document.getElementById('card-name').value.trim();
        const cardExpiry = document.getElementById('card-expiry').value.trim();
        const cardCvc = document.getElementById('card-cvc').value.trim();
        if (!cardNumber || !cardName || !cardExpiry || !cardCvc) {
            alert('Please fill in all card fields.');
            return;
        }
        paymentMethod.cardDetails = {
            cardNumber,
            cardName,
            cardExpiry,
            cardCvc
        };
    }
    // Build object for the endpoint
    const orderData = {
        productId: productId,
        deliveryAddress,
        paymentMethod,
        paymentIntent
    };
    // Call the /api/checkout/orders endpoint
    fetch('/api/checkout/orders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(orderData)
    })
    .then(response => {
        if (!response.ok) throw new Error('Checkout error');
        return response.json();
    })
    .then(data => {
        console.log("Server response:", data);
        if('COMPLETED' === data.status) {
            alert('Payment captured successfully!');
            // Return to store view
            cancelPaymentForm();
            cancelCheckout();
            returnToStore();
            return;
        }else if('PAYER_ACTION_REQUIRED' === data.status) {
            // Redirect to the provided URL
            window.location.href = data.approvalUrl;
        }else{
            cancelPaymentForm();
        }

    })
    .catch(error => {
        alert('There was an error processing the checkout.');
        console.error(error);
    });
}

function getQueryParams() {
    const params = {};
    window.location.search.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m, key, value) {
        params[decodeURIComponent(key)] = decodeURIComponent(value);
    });
    return params;
}

// Helper to perform order action (capture/authorize)
function callOrderAction(actionType, referenceId) {
    const endpoint = `/api/checkout/orders/${encodeURIComponent(referenceId)}/${actionType}`;
    return fetch(endpoint, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
    }).then(response => {
        if (!response.ok) throw new Error(`${actionType} failed`);
        return response.json();
    });
}

// New helper: remove query params and return UI to store/catalog view
function returnToStore() {
    if (window.location.search) {
        const newUrl = window.location.origin + window.location.pathname + window.location.hash;
        window.history.replaceState(null, '', newUrl);
    }
    if (typeof retrieveCatalog === 'function') {
        retrieveCatalog();
    }
}

function process(action, data) {
    console.log(`Processing action: ${action} with data: ${data}`);

    const actionMap = {
        'captureOrder': { endpointAction: 'capture', successMsg: 'Payment captured successfully!', errorVerb: 'capturing' },
        'authorizeOrder': { endpointAction: 'authorize', successMsg: 'Payment authorized successfully!', errorVerb: 'authorizing' }
    };

    const mapping = actionMap[action];
    if (mapping) {
        callOrderAction(mapping.endpointAction, data)
            .then(result => {
                alert(mapping.successMsg);
                console.log(`${mapping.endpointAction} response:`, result);

                // centralised cleanup: remove query params and return to store view
                returnToStore();
            })
            .catch(error => {
                alert(`There was an error ${mapping.errorVerb} the payment.`);
                console.error(error);

                // also remove query params and return to store view on error
                returnToStore();
            });
    } else {
        alert(`Processing action: ${action} with data: ${data}`);
        // You can add real logic here as needed
        returnToStore();
    }
}

function retrieveCatalog() {
    fetch('/api/catalog')
        .then(response => response.json())
        .then(data => {
            productsData = data;
            renderProducts(productsData);
        });
}

// On page load, decide what to run based on query params
const params = getQueryParams();
if (params.action && params.data) {
    process(params.action, params.data);
} else {
    retrieveCatalog();
}
