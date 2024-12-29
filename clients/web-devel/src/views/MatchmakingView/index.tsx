import * as Actions from '~/state/actions';
import * as React from 'react';
import * as Types from '~/util/types';
import Button from 'react-bootstrap/Button';
import ChallengesView from './ChallengesView';
import fetchLobby from './fetchLobby';
// @ts-ignore
import styles from './index.css';
import writeSeek from './writeSeek';
import { State } from '~/state/state';
import { useDispatch, useSelector } from 'react-redux';

const MatchmakingView = () => {
  const dispatch = useDispatch();
  const onSeek = React.useCallback(() => writeSeek(), []);
  React.useEffect(() => {
    fetchLobby().then(_ => {
      dispatch(Actions.matchmakingSet(_.data.lobby.challenges));
    });
  }, []);

  const userId = useSelector<State, string>(_ => _.auth.userId);
  const challenges = useSelector<State, Types.Challenge[]>(_ => _.matchmaking.challenges);
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
