/*
  fallback.js

  Purpose:
  - If the CDN script is blocked by browser tracking prevention, attempt to load local vendor files.
  - If local files are not present, show a helpful message with remediation steps.
*/

function loadScript(src, onload, onerror) {
  const s = document.createElement('script');
  s.src = src;
  s.async = true;
  s.onload = onload;
  s.onerror = onerror;
  document.head.appendChild(s);
}

function loadLocalScalar() {
  // Try to load vendor copy served from our app
  const localJs = '/scalar/vendor/standalone.js';
  const localCss = '/scalar/vendor/scalar.css';

  // Switch CSS to local if not already
  const css = document.getElementById('scalar-css');
  if (css && css.href && css.href.indexOf('/scalar/vendor/') === -1) {
    css.href = localCss;
  }

  loadScript(localJs, function() {
    // success: nothing else to do, Scalar will initialize
    console.log('Loaded local Scalar script.');
  }, function() {
    // both CDN and local failed -> show guidance
    showFallbackMessage();
  });
}

function showFallbackMessage() {
  const root = document.getElementById('scalar-root') || document.body;
  root.innerHTML = ''; // clear

  const container = document.createElement('div');
  container.className = 'scalar-fallback';
  container.innerHTML = `
    <h1>Documentación (Scalar) no disponible</h1>
    <p>El navegador ha bloqueado el acceso a los recursos de terceros (CDN) y no se encontró una copia local.</p>
    <p>Opciones:</p>
    <ul>
      <li>Permitir recursos de terceros para este sitio en la configuración de privacidad del navegador.</li>
      <li>Descargar los archivos de Scalar en <code>/static/scalar/vendor/</code> (vea <a href="/scalar/vendor/README.html">instrucciones</a>).</li>
      <li>Usar la página alternativa de OpenAPI: <a href="/v3/api-docs">/v3/api-docs</a> (raw JSON).</li>
    </ul>
  `;
  root.appendChild(container);
}

// If the CDN script loads but Scalar does not initialize within a timeout, try local fallback
setTimeout(function() {
  if (typeof window.Scalar === 'undefined' && typeof window['ApiReference'] === 'undefined') {
    // likely blocked or failed
    console.warn('Scalar not initialized from CDN, attempting local load...');
    loadLocalScalar();
  }
}, 1500);
