import request from '../utils/request';

// GET /api/city -> [{ id, cityName }]
export function getCities(config) {
  return request.get('/city', config);
}
