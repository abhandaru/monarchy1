import * as React from 'react';
import Button from 'react-bootstrap/Button';
import webSocket from '~/api/webSocket';

const MatchmakingView = () => {
  const onSeek = React.useCallback(() => webSocket.send(JSON.stringify({name: 'ChallengeSeek'})));

  return (
    <div>
      <h3>Matchmaking</h3>
      <Button onClick={onSeek}>Seek challenge</Button>
    </div>
  );
};

export default MatchmakingView;
