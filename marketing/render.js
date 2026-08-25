#!/usr/bin/env node
/**
 * Renderiza cada pieza de plantillas/piezas.html a PNG en salida/.
 *
 * En el repositorio:      cd <raíz> && python3 -m http.server 8099 & node marketing/render.js
 * En el kit descargable:  cd 4-editable && python3 -m http.server 8099 & node render.js
 *
 * Cada elemento .art se captura a su tamaño exacto en píxeles.
 */
const { chromium } = require('playwright');
const path = require('path');

const fs = require('fs');

const BASE = process.env.BASE_URL || 'http://localhost:8099';
const OUT = path.join(__dirname, 'salida');

// Funciona tanto dentro del repositorio (marketing/plantillas/piezas.html)
// como en la carpeta suelta del kit descargable (piezas.html al lado).
const PAGINA = process.env.PAGE_PATH || (
  fs.existsSync(path.join(__dirname, 'piezas.html'))
    ? '/piezas.html'
    : '/marketing/plantillas/piezas.html'
);

(async () => {
  const browser = await chromium.launch({
    executablePath: process.env.CHROMIUM_PATH || '/opt/pw-browsers/chromium'
  });
  const page = await browser.newPage({ viewport: { width: 1700, height: 1000 }, deviceScaleFactor: 1 });

  const missing = [];
  page.on('requestfailed', r => missing.push(r.url()));

  await page.goto(BASE + PAGINA, { waitUntil: 'networkidle' });
  await page.waitForTimeout(1200);

  const ids = await page.$$eval('.art', els => els.map(e => e.id));
  for (const id of ids) {
    const el = await page.$('#' + id);
    const box = await el.boundingBox();
    await el.screenshot({ path: path.join(OUT, id + '.png') });
    console.log(`  ${id}.png  ${Math.round(box.width)}x${Math.round(box.height)}`);
  }

  if (missing.length) console.log('\nRecursos no cargados:', [...new Set(missing)]);
  console.log(`\n${ids.length} piezas exportadas en ${OUT}`);
  await browser.close();
})();
