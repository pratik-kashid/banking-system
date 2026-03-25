const API_BASE = '/api';
let authToken = localStorage.getItem('token');
let currentUser = localStorage.getItem('username');
let currentAccounts = [];

// DOM Elements
const authSection = document.getElementById('auth-section');
const dashboardSection = document.getElementById('dashboard-section');
const app = document.getElementById('app');

// Initialize
function init() {
    if (authToken && currentUser) {
        showDashboard();
    } else {
        showAuth();
    }
}

// UI Controllers
function showAuth() {
    authSection.classList.remove('hide');
    dashboardSection.classList.add('hide');
    // Remove inline styles that could conflict
    authSection.style.display = '';
    dashboardSection.style.display = 'none';
    app.style.display = 'flex';
}

function showDashboard() {
    authSection.classList.add('hide');
    dashboardSection.classList.remove('hide');
    authSection.style.display = 'none';
    dashboardSection.style.display = 'flex';
    app.style.display = 'block';
    
    document.getElementById('user-greeting').textContent = currentUser;
    loadAccounts();
}

function switchTab(tab) {
    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');
    const tabLogin = document.getElementById('tab-login');
    const tabRegister = document.getElementById('tab-register');
    const errorEl = document.getElementById('auth-error');
    
    errorEl.textContent = '';
    
    if (tab === 'login') {
        loginForm.classList.remove('hide');
        registerForm.classList.add('hide');
        tabLogin.classList.add('active');
        tabRegister.classList.remove('active');
    } else {
        loginForm.classList.add('hide');
        registerForm.classList.remove('hide');
        tabLogin.classList.remove('active');
        tabRegister.classList.add('active');
    }
}

function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    authToken = null;
    currentUser = null;
    showAuth();
}

// Helper for API calls
async function fetchAPI(endpoint, options = {}) {
    const headers = {
        'Content-Type': 'application/json',
        ...options.headers
    };
    
    if (authToken) {
        headers['Authorization'] = `Bearer ${authToken}`;
    }
    
    const response = await fetch(`${API_BASE}${endpoint}`, {
        ...options,
        headers
    });
    
    let data;
    const contentType = response.headers.get("content-type");
    if (contentType && contentType.indexOf("application/json") !== -1) {
      data = await response.json();
    } else {
      data = await response.text();
    }
    
    if (!response.ok) {
        const errorMsg = typeof data === 'string' ? data : (data.message || 'API Error');
        if (response.status === 401 || response.status === 403) {
            logout();
        }
        throw new Error(errorMsg);
    }
    
    return data;
}

// Auth Actions
document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const errorEl = document.getElementById('auth-error');
    errorEl.textContent = 'Logging in...';
    errorEl.className = 'text-center'; // not red yet
    
    const payload = {
        username: document.getElementById('login-username').value,
        password: document.getElementById('login-password').value
    };
    
    try {
        const data = await fetchAPI('/auth/login', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        
        authToken = data.token;
        currentUser = data.username;
        localStorage.setItem('token', authToken);
        localStorage.setItem('username', currentUser);
        errorEl.textContent = '';
        showDashboard();
    } catch (err) {
        errorEl.textContent = err.message || 'Login failed. Check credentials.';
        errorEl.className = 'error-text';
    }
});

document.getElementById('register-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const errorEl = document.getElementById('auth-error');
    errorEl.textContent = 'Creating account...';
    errorEl.className = 'text-center';
    
    const payload = {
        username: document.getElementById('reg-username').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-password').value,
        role: 'USER'
    };
    
    try {
        const data = await fetchAPI('/auth/register', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        errorEl.textContent = 'Registration successful! Please login.';
        errorEl.className = 'success-text';
        setTimeout(() => switchTab('login'), 1500);
    } catch (err) {
        errorEl.textContent = err.message || 'Registration failed.';
        errorEl.className = 'error-text';
    }
});

// Dashboard Actions
async function loadAccounts() {
    const listEl = document.getElementById('accounts-list');
    const transferSource = document.getElementById('transfer-source');
    const historySelect = document.getElementById('history-account-select');
    
    listEl.innerHTML = '<div class="loader"></div>';
    
    try {
        const accounts = await fetchAPI('/accounts/my-accounts');
        currentAccounts = accounts;
        
        if (accounts.length === 0) {
            listEl.innerHTML = '<p>No accounts found.</p>';
            return;
        }
        
        listEl.innerHTML = accounts.map(acc => `
            <div class="account-item">
                <div class="acc-name">Account Type: <span style="text-transform: capitalize">${acc.accountType}</span></div>
                <div class="acc-bal">$${parseFloat(acc.balance).toFixed(2)}</div>
                <div class="acc-num">${acc.accountNumber}</div>
            </div>
        `).join('');
        
        // Populate selects
        let optionsHtml = '';
        accounts.forEach(acc => {
            optionsHtml += `<option value="${acc.accountNumber}">${acc.accountType} - ${acc.accountNumber.substring(0,8)}...</option>`;
        });
        
        transferSource.innerHTML = optionsHtml;
        historySelect.innerHTML = optionsHtml;
        
        loadHistory();
        
    } catch (err) {
        listEl.innerHTML = `<p class="error-text">Failed to load accounts: ${err.message}</p>`;
    }
}

async function loadHistory() {
    const accountNum = document.getElementById('history-account-select').value;
    const bodyEl = document.getElementById('transactions-body');
    
    if (!accountNum) return;
    
    bodyEl.innerHTML = '<tr><td colspan="3" class="text-center"><div class="loader"></div></td></tr>';
    
    try {
        const history = await fetchAPI(`/transactions/history/${accountNum}`);
        
        if (!history || history.length === 0) {
            bodyEl.innerHTML = '<tr><td colspan="3" class="text-center">No recent transactions</td></tr>';
            return;
        }
        
        bodyEl.innerHTML = history.map(tx => {
            const date = new Date(tx.timestamp).toLocaleString(undefined, {
                month: 'short', day: 'numeric', hour: '2-digit', minute:'2-digit'
            });
            const isSend = tx.sourceAccount && tx.sourceAccount.accountNumber === accountNum;
            const amtClass = isSend ? 'amt-negative' : 'amt-positive';
            const sign = isSend ? '-' : '+';
            return `
                <tr>
                    <td>${date}</td>
                    <td class="type-transfer">${tx.transactionType}</td>
                    <td class="${amtClass}">${sign}$${parseFloat(tx.amount).toFixed(2)}</td>
                </tr>
            `;
        }).join('');
        
    } catch (err) {
        bodyEl.innerHTML = `<tr><td colspan="3" class="error-text text-center">Failed to load history</td></tr>`;
    }
}

// Transfer Form
document.getElementById('transfer-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const msgEl = document.getElementById('transfer-msg');
    const btn = document.getElementById('btn-transfer');
    
    msgEl.textContent = 'Processing...';
    msgEl.className = 'text-center';
    btn.disabled = true;
    
    const payload = {
        sourceAccountNumber: document.getElementById('transfer-source').value,
        destinationAccountNumber: document.getElementById('transfer-dest').value,
        amount: parseFloat(document.getElementById('transfer-amount').value)
    };
    
    try {
        const responseText = await fetchAPI('/transactions/transfer', {
            method: 'POST',
            body: JSON.stringify(payload)
        });
        
        msgEl.textContent = 'Transfer successful!';
        msgEl.className = 'success-text';
        document.getElementById('transfer-amount').value = '';
        document.getElementById('transfer-dest').value = '';
        
        // Refresh data
        setTimeout(() => {
            msgEl.textContent = '';
            loadAccounts();
        }, 2000);
        
    } catch (err) {
        msgEl.textContent = err.message || 'Transfer failed.';
        msgEl.className = 'error-text';
    } finally {
        btn.disabled = false;
    }
});

// Start
init();
