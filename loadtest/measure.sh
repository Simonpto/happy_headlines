#!/bin/sh
# Runs the k6 load test and prints what it cost: database calls, log lines and response times.
# Usage: loadtest/measure.sh [url]
URL=${1:-http://load-balancer/articles/europe}
cd "$(dirname "$0")/.."

db_calls() {
  docker compose exec -T db-europe psql -U postgres -d articles -tAc \
    "SELECT xact_commit FROM pg_stat_database WHERE datname = 'articles'"
}

# Cache hits or misses (result=hit|miss) summed over the three article-service instances
cache_gets() {
  for n in 1 2 3; do
    docker run --rm --network happy_headlines_default curlimages/curl:8.11.1 -s \
      "article-service-$n:8080/actuator/metrics/cache.gets?tag=cache:articles&tag=result:$1"
  done | grep -o '"value":[0-9.]*' | cut -d: -f2 | awk '{ sum += $1 } END { print int(sum) }'
}

before=$(db_calls)
hits_before=$(cache_gets hit)
misses_before=$(cache_gets miss)
since=$(date -u +%Y-%m-%dT%H:%M:%SZ)
docker run --rm -i --network happy_headlines_default -e URL="$URL" grafana/k6:0.57.0 run --quiet - < loadtest/articles.js \
  | grep -E "http_req_duration|http_reqs|data_received|checks"
sleep 12 # Postgres flushes its statistics from idle connections every 10 seconds
after=$(db_calls)
hits=$(( $(cache_gets hit) - hits_before ))
misses=$(( $(cache_gets miss) - misses_before ))

echo "database calls: $((after - before - 1))" # -1 for our own "before" query
logs=$(docker compose logs --no-log-prefix --since "$since" article-service-1 article-service-2 article-service-3)
echo "log lines:      $(printf "%s" "$logs" | grep -c .)"
echo "log bytes:      $(printf "%s" "$logs" | wc -c | tr -d " ")"
echo "cache hits:     $hits"
echo "cache misses:   $misses"
