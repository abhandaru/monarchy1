import { State } from '~/state/state';
import { useSelector } from 'react-redux';

export default function useMyTurn(): Boolean {
  const playerId = useSelector<State, String | null>(_ => _.auth.userId);
  const currentPlayerId = useSelector<State, String | null>(_ => _.games.game.state.currentPlayerId);
  return playerId === currentPlayerId;
}