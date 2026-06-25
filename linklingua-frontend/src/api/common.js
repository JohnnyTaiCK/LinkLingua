import request from '../utils/request';

// POST /api/common/upload (multipart/form-data) -> data: image url string
export function uploadFile(file) {
  const formData = new FormData();
  formData.append('file', file);
  return request.post('/common/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });
}
