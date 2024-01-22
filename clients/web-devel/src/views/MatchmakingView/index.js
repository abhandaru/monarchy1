import * as React from 'react';
import Button from 'react-bootstrap/Button';
import ChallengesView from './ChallengesView';
import styles from './index.css';
import writeSeek from './writeSeek';
import { useSelector } from 'react-redux'

const MatchmakingView = () => {
  const onSeek = React.useCallback(() => writeSeek());
  const userId = useSelector(_ => _.auth.userId);
  const challenges = useSelector(_ => _.matchmaking.challenges);
  const challengeTable = challenges.length > 0 ?
    <ChallengesView className={styles.challenges} challenges={challenges} /> : null;
  return (
    <div>
      <h3>Matchmaking</h3>
      <Button onClick={onSeek}>Seek challenge</Button>
      {challengeTable}
    </div>
  );
};

export default MatchmakingView;
