// ── DATA ────────────────────────────────────────────────
const CATEGORIES = [
  { id: 'food',       label: 'Food',       emoji: '🍜' },
  { id: 'transport',  label: 'Transport',  emoji: '🚌' },
  { id: 'shopping',   label: 'Shopping',   emoji: '🛍️' },
  { id: 'health',     label: 'Health',     emoji: '💊' },
  { id: 'bills',      label: 'Bills',      emoji: '📄' },
  { id: 'entertain',  label: 'Fun',        emoji: '🎉' },
  { id: 'education',  label: 'Education',  emoji: '📚' },
  { id: 'other',      label: 'Other',      emoji: '📦' },
];

const COLORS = ['#e8c547','#f0a050','#55c78a','#6ab0f5','#d06af5','#e85555','#50d0c8','#f5a0b5'];

let expenses = JSON.parse(localStorage.getItem('spendly_expenses') || '[]');
let budget   = parseFloat(localStorage.getItem('spendly_budget') || '0');
let selectedCat = '';

function save() {
  localStorage.setItem('spendly_expenses', JSON.stringify(expenses));
  localStorage.setItem('spendly_budget', budget.toString());
}

function fmt(n) {
  return '₹' + parseFloat(n).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function today() {
  return new Date().toISOString().slice(0, 10);
}

// ── NAV ─────────────────────────────────────────────────
const navBtns = document.querySelectorAll('.nav-btn');
const views   = document.querySelectorAll('.view');

navBtns.forEach(btn => {
  btn.addEventListener('click', () => {
    navBtns.forEach(b => b.classList.remove('active'));
    btn.classList.add('active');
    const target = btn.dataset.view;
    views.forEach(v => {
      v.classList.toggle('active', v.id === 'view-' + target);
    });
    if (target === 'analytics') renderCharts();
    if (target === 'transactions') renderAllTx();
  });
});

// ── MODAL ────────────────────────────────────────────────
const overlay   = document.getElementById('modalOverlay');
const openBtn   = document.getElementById('openModal');
const closeBtn  = document.getElementById('closeModal');

openBtn.addEventListener('click', () => overlay.classList.add('open'));
closeBtn.addEventListener('click', closeModal);
overlay.addEventListener('click', e => { if (e.target === overlay) closeModal(); });

function closeModal() {
  overlay.classList.remove('open');
  document.getElementById('expDesc').value = '';
  document.getElementById('expAmount').value = '';
  document.getElementById('expDate').value = today();
  document.getElementById('expNote').value = '';
  selectedCat = '';
  document.querySelectorAll('.cat-btn').forEach(b => b.classList.remove('selected'));
}

// ── CATEGORY BUTTONS ─────────────────────────────────────
const catGrid = document.getElementById('catGrid');
CATEGORIES.forEach(cat => {
  const btn = document.createElement('button');
  btn.className = 'cat-btn';
  btn.dataset.id = cat.id;
  btn.innerHTML = `<span class="cat-em">${cat.emoji}</span><span class="cat-lbl">${cat.label}</span>`;
  btn.addEventListener('click', () => {
    document.querySelectorAll('.cat-btn').forEach(b => b.classList.remove('selected'));
    btn.classList.add('selected');
    selectedCat = cat.id;
  });
  catGrid.appendChild(btn);
});

// Pre-fill today's date
document.getElementById('expDate').value = today();

// ── ADD EXPENSE ───────────────────────────────────────────
document.getElementById('submitExpense').addEventListener('click', () => {
  const desc   = document.getElementById('expDesc').value.trim();
  const amount = parseFloat(document.getElementById('expAmount').value);
  const date   = document.getElementById('expDate').value;
  const note   = document.getElementById('expNote').value.trim();

  if (!desc) return shake('expDesc');
  if (!amount || amount <= 0) return shake('expAmount');
  if (!selectedCat) return alert('Please select a category.');
  if (!date) return shake('expDate');

  const cat = CATEGORIES.find(c => c.id === selectedCat);
  expenses.unshift({ id: Date.now(), desc, amount, date, note, catId: cat.id, catLabel: cat.label, catEmoji: cat.emoji });
  save();
  closeModal();
  renderDashboard();
  populateCatFilter();
});

function shake(id) {
  const el = document.getElementById(id);
  el.style.animation = 'none';
  el.offsetHeight;
  el.style.animation = 'shake .3s ease';
  el.style.borderColor = '#e85555';
  setTimeout(() => { el.style.borderColor = ''; el.style.animation = ''; }, 600);
}

// Shake animation
const style = document.createElement('style');
style.textContent = `@keyframes shake { 0%,100%{transform:none} 25%{transform:translateX(-6px)} 75%{transform:translateX(6px)} }`;
document.head.appendChild(style);

// ── BUDGET ───────────────────────────────────────────────
document.getElementById('setBudget').addEventListener('click', () => {
  const val = parseFloat(document.getElementById('budgetInput').value);
  if (val > 0) { budget = val; save(); renderDashboard(); }
});

// ── RENDER DASHBOARD ─────────────────────────────────────
function renderDashboard() {
  const now   = new Date();
  const month = now.getMonth();
  const year  = now.getFullYear();

  const thisMonth = expenses.filter(e => {
    const d = new Date(e.date);
    return d.getMonth() === month && d.getFullYear() === year;
  });

  const total = thisMonth.reduce((s, e) => s + e.amount, 0);

  document.getElementById('totalSpent').textContent = fmt(total);
  document.getElementById('monthLabel').textContent = now.toLocaleString('default', { month: 'long', year: 'numeric' });
  document.getElementById('txCount').textContent = expenses.length;
  document.getElementById('avgExpense').textContent = expenses.length ? fmt(total / (thisMonth.length || 1)) : '₹0';

  // Budget
  if (budget > 0) {
    document.getElementById('budgetDisplay').textContent = fmt(budget);
    document.getElementById('budgetInput').value = '';
    const pct = Math.min((total / budget) * 100, 100);
    const bar = document.getElementById('budgetBar');
    bar.style.width = pct + '%';
    bar.style.background = pct > 90 ? '#e85555' : pct > 70 ? '#f0a050' : '#e8c547';
    document.getElementById('budgetPct').textContent = pct.toFixed(0) + '% used';
  } else {
    document.getElementById('budgetDisplay').textContent = 'Not set';
    document.getElementById('budgetPct').textContent = '—';
  }

  // Top category
  const catTotals = {};
  thisMonth.forEach(e => {
    catTotals[e.catId] = (catTotals[e.catId] || 0) + e.amount;
  });
  const topCatId = Object.keys(catTotals).sort((a,b) => catTotals[b] - catTotals[a])[0];
  if (topCatId) {
    const cat = CATEGORIES.find(c => c.id === topCatId);
    document.getElementById('topCatIcon').textContent = cat.emoji;
    document.getElementById('topCatName').textContent = cat.label;
    document.getElementById('topCatAmount').textContent = fmt(catTotals[topCatId]);
  } else {
    document.getElementById('topCatIcon').textContent = '—';
    document.getElementById('topCatName').textContent = 'No data';
    document.getElementById('topCatAmount').textContent = '';
  }

  // Recent list
  renderTxList(document.getElementById('recentList'), expenses.slice(0, 5));
}

function renderTxList(container, list) {
  container.innerHTML = '';
  if (!list.length) {
    container.innerHTML = `<div class="empty-state"><div class="empty-icon">🪙</div>No transactions yet. Add your first expense!</div>`;
    return;
  }
  list.forEach(e => {
    const item = document.createElement('div');
    item.className = 'tx-item';
    const d = new Date(e.date + 'T00:00:00');
    const dateStr = d.toLocaleDateString('en-IN', { day: '2-digit', month: 'short', year: 'numeric' });
    item.innerHTML = `
      <div class="tx-emoji">${e.catEmoji}</div>
      <div class="tx-info">
        <div class="tx-desc">${e.desc}</div>
        <div class="tx-meta">${e.catLabel} · ${dateStr}${e.note ? ' · ' + e.note : ''}</div>
      </div>
      <div class="tx-amount">${fmt(e.amount)}</div>
      <button class="tx-delete" data-id="${e.id}" title="Delete">✕</button>
    `;
    item.querySelector('.tx-delete').addEventListener('click', (ev) => {
      deleteExpense(parseInt(ev.currentTarget.dataset.id));
    });
    container.appendChild(item);
  });
}

function deleteExpense(id) {
  expenses = expenses.filter(e => e.id !== id);
  save();
  renderDashboard();
  renderAllTx();
  populateCatFilter();
}

// ── TRANSACTIONS PAGE ─────────────────────────────────────
function populateCatFilter() {
  const sel = document.getElementById('filterCat');
  const used = [...new Set(expenses.map(e => e.catId))];
  sel.innerHTML = '<option value="">All Categories</option>';
  used.forEach(id => {
    const cat = CATEGORIES.find(c => c.id === id);
    if (cat) sel.innerHTML += `<option value="${id}">${cat.emoji} ${cat.label}</option>`;
  });
}

function renderAllTx() {
  const catFilter  = document.getElementById('filterCat').value;
  const sortFilter = document.getElementById('filterSort').value;

  let list = [...expenses];
  if (catFilter) list = list.filter(e => e.catId === catFilter);
  if (sortFilter === 'oldest') list.sort((a,b) => new Date(a.date) - new Date(b.date));
  else if (sortFilter === 'high') list.sort((a,b) => b.amount - a.amount);
  else if (sortFilter === 'low') list.sort((a,b) => a.amount - b.amount);
  else list.sort((a,b) => b.id - a.id);

  renderTxList(document.getElementById('allTxList'), list);
}

document.getElementById('filterCat').addEventListener('change', renderAllTx);
document.getElementById('filterSort').addEventListener('change', renderAllTx);

document.getElementById('clearAll').addEventListener('click', () => {
  if (expenses.length === 0) return;
  if (confirm('Clear all transactions? This cannot be undone.')) {
    expenses = [];
    save();
    renderDashboard();
    renderAllTx();
    populateCatFilter();
  }
});

// ── ANALYTICS ────────────────────────────────────────────
function renderCharts() {
  renderPie();
  renderBar();
}

function renderPie() {
  const canvas = document.getElementById('pieCanvas');
  const ctx    = canvas.getContext('2d');
  const size   = 260;
  canvas.width = size; canvas.height = size;
  ctx.clearRect(0, 0, size, size);

  const catTotals = {};
  expenses.forEach(e => { catTotals[e.catId] = (catTotals[e.catId] || 0) + e.amount; });
  const entries = Object.entries(catTotals).sort((a,b) => b[1] - a[1]);
  const total   = entries.reduce((s,[,v]) => s + v, 0);

  const legend = document.getElementById('legendList');
  legend.innerHTML = '';

  if (!total) {
    ctx.fillStyle = '#2e2e38';
    ctx.beginPath();
    ctx.arc(size/2, size/2, size/2 - 10, 0, Math.PI * 2);
    ctx.fill();
    ctx.fillStyle = '#8a8798';
    ctx.textAlign = 'center';
    ctx.font = '14px Instrument Sans';
    ctx.fillText('No data', size/2, size/2 + 5);
    return;
  }

  let startAngle = -Math.PI / 2;
  entries.forEach(([catId, val], i) => {
    const cat   = CATEGORIES.find(c => c.id === catId);
    const slice = (val / total) * Math.PI * 2;
    const color = COLORS[i % COLORS.length];
    ctx.beginPath();
    ctx.moveTo(size/2, size/2);
    ctx.arc(size/2, size/2, size/2 - 4, startAngle, startAngle + slice);
    ctx.closePath();
    ctx.fillStyle = color;
    ctx.fill();
    startAngle += slice;

    // Legend
    const li = document.createElement('div');
    li.className = 'legend-item';
    li.innerHTML = `
      <div class="legend-dot" style="background:${color}"></div>
      <span class="legend-name">${cat ? cat.emoji + ' ' + cat.label : catId}</span>
      <span class="legend-val">${fmt(val)}</span>
    `;
    legend.appendChild(li);
  });

  // Donut hole
  ctx.beginPath();
  ctx.arc(size/2, size/2, size/2 - 54, 0, Math.PI*2);
  ctx.fillStyle = '#18181b';
  ctx.fill();

  ctx.fillStyle = '#f0ede8';
  ctx.textAlign = 'center';
  ctx.font = 'bold 15px DM Mono';
  ctx.fillText(fmt(total), size/2, size/2 + 5);
}

function renderBar() {
  const canvas = document.getElementById('barCanvas');
  const ctx    = canvas.getContext('2d');
  const W = canvas.width = canvas.parentElement.clientWidth - 56 || 480;
  const H = canvas.height = 220;
  ctx.clearRect(0, 0, W, H);

  // Last 14 days
  const days = [];
  for (let i = 13; i >= 0; i--) {
    const d = new Date();
    d.setDate(d.getDate() - i);
    days.push(d.toISOString().slice(0, 10));
  }

  const dayTotals = {};
  days.forEach(d => { dayTotals[d] = 0; });
  expenses.forEach(e => { if (dayTotals[e.date] !== undefined) dayTotals[e.date] += e.amount; });

  const vals   = days.map(d => dayTotals[d]);
  const maxVal = Math.max(...vals, 1);

  const pad = { left: 10, right: 10, top: 20, bottom: 36 };
  const barW = (W - pad.left - pad.right) / days.length;

  vals.forEach((val, i) => {
    const barH = ((val / maxVal) * (H - pad.top - pad.bottom));
    const x = pad.left + i * barW + barW * .15;
    const y = H - pad.bottom - barH;
    const bw = barW * .7;

    // Bar
    const grad = ctx.createLinearGradient(0, y, 0, H - pad.bottom);
    grad.addColorStop(0, '#e8c547');
    grad.addColorStop(1, 'rgba(232,197,71,.15)');
    ctx.fillStyle = val ? grad : '#2e2e38';
    ctx.beginPath();
    ctx.roundRect(x, val ? y : H - pad.bottom - 2, bw, val ? barH : 2, [4, 4, 0, 0]);
    ctx.fill();

    // Date label
    const d = new Date(days[i] + 'T00:00:00');
    ctx.fillStyle = '#8a8798';
    ctx.font = '10px DM Mono';
    ctx.textAlign = 'center';
    if (i % 2 === 0) ctx.fillText(d.getDate() + '/' + (d.getMonth()+1), x + bw/2, H - pad.bottom + 14);
  });
}

// ── INIT ─────────────────────────────────────────────────
renderDashboard();
populateCatFilter();
