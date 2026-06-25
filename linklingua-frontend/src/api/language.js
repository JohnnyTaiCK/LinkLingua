import request from '../utils/request';

// GET /api/language/list -> [{ id, languageName }]
export function getLanguages(config) {
  return request.get('/language/list', config);
}
