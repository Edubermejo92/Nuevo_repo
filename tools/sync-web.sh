#!/usr/bin/env bash
# Copia la app web (assets del movil) a web/ para desplegar en Netlify.
# Ejecuta antes ./tools/build-web-assets.sh si has tocado clases de Tailwind.
set -euo pipefail
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
A="$ROOT/PadelPulse-Movil/mobile/src/main/assets"
W="$ROOT/web"
mkdir -p "$W"
rm -rf "$W/fonts"
cp -r "$A/fonts" "$W/fonts"
cp "$A/tailwind.css" "$W/tailwind.css"
cp "$A/logo.png" "$W/logo.png"
cp "$A/code.html" "$W/index.html"
echo "✅ web/ actualizado desde los assets de la app"
