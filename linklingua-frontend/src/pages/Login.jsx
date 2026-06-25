import { useState } from 'react';
import { Card, Form, Input, Button, Tabs, Typography, message } from 'antd';
import { useNavigate, useSearchParams, useLocation } from 'react-router-dom';
import { login as loginApi, signup as signupApi } from '../api';
import { useAuth } from '../context/auth-context';

const { Title } = Typography;

export default function Login() {
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();
  const { login } = useAuth();

  const [activeTab, setActiveTab] = useState(
    searchParams.get('mode') === 'signup' ? 'signup' : 'login'
  );
  const [loading, setLoading] = useState(false);

  const redirectTo = location.state?.from?.pathname || '/';

  const handleLogin = (values) => {
    setLoading(true);
    loginApi(values)
      .then((res) => {
        login(res.data);
        message.success('Logged in successfully');
        navigate(redirectTo, { replace: true });
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  const handleSignup = (values) => {
    setLoading(true);
    signupApi({ username: values.username, password: values.password })
      .then(() => {
        message.success('Signed up successfully, please log in');
        setActiveTab('login');
      })
      .catch(() => {})
      .finally(() => setLoading(false));
  };

  const loginForm = (
    <Form layout="vertical" onFinish={handleLogin} requiredMark={false}>
      <Form.Item
        label="Username"
        name="username"
        rules={[{ required: true, message: 'Please enter your username' }]}
      >
        <Input placeholder="Username" />
      </Form.Item>
      <Form.Item
        label="Password"
        name="password"
        rules={[{ required: true, message: 'Please enter your password' }]}
      >
        <Input.Password placeholder="Password" />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit" block loading={loading}>
          Login
        </Button>
      </Form.Item>
    </Form>
  );

  const signupForm = (
    <Form layout="vertical" onFinish={handleSignup} requiredMark={false}>
      <Form.Item
        label="Username"
        name="username"
        rules={[{ required: true, message: 'Please enter a username' }]}
      >
        <Input placeholder="Username" />
      </Form.Item>
      <Form.Item
        label="Password"
        name="password"
        rules={[{ required: true, message: 'Please enter a password' }]}
      >
        <Input.Password placeholder="Password" />
      </Form.Item>
      <Form.Item
        label="Confirm password"
        name="confirm"
        dependencies={['password']}
        rules={[
          { required: true, message: 'Please confirm your password' },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue('password') === value) {
                return Promise.resolve();
              }
              return Promise.reject(new Error('Passwords do not match'));
            },
          }),
        ]}
      >
        <Input.Password placeholder="Confirm password" />
      </Form.Item>
      <Form.Item>
        <Button type="primary" htmlType="submit" block loading={loading}>
          Sign up
        </Button>
      </Form.Item>
    </Form>
  );

  return (
    <div style={{ display: 'flex', justifyContent: 'center', paddingTop: 64 }}>
      <Card style={{ width: 380 }}>
        <Title level={3} style={{ textAlign: 'center' }}>
          LinkLingua
        </Title>
        <Tabs
          activeKey={activeTab}
          onChange={setActiveTab}
          centered
          items={[
            { key: 'login', label: 'Login', children: loginForm },
            { key: 'signup', label: 'Sign up', children: signupForm },
          ]}
        />
      </Card>
    </div>
  );
}
