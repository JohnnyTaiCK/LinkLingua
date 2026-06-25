import request from '../utils/request';

// GET /api/events/{id}
export function getEvent(id) {
  return request.get(`/events/${id}`);
}

// POST /api/events
export function createEvent(data) {
  return request.post('/events', data);
}

// PUT /api/events
export function updateEvent(data) {
  return request.put('/events', data);
}

// DELETE /api/events?ids=1,2,3
export function deleteEvents(ids) {
  const value = Array.isArray(ids) ? ids.join(',') : ids;
  return request.delete('/events', { params: { ids: value } });
}

// GET /api/events/page?page=&pageSize=&city=&language=&time=
export function pageEvents(params, config) {
  return request.get('/events/page', { params, ...config });
}
