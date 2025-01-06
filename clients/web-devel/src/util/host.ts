const env = process.env.NODE_ENV || 'local';

export const host = env === 'prod'
  ? 'https://api.monarchy1.com'
  : 'http://localhost:8080';

export default host;