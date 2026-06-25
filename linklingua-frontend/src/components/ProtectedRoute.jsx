import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/auth-context';
import { getToken } from '../utils/auth';
import { isTokenValid } from '../utils/jwt';

// Wrap routes that require an authenticated user with a non-expired token.
export default function ProtectedRoute({ children }) {
  const { isLoggedIn } = useAuth();
  const location = useLocation();

  const allowed = isLoggedIn && isTokenValid(getToken());
  if (!allowed) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return children;
}
