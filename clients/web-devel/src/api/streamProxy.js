import ReconnectingWebSocket from 'reconnecting-websocket';

const webSocketRoute = 'ws://localhost:8080/connect';
const createWebSocket = () => (
  new ReconnectingWebSocket(webSocketRoute, null, {
    automaticOpen: false,
    debug: true
  })
);

let webSocket = null;

const send = (name, body) => {
  const bodyRaw = body != null ? JSON.stringify(body) : null;
  const payload = JSON.stringify({ name, body: bodyRaw });
  webSocket && webSocket.send(payload);
};

const connect = () => {
  webSocket && webSocket.close();
  webSocket = createWebSocket();
};

const listenerGen = (handler) => (raw) => {
  handler(JSON.parse(raw.data))
};

const listen = (handler) => {
  webSocket && (webSocket.onmessage = listenerGen(handler));
};

export default {
  connect,
  send,
  listen
};
