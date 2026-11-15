## 1. Avoid Unnecessary Resources:
Stage 1 builds the jar with the full JDK and Maven (eclipse-temurin:25-jdk-alpine).
The final image contains only eclipse-temurin:25-jre-alpine and the jar.
Docker builds from the repo root so the shared monitoring library is included.
All lists (articles, drafts, subscribers) support ?page=&pageSize=. The default is 20 per page and the maximum is 100. Articles are sorted newest first.


| Service | Før (`25-jre`, kun jar kopieret ind) | Efter (multi-stage, `25-jre-alpine`) | Reduktion |
|---|---|---|---|
| article-service | 555 MB | 388 MB | −30 % |
| comment-service | 598 MB | 428 MB | −28 % |
| draft-service | 546 MB | 376 MB | −31 % |
| newsletter-service | 556 MB | 386 MB | −31 % |
| profanity-service | 596 MB | 426 MB | −29 % |
| publisher-service | 551 MB | 381 MB | −31 % |
| subscriber-service | 555 MB | 386 MB | −30 % |
| **I alt (9 containere, article ×3)** | **5067 MB** | **3547 MB** | **−30 %** |


## 2. Fetch Data from the Proximity:


I implemeted @Cacheable on a few methods in Articleervice: findall, findById. Also @CacheEvict on CRUD

| | Uden cache | Med cache |
|---|---|---|
| Databasekald pr. 1000 requests | 1028 | 5 |
| Cache hit-rate | – | 99,8 % (998 hits, 2 misses) |
| Gennemsnitlig svartid | 28,8 ms | 20,6 ms |
| p95 svartid | 57,5 ms | 48,6 ms |
| Requests pr. sekund | 335 | 474 |


## 3. Accept Lower Fidelity:
Log levels are set in one place, the shared monitoring library, so they apply to every service:
monitoring.yaml (development): simon: DEBUG.
monitoring-prod.yaml (production): root: WARN, simon: WARN. It loads automatically when SPRING_PROFILES_ACTIVE=prod.
All services in docker-compose.yml run with the prod profile.
The noisiest logger was RequestLoggingFilter, which logged one INFO line per request. In prod it now only logs failed requests (4xx/5xx).
The “Loaded N drafts” debug line, which ran on every list call, is removed.
Warnings and errors are still logged and sent to Seq. Tracing is unchanged.

| | Udvikling (default) | Produktion (`prod`) |
|---|---|---|
| Log-linjer pr. 1000 requests | 1000 | 0 |
| Log-data pr. 1000 requests | 204 KB | 0 KB |

