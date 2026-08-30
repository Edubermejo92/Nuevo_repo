#!/usr/bin/env bash
# Prepara la carpeta que se publica en Netlify.
#
# Deja fuera el README y las migraciones de Supabase: al sitio solo suben
# los archivos que forman la app. Los ajustes de cabeceras y rutas van en
# _headers y _redirects (y no en netlify.toml) para que funcionen igual
# tanto si arrastras la carpeta a mano como si Netlify la construye desde git.
set -euo pipefail

OUT="${1:-dist}"
rm -rf "$OUT"
mkdir -p "$OUT"
cp index.html manifest.json sw.js "$OUT/"

# El HTML y el service worker no se cachean: así un redespliegue llega a los
# usuarios en la siguiente carga, en vez de dejarles una versión vieja.
cat > "$OUT/_headers" << 'HEADERS'
/index.html
  Cache-Control: public, max-age=0, must-revalidate

/sw.js
  Cache-Control: public, max-age=0, must-revalidate

/manifest.json
  Cache-Control: public, max-age=3600

/*
  X-Content-Type-Options: nosniff
  Referrer-Policy: strict-origin-when-cross-origin
  X-Frame-Options: SAMEORIGIN
  Permissions-Policy: microphone=(self), camera=(self), geolocation=()
HEADERS

# App de una sola página: cualquier ruta que no sea un archivo sirve la app.
cat > "$OUT/_redirects" << 'REDIRECTS'
/*    /index.html    200
REDIRECTS

echo "Carpeta lista en $OUT/:"
ls -1 "$OUT"
