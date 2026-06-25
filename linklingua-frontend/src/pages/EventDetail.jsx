import { useEffect, useState } from 'react';
import {
  Typography,
  Descriptions,
  Spin,
  Button,
  Space,
  Image,
  Popconfirm,
  message,
  Result,
} from 'antd';
import { useParams, useNavigate } from 'react-router-dom';
import { getEvent, deleteEvents } from '../api';
import { useAuth } from '../context/auth-context';

const { Title, Text } = Typography;

export default function EventDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();

  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect
    setLoading(true);
    getEvent(id)
      .then((res) => setEvent(res.data))
      .catch(() => message.error('Failed to load event'))
      .finally(() => setLoading(false));
  }, [id]);

  const handleDelete = () => {
    deleteEvents(id)
      .then(() => {
        message.success('Event deleted');
        navigate('/');
      })
      .catch(() => {});
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', paddingTop: 80 }}>
        <Spin size="large" />
      </div>
    );
  }

  if (!event) {
    return <Result status="404" title="Event not found" />;
  }

  const isOwner = user && user.id === event.publishUserId;

  return (
    <div style={{ maxWidth: 800, margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
        <Title level={2}>{event.title}</Title>
        {isOwner && (
          <Space>
            <Button onClick={() => navigate(`/events/publish?id=${event.id}`)}>Edit</Button>
            <Popconfirm title="Delete this event?" onConfirm={handleDelete}>
              <Button danger>Delete</Button>
            </Popconfirm>
          </Space>
        )}
      </div>

      <Text type="secondary">Published by user #{event.publishUserId}</Text>

      <div style={{ margin: '16px 0' }}>
        <Image
          src={event.image}
          alt={event.title}
          width="100%"
          style={{ maxHeight: 420, objectFit: 'cover' }}
          fallback="data:image/svg+xml;charset=utf-8,%3Csvg xmlns='http://www.w3.org/2000/svg' width='800' height='400'%3E%3Crect width='100%25' height='100%25' fill='%23d9d9d9'/%3E%3C/svg%3E"
        />
      </div>

      <Text>{event.description}</Text>

      <Descriptions column={1} style={{ marginTop: 24 }} bordered>
        <Descriptions.Item label="Start Time">{event.eventStartTime}</Descriptions.Item>
        <Descriptions.Item label="Location">{event.location}</Descriptions.Item>
        <Descriptions.Item label="Language">{event.langName}</Descriptions.Item>
        <Descriptions.Item label="City">{event.cityName}</Descriptions.Item>
        <Descriptions.Item label="Max Participants">{event.maxParticipant}</Descriptions.Item>
      </Descriptions>
    </div>
  );
}
