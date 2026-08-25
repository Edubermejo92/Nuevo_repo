#!/usr/bin/env bash
# Genera el ZIP descargable del kit de marketing a partir de marketing/.
# Uso:  bash marketing/empaquetar.sh
set -euo pipefail

AQUI="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
RAIZ="$(dirname "$AQUI")"
NOMBRE="PadelPulseLive-kit-marketing"
TMP="$(mktemp -d)"
DEST="$TMP/$NOMBRE"

mkdir -p "$DEST"/{1-google-play/capturas-movil,1-google-play/capturas-wear-os,2-web-y-redes,3-logotipos,4-editable}

S="$AQUI/salida"

# --- Google Play ---
cp "$AQUI/fuentes/icono-app-512.png"        "$DEST/1-google-play/icono-512.png"
cp "$S/play-grafico-destacado.png"          "$DEST/1-google-play/grafico-destacado-1024x500.png"
cp "$S/play-movil-1-voz.png"                "$DEST/1-google-play/capturas-movil/1-voz.png"
cp "$S/play-movil-2-reloj.png"              "$DEST/1-google-play/capturas-movil/2-reloj.png"
cp "$S/play-movil-3-idiomas.png"            "$DEST/1-google-play/capturas-movil/3-idiomas.png"
cp "$S/play-movil-4-arbitro.png"            "$DEST/1-google-play/capturas-movil/4-arbitro-ia.png"
cp "$S/play-movil-5-modos.png"              "$DEST/1-google-play/capturas-movil/5-modos.png"
cp "$S/play-movil-6-sin-suscripcion.png"    "$DEST/1-google-play/capturas-movil/6-sin-suscripcion.png"
cp "$S/play-wear-1-marcador.png"            "$DEST/1-google-play/capturas-wear-os/1-marcador.png"
cp "$S/play-wear-2-voz.png"                 "$DEST/1-google-play/capturas-wear-os/2-voz.png"
cp "$S/play-wear-3-set.png"                 "$DEST/1-google-play/capturas-wear-os/3-tie-break.png"

# --- Web y redes ---
cp "$S/web-banner-1600x600.png"             "$DEST/2-web-y-redes/banner-web-1600x600.png"
cp "$S/social-instagram-1080.png"           "$DEST/2-web-y-redes/instagram-1080x1080.png"
cp "$S/social-story-1080x1920.png"          "$DEST/2-web-y-redes/historia-1080x1920.png"
cp "$RAIZ/assets/img/og-image.png"          "$DEST/2-web-y-redes/compartir-enlace-1200x630.png"

# --- Logotipos ---
cp "$AQUI/fuentes/logo.png"                 "$DEST/3-logotipos/logotipo-completo.png"
cp "$AQUI/fuentes/pala.png"                 "$DEST/3-logotipos/pala-sola.png"
cp "$AQUI/fuentes/icono-app-512.png"        "$DEST/3-logotipos/icono-app-512.png"

# --- Editable (rutas relativas para que funcione fuera del repositorio) ---
cp -r "$AQUI/fuentes"                       "$DEST/4-editable/fuentes"
cp "$AQUI/plantillas/base.css"              "$DEST/4-editable/base.css"
sed 's|/marketing/fuentes/|fuentes/|g'      "$AQUI/plantillas/piezas.html" > "$DEST/4-editable/piezas.html"
cp "$AQUI/render.js"                        "$DEST/4-editable/render.js"
mkdir -p "$DEST/4-editable/salida"

cp "$AQUI/LEEME.md"                         "$DEST/LEEME.md"

( cd "$TMP" && zip -qr9 "$NOMBRE.zip" "$NOMBRE" )
mv "$TMP/$NOMBRE.zip" "$AQUI/$NOMBRE.zip"
rm -rf "$TMP"

echo "Creado: marketing/$NOMBRE.zip  ($(du -h "$AQUI/$NOMBRE.zip" | cut -f1))"
