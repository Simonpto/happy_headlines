// Load test for the front page: 1000 requests spread over 10 virtual users.
// Run: docker run --rm -i --network happy_headlines_default -e URL=<url> grafana/k6:0.57.0 run - < loadtest/articles.js
import http from 'k6/http';
import { check } from 'k6';

export const options = { vus: 10, iterations: 1000 };

export default function () {
  const res = http.get(__ENV.URL || 'http://load-balancer/articles/europe');
  check(res, { 'status is 200': r => r.status === 200 });
}
