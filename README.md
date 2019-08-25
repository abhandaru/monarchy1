[![Build Status](https://semaphoreci.com/api/v1/abhandaru/monarchy1/branches/master/badge.svg)](https://semaphoreci.com/abhandaru/monarchy1)

# Monarchy project

This project contains both client and server code.

### Server

Our server is writting in Scala using Akka for HTTP and Websocket support. Set up instructions:

1. Get set up with [Bazel build tooling](https://docs.bazel.build/versions/master/install.html)
1. Run server using

```bash
bazel run //server/src/web
```

3. Route your browser to http://localhost:8080/admin
