import * as Actions from '~/state/actions';
import * as React from 'react';
import Button from 'react-bootstrap/Button';
import ChallengesView from './ChallengesView';
import fetchLobby from './fetchLobby';
import styles from './index.css';
import writeSeek from './writeSeek';
import { useDispatch, useSelector } from 'react-redux'

const MatchmakingView = () => {
  const dispatch = useDispatch();
  const onSeek = React.useCallback(() => writeSeek());
  React.useEffect(() => {
    fetchLobby().then(_ => {
      dispatch(Actions.matchmakingSet(_.data.lobby.challenges));
    });
  }, []);

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
