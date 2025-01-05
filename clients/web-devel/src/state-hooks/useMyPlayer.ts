import { Player, State } from '~/state/state';
import { useSelector } from 'react-redux';

// Grab the viewer ID from the auth state. Then compare to the fully hydrated 
// player data in the game state.
export default function useMyPlayer(): Player | null {
  const userId = useSelector<State, String | null>(_ => _.auth.userId);
  const players = useSelector<State, Player[]>(_ => _.games.game.players);
  return players.find(_ => _.user.id === userId);
}
