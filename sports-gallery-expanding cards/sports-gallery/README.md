# ARENA.LIVE — Sports Expanding Cards Gallery

A championship-grade sports gallery with cinematic expanding cards, live animations, and immersive effects.

## 🏆 Sports Featured
- 🏈 **Football** (NFL Playoffs) — Orange accent
- 🏀 **Basketball** (NBA Finals) — Red accent
- ⚽ **Soccer** (UEFA Champions) — Green accent
- 🥊 **Combat Sports** (UFC Heavyweight) — Crimson accent
- 🎾 **Tennis** (Wimbledon) — Yellow accent

## ✨ Features

| Feature | Details |
|---------|---------|
| Expanding Cards | Smooth 0.7s cubic-bezier expansion on hover/click |
| Particle System | 55 floating sport-colored particles |
| Custom Cursor | Magnetic blend-mode cursor that grows on hover |
| Live Score Ticker | Header ticker with rotating live scores |
| Counter Animation | Hero stats count up on page load |
| Auto-Cycle | Cards rotate every 4s, pauses on interaction |
| Progress Bar | Visual timer bar below the gallery |
| Sport Icon Float | Animated floating emoji icons |
| Diagonal Light Ray | Per-card accent diagonal beam |
| Scanline Overlay | CRT-style scanlines for texture |
| Ripple Effect | Button click ripple animation |
| Keyboard Nav | ← → or ↑ ↓ arrow keys |
| Touch/Swipe | Full mobile swipe support |
| Responsive | Adapts to all screen sizes |

## 📁 Structure
```
sports-gallery/
├── index.html        ← Main page
├── css/
│   └── style.css     ← Full theme + animations
├── js/
│   └── gallery.js    ← All interactions + particle engine
└── README.md
```

## 🎨 Design Language

**Theme:** Championship Arena — dark, powerful, cinematic

- **Fonts:** Anton (hero) · Teko (UI) · Oswald (stats) · Rajdhani (body)
- **Base palette:** #060608 bg, #E8E4DC text
- **Sport accents:** Each card has unique color + glow
- **Typography scale:** Massive display headers with outline variants

## 🖥️ Usage

Open `index.html` in any modern browser (Chrome, Firefox, Safari, Edge).
No build step, no dependencies, no frameworks — pure HTML/CSS/JS.

## ⌨️ Controls

| Input | Action |
|-------|--------|
| Hover card | Expand |
| Click card | Expand + restart timer |
| ← → Keys | Navigate |
| Click dots | Jump to card |
| Click ◀ ▶ | Prev/Next |
| Swipe | Mobile navigate |

## 🔧 Customisation

Each card's accent color is set via CSS custom properties on the element:

```html
<div class="card" data-sport="your-sport">
```

Add matching CSS:
```css
[data-sport="your-sport"] {
  --ac: #YOUR_COLOR;
  --ac2: #YOUR_LIGHT_COLOR;
  --glow: rgba(R, G, B, 0.35);
}
```
