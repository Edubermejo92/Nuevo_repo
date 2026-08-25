/*!
 * Padel Pulse Live — padelpulselive.es
 * Interacciones de la web: navegación, revelado al hacer scroll y
 * consentimiento de cookies (RGPD / LSSI-CE).
 */
(function () {
  'use strict';

  /* ---------------------------------------------------------------- Nav -- */
  var nav = document.querySelector('[data-nav]');
  var burger = document.querySelector('[data-burger]');
  var menu = document.querySelector('[data-menu]');

  if (nav) {
    var onScroll = function () {
      nav.classList.toggle('is-stuck', window.scrollY > 12);
    };
    onScroll();
    window.addEventListener('scroll', onScroll, { passive: true });
  }

  if (burger && menu) {
    burger.addEventListener('click', function () {
      var open = burger.getAttribute('aria-expanded') === 'true';
      burger.setAttribute('aria-expanded', String(!open));
      menu.classList.toggle('is-open', !open);
      document.body.style.overflow = !open ? 'hidden' : '';
    });

    menu.addEventListener('click', function (e) {
      if (e.target.closest('a')) {
        burger.setAttribute('aria-expanded', 'false');
        menu.classList.remove('is-open');
        document.body.style.overflow = '';
      }
    });

    window.addEventListener('keydown', function (e) {
      if (e.key === 'Escape' && burger.getAttribute('aria-expanded') === 'true') {
        burger.setAttribute('aria-expanded', 'false');
        menu.classList.remove('is-open');
        document.body.style.overflow = '';
        burger.focus();
      }
    });
  }

  /* ----------------------------------------------------- Revelado suave -- */
  var revealables = document.querySelectorAll('.reveal');
  if (revealables.length) {
    if ('IntersectionObserver' in window) {
      var io = new IntersectionObserver(function (entries) {
        entries.forEach(function (entry) {
          if (entry.isIntersecting) {
            entry.target.classList.add('is-in');
            io.unobserve(entry.target);
          }
        });
      }, { rootMargin: '0px 0px -8% 0px', threshold: 0.08 });

      revealables.forEach(function (el, i) {
        el.style.transitionDelay = Math.min(i % 4, 3) * 70 + 'ms';
        io.observe(el);
      });
    } else {
      revealables.forEach(function (el) { el.classList.add('is-in'); });
    }
  }

  /* -------------------------------------------------- Consentimiento --- */
  /* Sin cookies de terceros por defecto: solo se cargan tras aceptar.     */
  var STORAGE_KEY = 'ppl_cookie_consent';
  var CONSENT_VERSION = 1;

  var banner = document.querySelector('[data-cookie-banner]');

  function readConsent() {
    try {
      var raw = localStorage.getItem(STORAGE_KEY);
      if (!raw) return null;
      var parsed = JSON.parse(raw);
      return parsed && parsed.v === CONSENT_VERSION ? parsed : null;
    } catch (err) {
      return null;
    }
  }

  function saveConsent(status) {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify({
        v: CONSENT_VERSION,
        status: status,
        date: new Date().toISOString()
      }));
    } catch (err) { /* almacenamiento no disponible: no bloquea la web */ }
  }

  function applyConsent(status) {
    document.documentElement.setAttribute('data-consent', status);
    // Punto único de enganche para analítica futura (Netlify Analytics es
    // de servidor y no usa cookies; cualquier script con cookies debe
    // inicializarse aquí y solo si status === 'accepted').
    window.dispatchEvent(new CustomEvent('ppl:consent', { detail: { status: status } }));
  }

  function hideBanner() {
    if (!banner) return;
    banner.classList.remove('is-visible');
    banner.setAttribute('hidden', '');
  }

  function showBanner() {
    if (!banner) return;
    banner.removeAttribute('hidden');
    banner.classList.add('is-visible');
  }

  var stored = readConsent();
  if (stored) {
    applyConsent(stored.status);
  } else {
    applyConsent('pending');
    showBanner();
  }

  document.addEventListener('click', function (e) {
    var trigger = e.target.closest('[data-consent-action]');
    if (!trigger) return;

    var action = trigger.getAttribute('data-consent-action');

    if (action === 'open') {
      e.preventDefault();
      showBanner();
      return;
    }

    saveConsent(action);
    applyConsent(action);
    hideBanner();
  });

  /* ------------------------------------------------------- Año en pie --- */
  var year = String(new Date().getFullYear());
  document.querySelectorAll('[data-year]').forEach(function (el) {
    el.textContent = year;
  });
})();
