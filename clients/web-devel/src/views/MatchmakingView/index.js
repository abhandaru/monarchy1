import * as React from 'react';
import Button from 'react-bootstrap/Button';
import ChallengesView from './ChallengesView';
import streamProxy from '~/api/streamProxy';
import styles from './index.css';
import { useSelector } from 'react-redux'

const MatchmakingView = () => {
  const onSeek = React.useCallback(() => streamProxy.send({name: 'ChallengeSeek'}));
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
