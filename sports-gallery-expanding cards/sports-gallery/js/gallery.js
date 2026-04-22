/* ═══════════════════════════════════════════════════
   ARENA.LIVE — Gallery JS
   Features: Particles · Custom Cursor · Counter Anim
             Auto-cycle · Keyboard · Touch · Progress
   ═══════════════════════════════════════════════════ */

(function () {
  'use strict';

  /* ── DOM ── */
  const gallery   = document.getElementById('gallery');
  const cards     = [...gallery.querySelectorAll('.card')];
  const dots      = [...document.querySelectorAll('.cdot')];
  const cpBar     = document.getElementById('cpBar');
  const prevBtn   = document.getElementById('prevBtn');
  const nextBtn   = document.getElementById('nextBtn');
  const canvas    = document.getElementById('particles');
  const ctx       = canvas.getContext('2d');

  let current     = 0;
  let autoCycle   = null;
  let progressInv = null;
  let paused      = false;
  const INTERVAL  = 4000;
  let progressPct = 0;

  /* ══════════════════════════════════════
     CUSTOM CURSOR
  ══════════════════════════════════════ */
  const cursor = document.createElement('div');
  cursor.className = 'custom-cursor';
  document.body.appendChild(cursor);

  let mx = -200, my = -200;
  let cx = -200, cy = -200;

  document.addEventListener('mousemove', e => { mx = e.clientX; my = e.clientY; });

  document.querySelectorAll('a, button, .card').forEach(el => {
    el.addEventListener('mouseenter', () => cursor.classList.add('hover'));
    el.addEventListener('mouseleave', () => cursor.classList.remove('hover'));
  });

  function animCursor() {
    cx += (mx - cx) * 0.14;
    cy += (my - cy) * 0.14;
    cursor.style.left = cx + 'px';
    cursor.style.top  = cy + 'px';
    requestAnimationFrame(animCursor);
  }
  animCursor();

  /* ══════════════════════════════════════
     PARTICLE SYSTEM
  ══════════════════════════════════════ */
  function resizeCanvas() {
    canvas.width  = window.innerWidth;
    canvas.height = window.innerHeight;
  }

  resizeCanvas();
  window.addEventListener('resize', resizeCanvas);

  const PARTICLE_COUNT = 55;
  const particles = [];

  const SPORT_COLORS = [
    '#FF6B00', '#C8102E', '#00C853',
    '#B71C1C', '#FFD600', '#FFFFFF'
  ];

  class Particle {
    constructor() { this.reset(true); }

    reset(init) {
      this.x  = Math.random() * canvas.width;
      this.y  = init ? Math.random() * canvas.height : canvas.height + 10;
      this.vx = (Math.random() - 0.5) * 0.4;
      this.vy = -(Math.random() * 0.6 + 0.2);
      this.size   = Math.random() * 2.5 + 0.5;
      this.alpha  = Math.random() * 0.5 + 0.1;
      this.color  = SPORT_COLORS[Math.floor(Math.random() * SPORT_COLORS.length)];
      this.twinkle = Math.random() * Math.PI * 2;
      this.twinkleSpeed = Math.random() * 0.04 + 0.01;
    }

    update() {
      this.x += this.vx;
      this.y += this.vy;
      this.twinkle += this.twinkleSpeed;
      if (this.y < -10) this.reset(false);
    }

    draw() {
      const a = this.alpha * (0.7 + 0.3 * Math.sin(this.twinkle));
      ctx.beginPath();
      ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
      ctx.fillStyle = this.color;
      ctx.globalAlpha = a;
      ctx.fill();
      ctx.globalAlpha = 1;
    }
  }

  for (let i = 0; i < PARTICLE_COUNT; i++) particles.push(new Particle());

  function drawParticles() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    particles.forEach(p => { p.update(); p.draw(); });
    requestAnimationFrame(drawParticles);
  }

  drawParticles();

  /* ══════════════════════════════════════
     COUNTER ANIMATION
  ══════════════════════════════════════ */
  function animateCounters() {
    document.querySelectorAll('.hs-num[data-target]').forEach(el => {
      const target = +el.dataset.target;
      const hasK   = target >= 1000;
      let start    = null;
      const dur    = 1800;

      function step(ts) {
        if (!start) start = ts;
        const progress = Math.min((ts - start) / dur, 1);
        const ease = 1 - Math.pow(1 - progress, 3);
        const val  = Math.floor(ease * target);
        el.textContent = hasK ? (val >= 1000 ? (val / 1000).toFixed(1) + 'K' : val) : val;
        if (progress < 1) requestAnimationFrame(step);
      }

      requestAnimationFrame(step);
    });
  }

  setTimeout(animateCounters, 600);

  /* ══════════════════════════════════════
     CARD ACTIVATION
  ══════════════════════════════════════ */
  function activate(idx, skipReset) {
    current = (idx + cards.length) % cards.length;
    cards.forEach((c, i) => c.classList.toggle('active', i === current));
    dots.forEach((d, i)  => d.classList.toggle('active', i === current));
    if (!skipReset) resetProgress();
  }

  /* ══════════════════════════════════════
     PROGRESS BAR
  ══════════════════════════════════════ */
  function resetProgress() {
    progressPct = 0;
    cpBar.style.width = '0%';
    cpBar.style.transition = 'none';
    clearInterval(progressInv);

    if (!paused) {
      progressInv = setInterval(() => {
        progressPct += 100 / (INTERVAL / 80);
        if (progressPct >= 100) {
          progressPct = 100;
          clearInterval(progressInv);
        }
        cpBar.style.transition = 'width 0.08s linear';
        cpBar.style.width = progressPct + '%';
      }, 80);
    }
  }

  /* ══════════════════════════════════════
     AUTO CYCLE
  ══════════════════════════════════════ */
  function startCycle() {
    clearInterval(autoCycle);
    autoCycle = setInterval(() => {
      if (!paused) activate(current + 1);
    }, INTERVAL);
    resetProgress();
  }

  function stopCycle() {
    paused = true;
    clearInterval(autoCycle);
    clearInterval(progressInv);
    cpBar.style.width = '0%';
  }

  function resumeCycle() {
    paused = false;
    startCycle();
  }

  startCycle();

  /* ══════════════════════════════════════
     EVENT HANDLERS
  ══════════════════════════════════════ */

  // Card click/hover
  cards.forEach((card, i) => {
    card.addEventListener('click', () => {
      current = i;
      activate(i);
      stopCycle();
      resumeCycle();
    });

    card.addEventListener('mouseenter', () => {
      if (i !== current) {
        activate(i, true);
        stopCycle();
      }
    });

    card.addEventListener('mouseleave', () => {
      if (paused) resumeCycle();
    });
  });

  // Gallery leave — resume
  gallery.addEventListener('mouseleave', () => {
    if (paused) resumeCycle();
  });

  // Dot controls
  dots.forEach((dot, i) => {
    dot.addEventListener('click', () => {
      activate(i);
      stopCycle();
      resumeCycle();
    });
  });

  // Arrow buttons
  prevBtn.addEventListener('click', () => {
    activate(current - 1);
    stopCycle();
    resumeCycle();
  });

  nextBtn.addEventListener('click', () => {
    activate(current + 1);
    stopCycle();
    resumeCycle();
  });

  // Keyboard navigation
  document.addEventListener('keydown', e => {
    if (e.key === 'ArrowRight' || e.key === 'ArrowDown') {
      activate(current + 1);
      stopCycle();
      resumeCycle();
    } else if (e.key === 'ArrowLeft' || e.key === 'ArrowUp') {
      activate(current - 1);
      stopCycle();
      resumeCycle();
    }
  });

  // Touch / swipe
  let touchX = 0;

  gallery.addEventListener('touchstart', e => {
    touchX = e.touches[0].clientX;
    stopCycle();
  }, { passive: true });

  gallery.addEventListener('touchend', e => {
    const dx = e.changedTouches[0].clientX - touchX;
    if (Math.abs(dx) > 40) {
      activate(dx < 0 ? current + 1 : current - 1);
    }
    resumeCycle();
  }, { passive: true });

  // Button watch click
  document.querySelectorAll('.cb-btn').forEach(btn => {
    btn.addEventListener('click', e => {
      e.stopPropagation();
      const card  = btn.closest('.card');
      const sport = card.dataset.sport;
      const title = card.querySelector('.cb-title').innerText.replace(/\n/g, ' ').trim();
      // Ripple effect
      const ripple = document.createElement('span');
      Object.assign(ripple.style, {
        position: 'absolute',
        borderRadius: '50%',
        background: 'rgba(255,255,255,0.25)',
        width: '200px', height: '200px',
        left: e.offsetX - 100 + 'px',
        top: e.offsetY - 100 + 'px',
        transform: 'scale(0)',
        animation: 'ripple .5s ease-out forwards',
        pointerEvents: 'none'
      });
      btn.appendChild(ripple);
      setTimeout(() => ripple.remove(), 600);
      setTimeout(() => alert(`🏆 "${title}" highlights — coming soon on ARENA.LIVE!`), 300);
    });
  });

  // Ripple keyframe (inject once)
  const style = document.createElement('style');
  style.textContent = `@keyframes ripple { to { transform: scale(1); opacity: 0; } }`;
  document.head.appendChild(style);

  /* ══════════════════════════════════════
     SCORE TICKER ANIMATION
  ══════════════════════════════════════ */
  const ticker = document.querySelector('.score-ticker');
  if (ticker) {
    const scores = [
      '🏈 Chiefs <strong>24</strong> — <strong>17</strong> Bills',
      '🏀 Lakers <strong>108</strong> — <strong>102</strong> Celtics',
      '⚽ Man City <strong>3</strong> — <strong>1</strong> Arsenal',
      '🎾 Djokovic <strong>6-4 7-5</strong> Alcaraz',
      '🏒 Rangers <strong>4</strong> — <strong>2</strong> Bruins',
      '⚾ Yankees <strong>7</strong> — <strong>3</strong> Red Sox'
    ];
    let si = 0;
    const items = ticker.querySelectorAll('.st-item');

    setInterval(() => {
      si = (si + 1) % scores.length;
      if (items[0]) {
        items[0].style.transition = 'opacity .4s';
        items[0].style.opacity = '0';
        setTimeout(() => {
          items[0].innerHTML = scores[si];
          items[0].style.opacity = '1';
        }, 400);
      }
    }, 3000);
  }

  /* ══════════════════════════════════════
     RESIZE HANDLER
  ══════════════════════════════════════ */
  window.addEventListener('resize', () => {
    if (window.innerWidth < 600 && cards[current]) {
      activate(current, true);
    }
  });

})();
