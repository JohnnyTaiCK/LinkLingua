import { createBrowserRouter } from 'react-router-dom';
import MainLayout from '../components/MainLayout';
import ProtectedRoute from '../components/ProtectedRoute';
import Home from '../pages/Home';
import Login from '../pages/Login';
import EventDetail from '../pages/EventDetail';
import EventPublish from '../pages/EventPublish';

const router = createBrowserRouter([
  {
    path: '/',
    element: <MainLayout />,
    children: [
      { index: true, element: <Home /> },
      { path: 'events/:id', element: <EventDetail /> },
      {
        path: 'events/publish',
        element: (
          <ProtectedRoute>
            <EventPublish />
          </ProtectedRoute>
        ),
      },
    ],
  },
  { path: '/login', element: <Login /> },
]);

export default router;
