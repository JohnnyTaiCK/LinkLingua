// Lightweight JWT helpers. We only *read* the payload on the client to check
// expiry for UX/guarding purposes — the backend is still the source of truth
// and must verify the signature on every request.

// Decode the payload segment of a JWT without verifying its signature.
export function decodeJwt(token) {
  try {
    const payloadSegment = token.split('.')[1];
    if (!payloadSegment) return null;
    const base64 = payloadSegment.replace(/-/g, '+').replace(/_/g, '/');
    const json = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    );
    return JSON.parse(json);
  } catch {
    return null;
  }
}

// A token counts as expired when it is missing, malformed, or past its `exp`.
// Tokens without an `exp` claim are treated as non-expiring.
export function isTokenExpired(token) {
  if (!token) return true;
  const payload = decodeJwt(token);
  if (!payload) return true; // malformed -> force re-login
  if (typeof payload.exp !== 'number') return false; // no exp claim
  return Date.now() >= payload.exp * 1000;
}

export function isTokenValid(token) {
  return !!token && !isTokenExpired(token);
}
