#!/usr/bin/env bash
# ─────────────────────────────────────────────────────────────────────────
# Regenera assets/tailwind.css a partir de assets/code.html.
#
# IMPORTANTE: ejecuta esto SIEMPRE que añadas o cambies clases de Tailwind
# en code.html. El CSS es estatico (la app no carga el CDN para funcionar
# sin cobertura), asi que una clase nueva que no pase por aqui simplemente
# no tiene estilos.
#
#   ./tools/build-web-assets.sh
#
# Requiere Node 18+. La primera vez se descarga Tailwind (npx).
# ─────────────────────────────────────────────────────────────────────────
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ASSETS="$ROOT/PadelPulse-Movil/mobile/src/main/assets"
WORK="$(mktemp -d)"
trap 'rm -rf "$WORK"' EXIT

cat > "$WORK/tailwind.config.js" <<CONF
module.exports = {
  darkMode: "class",
  content: ["$ASSETS/code.html"],
  theme: { extend: {
    colors: {
      "primary": "#a4ffb9", "primary-container": "#00fd87", "primary-dim": "#00ed7e",
      "on-primary": "#006532", "background": "#0e0e0e", "surface": "#0e0e0e",
      "surface-container": "#1a1919", "surface-container-low": "#131313",
      "surface-container-high": "#201f1f", "surface-container-highest": "#262626",
      "surface-container-lowest": "#000000", "surface-bright": "#2c2c2c",
      "on-surface": "#ffffff", "on-surface-variant": "#adaaaa",
      "outline": "#777575", "outline-variant": "#494847",
      "tertiary": "#7ee6ff", "tertiary-dim": "#00cdee",
      "error": "#ff716c", "error-dim": "#d7383b"
    },
    fontFamily: { "body": ["Lexend", "sans-serif"] }
  } }
}
CONF

printf '@tailwind base;@tailwind components;@tailwind utilities;\n' > "$WORK/in.css"

npx --yes tailwindcss@3.4.17 \
  -c "$WORK/tailwind.config.js" \
  -i "$WORK/in.css" \
  -o "$ASSETS/tailwind.css" \
  --minify

echo "✅ tailwind.css regenerado ($(wc -c < "$ASSETS/tailwind.css") bytes)"
