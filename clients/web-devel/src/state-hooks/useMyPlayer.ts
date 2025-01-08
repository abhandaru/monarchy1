import * as Types from '~/util/types';
import { State } from '~/state/state';
import { useSelector } from 'react-redux';

// Grab the viewer ID from the auth state. Then compare to the fully hydrated 
// player data in the game state.
export default function useMyPlayer(): Types.Player | null {
  const userId = useSelector<State, string | null>(_ => _.auth.userId);
  const players = useSelector<State, Types.Player[]>(_ => _.games.game.players);
  return players.find(_ => _.user.id === userId);
}
