import { Card, Tag, Space, Typography } from 'antd';
import { useNavigate } from 'react-router-dom';

const { Text } = Typography;

const FALLBACK_IMAGE =
  'data:image/svg+xml;charset=utf-8,' +
  encodeURIComponent(
    '<svg xmlns="http://www.w3.org/2000/svg" width="400" height="220"><rect width="100%" height="100%" fill="#d9d9d9"/></svg>'
  );

// `event` follows the EventVO shape returned by the API.
export default function EventCard({ event }) {
  const navigate = useNavigate();

  return (
    <Card
      hoverable
      onClick={() => navigate(`/events/${event.id}`)}
      style={{ height: 360, position: 'relative' }}
      cover={
        <div style={{ position: 'relative', height: 180, overflow: 'hidden' }}>
          <img
          alt={event.title}
          src={event.image || FALLBACK_IMAGE}
          style={{ height: 180, width: '100%', objectFit: 'cover' }}
          onError={(e) => {
            e.currentTarget.src = FALLBACK_IMAGE;
          }}
          />

          <Tag
            color={event.eventStatus === 1 ? '#87d068' : event.eventStatus === 2 ? '#1890ff' : '#bfbfbf'}
            style={{
              position: 'absolute',
              top: 8,
              left: 8,
              margin: 0,
              fontSize: 14
            }}
          >
            {event.eventStatus === 1 ? 'Upcoming' : event.eventStatus === 2 ? 'Ongoing' : 'Finished'}
          </Tag>
        </div>
      }
    >
      <Space direction="vertical" size={4} style={{ width: '100%' }}>
        <Text strong>{event.title}</Text>
        <Text type="secondary">{event.eventStartTime}</Text>
        <Text>{event.langName}</Text>
        <Text type="secondary">{event.cityName}</Text>
      </Space>
    </Card>
  );
}
