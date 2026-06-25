import { Layout, Button, Space, Typography } from 'antd';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/auth-context';

const { Header } = Layout;
const { Title } = Typography;

export default function AppHeader() {
  const navigate = useNavigate();
  const { isLoggedIn, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <Header
      style={{
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        background: '#fff',
        borderBottom: '1px solid #f0f0f0',
        padding: '0 32px',
      }}
    >
      <Title
        level={3}
        style={{ margin: 0, cursor: 'pointer' }}
        onClick={() => navigate('/')}
      >
        LinkLingua
      </Title>

      <Space>
        {isLoggedIn ? (
          <>
            <Button type="primary" onClick={() => navigate('/events/publish')}>
              Publish event
            </Button>
            <Button onClick={handleLogout}>Log out</Button>
          </>
        ) : (
          <>
            <Button onClick={() => navigate('/login')}>Login</Button>
            <Button type="primary" onClick={() => navigate('/login?mode=signup')}>
              Sign up
            </Button>
          </>
        )}
      </Space>
    </Header>
  );
}
