# Backend architecture

`monarchy-api` is a JVM application written in Scala 2.13. It exposes a GraphQL
API over HTTP and support websocket connections.

## Webserver

The webserver is a standard Akka HTTP server. It is configured to support websocket connections and to serve a GraphQL API over HTTP. It is completely stateless and can be scaled horizontally. We expect connection pressure from clients to demand more replicas.

## Persistence

### Postgres

`monarchy-api` uses a Postgres database to durably store player data. This includes:

* Authentication
* Account settings
* Matchmaking results
* Ratings and leaderboards

### Redis

In addition, we use Redis for the following:

* Live player state
* Live game state
* Matchmaking queues
* Peer-to-peer communication (via PubSub)

## Sub-systems

### Peer-to-peer

As cited above, we use **Redis PubSub** channels to mediate peer-to-peer communication. Events from one client can be sent (bidirectionally) to `monarchy-api` which then processes them and computes an necessary transformations and fanout.

When clients come online, they open a single websocket connection that `monarchy-api` uses to send any relevant events to. This dramatically limits the number of connections as the expense of needing to unmarshal events more carefully on the client, since they are union types.

#### Why not Kafka?

Kafka is great at supporting multi-writer-reader workloads and is quite suitable for high-throughput and durable systems. However, given the time formats we wish to support, low-latency is critical at the sacrifice of durability, so we use Redis PubSub instead. In addition:

* The overhead of maintaining per-connection consumer-groups is cumbersome
* We do not need scrict ordering guarantees or event replay
* For replay-based features, Kafka message expiry is unsuitable

We may still use Kafka for other purposes in the future.

### User online status

When a client connects, we expect it open a singlular websocket connection to `monarchy-api`. As part of the protocol, the server **expects** to receive a `ping` event with client timing data. It turn clients receive a `pong` event with server timing data.

This simple mechanism affords the following:

* Transparency for clients around latency and connection stability
* Clients that send `ping` events are also queued by time in a Redis `ZSET`
* The `ZSET` can then easily compute users online in the last `T` interval

### Matchmaking

The matchmaking system is designed to pair players into a new game. It consists of two types of requests:

* `seek` requests issued by 1 client and broadcast to all other connected clients
* `accept` requests attempt to claim the `seek` request and form a new game