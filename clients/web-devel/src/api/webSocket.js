import ReconnectingWebSocket from 'reconnecting-websocket';

const webSocketRoute = 'ws://localhost:8080/connect';
const webSocket = new ReconnectingWebSocket(webSocketRoute, null, {
  debug: true
});

export default webSocket;
