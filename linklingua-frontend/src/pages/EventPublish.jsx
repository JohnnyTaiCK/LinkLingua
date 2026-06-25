import { useEffect, useState } from 'react';
import {
  Card,
  Form,
  Input,
  InputNumber,
  Select,
  DatePicker,
  Button,
  Space,
  Upload,
  Typography,
  message,
} from 'antd';
import { UploadOutlined } from '@ant-design/icons';
import { useNavigate, useSearchParams } from 'react-router-dom';
import dayjs from 'dayjs';
import {
  createEvent,
  updateEvent,
  getEvent,
  getCities,
  getLanguages,
  uploadFile,
} from '../api';
import { useAuth } from '../context/auth-context';

const { Title } = Typography;
const TIME_FORMAT = 'YYYY-MM-DD HH:mm';
const MAX_IMAGE_SIZE_MB = 10;

const disablePastDate = (current) => {
  return current && current.isBefore(dayjs().startOf('day'));
};

const disabledTime = (selectedDate) => {
  const now = dayjs();
  const minAllowTime = now.add(2, 'hour');
  // 如果选中的不是今天，时分完全不限制
  if (!dayjs(selectedDate).isSame(now, 'day')) {
    return {};
  }

  // 只有选中今天时，才禁用2小时内时分
  const limitHour = minAllowTime.hour();
  const limitMinute = minAllowTime.minute();

  return {
    disabledHours: () => {
      const list = [];
      for (let i = 0; i < limitHour; i++) list.push(i);
      return list;
    },
    disabledMinutes: (hour) => {
      if (hour < limitHour) return Array.from({ length: 60 }, (_, i) => i);
      if (hour === limitHour) {
        const list = [];
        for (let i = 0; i < limitMinute; i++) list.push(i);
        return list;
      }
      return [];
    },
    disabledSeconds: () => []
  };
};


export default function EventPublish() {
  const [form] = Form.useForm();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { user } = useAuth();

  const editId = searchParams.get('id');
  const isEdit = !!editId;

  const [cities, setCities] = useState([]);
  const [languages, setLanguages] = useState([]);
  const [imageUrl, setImageUrl] = useState('');
  const [uploading, setUploading] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    getCities().then((res) => setCities(res.data || [])).catch(() => {});
    getLanguages().then((res) => setLanguages(res.data || [])).catch(() => {});
  }, []);

  useEffect(() => {
    if (!isEdit) return;
    getEvent(editId)
      .then((res) => {
        const e = res.data;
        setImageUrl(e.image || '');
        form.setFieldsValue({
          title: e.title,
          description: e.description,
          location: e.location,
          eventStartTime: e.eventStartTime ? dayjs(e.eventStartTime) : null,
          maxParticipant: e.maxParticipant,
          cityId: e.cityId,
          langId: e.langId,
        });
      })
      .catch(() => message.error('Failed to load event'));
  }, [editId, isEdit, form]);

  const beforeUpload = (file) => {
    const withinLimit = file.size / 1024 / 1024 < MAX_IMAGE_SIZE_MB;
    if (!withinLimit) {
      message.error(`Image must be smaller than ${MAX_IMAGE_SIZE_MB}MB`);
      return Upload.LIST_IGNORE;
    }
    return true;
  };

  const handleUpload = ({ file }) => {
    setUploading(true);
    uploadFile(file)
      .then((res) => {
        setImageUrl(res.data);
        message.success('Image uploaded');
      })
      .catch(() => {})
      .finally(() => setUploading(false));
  };

  const handleSubmit = (values) => {
    const payload = {
      ...values,
      eventStartTime: values.eventStartTime.format(TIME_FORMAT),
      image: imageUrl,
      publishUserId: user?.id,
    };

    setSubmitting(true);
    const action = isEdit
      ? updateEvent({ ...payload, id: Number(editId) })
      : createEvent(payload);

    action
      .then(() => {
        message.success(isEdit ? 'Event updated' : 'Event published');
        navigate(isEdit ? `/events/${editId}` : '/');
      })
      .catch(() => {})
      .finally(() => setSubmitting(false));
  };

  return (
    <div style={{ maxWidth: 720, margin: '0 auto' }}>
      <Card>
        <Title level={3}>{isEdit ? 'Edit Event' : 'Publish New Event'}</Title>
        <Form form={form} layout="vertical" onFinish={handleSubmit} requiredMark={false}>
          <Form.Item
            label="Event Title"
            name="title"
            rules={[{ required: true, message: 'Please enter a title' }]}
          >
            <Input placeholder="Event title" />
          </Form.Item>

          <Form.Item
            label="Description"
            name="description"
            rules={[{ required: true, message: 'Please enter a description' }]}
          >
            <Input.TextArea rows={5} placeholder="Describe your event" />
          </Form.Item>

          <Form.Item
            label="Location"
            name="location"
            rules={[{ required: true, message: 'Please enter a location' }]}
          >
            <Input placeholder="Location" />
          </Form.Item>

          <Form.Item
            label="Start time"
            name="eventStartTime"
            rules={[{ required: true, message: 'Please pick a start time' }]}
          >
            <DatePicker disabledDate={ disablePastDate }
                        disabledTime={ disabledTime }
                        showTime format={TIME_FORMAT} 
                        style={{ width: '100%' }} />
          </Form.Item>

          <Form.Item
            label="Max participants"
            name="maxParticipant"
            rules={[{ required: true, message: 'Please enter max participants' }]}
          >
            <InputNumber min={1} style={{ width: '100%' }} />
          </Form.Item>

          <Form.Item
            label="City"
            name="cityId"
            rules={[{ required: true, message: 'Please select a city' }]}
          >
            <Select
              placeholder="Select a city"
              options={cities.map((c) => ({ value: c.id, label: c.cityName }))}
            />
          </Form.Item>

          <Form.Item
            label="Language"
            name="langId"
            rules={[{ required: true, message: 'Please select a language' }]}
          >
            <Select
              placeholder="Select a language"
              options={languages.map((l) => ({ value: l.id, label: l.languageName }))}
            />
          </Form.Item>

          <Form.Item label="Image">
            <Space direction="vertical">
              <Upload
                customRequest={handleUpload}
                beforeUpload={beforeUpload}
                showUploadList={false}
                accept="image/*"
              >
                <Button icon={<UploadOutlined />} loading={uploading}>
                  Upload image
                </Button>
              </Upload>
              {imageUrl && (
                <img src={imageUrl} alt="preview" style={{ maxWidth: 240, borderRadius: 8 }} />
              )}
            </Space>
          </Form.Item>

          <Form.Item>
            <Space>
              <Button onClick={() => navigate(-1)}>Cancel</Button>
              <Button type="primary" htmlType="submit" loading={submitting}>
                Submit
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </Card>
    </div>
  );
}
