[![Merge](https://github.com/eusphere/monarchy/actions/workflows/merge.yml/badge.svg)](https://github.com/eusphere/monarchy/actions/workflows/merge.yml)

# Monarchy project

This project contains both client and server code.

### Server

Our server is written in Scala using Akka for HTTP and Websocket support. Set up instructions:

1. Set up [redis via brew](https://medium.com/@petehouston/install-and-config-redis-on-mac-os-x-via-homebrew-eb8df9a4f298).
1. Set up Postgres locally via `brew`

```bash
brew install postgres
brew services start postgresql
createdb
psql
> create database monarchy_local;
```

2. Get set up with [Bazel build tooling](https://docs.bazel.build/versions/master/install.html)
1. Run server using

```bash
bazel run //server/src/web
```

4. Set up database schema and seed with some data.

```bash
cat ./database/1-init.sql | psql -d monarchy_local
cat ./database/2-game-schema.sql | psql -d monarchy_local
# and so on, will script this soon
./database/seed.sh
```

5. Route your browser to http://localhost:8080/admin


### Client

You can find the development client in [`clients/web-devel`](https://github.com/abhandaru/monarchy1/tree/master/clients/web-devel). This is simple React/Redux app with [React-bootstrap](https://react-bootstrap.github.io/)

1. In a separate tab, navigate to `clients/web-devel`
1. Run `yarn`
1. Run `yarn dev`

This app is already configured to talk to your server on `localhost:8080`
